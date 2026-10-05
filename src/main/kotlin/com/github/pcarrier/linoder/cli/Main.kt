package com.github.pcarrier.linoder.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.pcarrier.linoder.api.LinodeApi
import com.github.pcarrier.linoder.model.CloneRequest
import java.io.File

class Linoder : CliktCommand(name = "linoder") {
    override fun run() = Unit
}

abstract class ApiCommand(name: String, help: String = "") : CliktCommand(name = name, help = help) {
    private val apiKey by option("-k", "--key", help = "API token")
    private val debug by option("-d", "--debug", help = "Debug mode").flag()
    private val verbose by option("-v", "--verbose", help = "Verbose output").flag()

    protected fun api(): LinodeApi {
        val token = apiKey
            ?: System.getenv("LINODE_TOKEN")
            ?: File(System.getProperty("user.home"), ".linode-token").let {
                if (it.exists()) it.readText().trim() else null
            }
            ?: error("No API token. Use -k, LINODE_TOKEN env var, or ~/.linode-token")
        return LinodeApi(token, debug = debug)
    }

    protected fun isVerbose() = verbose
}

class RegionGroup : CliktCommand(name = "region", help = "Manage regions") {
    override fun run() = Unit
}

class RegionList : ApiCommand("list", "List available regions") {
    override fun run() {
        for (region in api().listRegions().sortedBy { it.id }) {
            echo("${region.id}\t${region.label} (${region.country})")
            if (isVerbose()) {
                echo("  status: ${region.status}")
                echo("  capabilities: ${region.capabilities.joinToString(", ")}")
            }
        }
    }
}

class TypeGroup : CliktCommand(name = "type", help = "Manage instance types") {
    override fun run() = Unit
}

class TypeList : ApiCommand("list", "List available instance types") {
    override fun run() {
        for (type in api().listTypes().sortedBy { it.id }) {
            val price = type.price.monthly?.let { "\$$it/mo" } ?: "N/A"
            echo("${type.id}\t${type.label}\t${type.memory}MB RAM\t${type.disk}MB disk\t${type.vcpus} vCPUs\t${type.transfer}GB xfer\t$price")
        }
    }
}

class InstanceGroup : CliktCommand(name = "linode", help = "Manage Linode instances") {
    override fun run() = Unit
}

class InstanceList : ApiCommand("list", "List instances") {
    override fun run() {
        val api = api()
        for (instance in api.listInstances().sortedBy { it.id }) {
            echo("${instance.id}\t${instance.label}\t${instance.status}\t${instance.region}\t${instance.ipv4.joinToString(",")}")
            if (isVerbose()) {
                instance.specs?.let { s ->
                    echo("  ${s.memory}MB RAM, ${s.disk}MB disk, ${s.vcpus} vCPUs, ${s.transfer}GB xfer")
                }
                echo("  tags: ${instance.tags.joinToString(", ").ifEmpty { "none" }}")
                echo("  watchdog: ${instance.watchdogEnabled}, backups: ${instance.backups?.enabled ?: false}")
                val ips = api.getIps(instance.id)
                val allIps = (ips.ipv4?.public.orEmpty()) + (ips.ipv4?.private.orEmpty())
                for (ip in allIps) {
                    val scope = if (ip.public) "public" else "private"
                    echo("  ${ip.address} ($scope${ip.rdns?.let { ", $it" } ?: ""})")
                }
            }
        }
    }
}

class InstanceShow : ApiCommand("show", "Show instance details") {
    private val targets by argument(help = "Instance IDs or labels").multiple(required = true)
    override fun run() {
        val api = api()
        val instances = api.listInstances()
        for (target in targets) {
            val found = target.toIntOrNull()?.let { id -> instances.filter { it.id == id } }
                ?: instances.filter { it.label == target }
            if (found.isEmpty()) {
                echo("Not found: $target", err = true)
                continue
            }
            for (instance in found) {
                echo("${instance.id}\t${instance.label}\t${instance.status}")
                echo("  region: ${instance.region}, type: ${instance.type ?: "unknown"}")
                instance.specs?.let { s ->
                    echo("  ${s.memory}MB RAM, ${s.disk}MB disk, ${s.vcpus} vCPUs, ${s.transfer}GB xfer")
                }
                echo("  ipv4: ${instance.ipv4.joinToString(", ")}")
                instance.ipv6?.let { echo("  ipv6: $it") }
                echo("  watchdog: ${instance.watchdogEnabled}, backups: ${instance.backups?.enabled ?: false}")
                val ips = api.getIps(instance.id)
                val allIps = (ips.ipv4?.public.orEmpty()) + (ips.ipv4?.private.orEmpty())
                for (ip in allIps) {
                    val scope = if (ip.public) "public" else "private"
                    echo("  ${ip.address} ($scope${ip.rdns?.let { ", $it" } ?: ""})")
                }
            }
        }
    }
}

class InstanceBoot : ApiCommand("boot", "Boot an instance") {
    private val targets by argument(help = "Instance IDs or labels").multiple(required = true)
    override fun run() {
        val api = api()
        for (id in resolveIds(api, targets)) {
            api.bootInstance(id)
            echo("Booted instance $id")
        }
    }
}

class InstanceClone : ApiCommand("clone", "Clone an instance") {
    private val source by argument(help = "Source instance ID or label")
    private val label by option("-l", "--label", help = "Label for the clone")
    private val region by option("-r", "--region", help = "Target region")
    private val type by option("-t", "--type", help = "Target type")
    override fun run() {
        val api = api()
        val sourceId = resolveIds(api, listOf(source)).first()
        val result = api.cloneInstance(sourceId, CloneRequest(region = region, type = type, label = label))
        echo("Cloned instance $sourceId -> ${result.id} (${result.label})")
    }
}

class InstanceRemove : ApiCommand("rm", "Remove instances") {
    private val targets by argument(help = "Instance IDs or labels").multiple(required = true)
    override fun run() {
        val api = api()
        for (id in resolveIds(api, targets)) {
            api.deleteInstance(id)
            echo("Deleted instance $id")
        }
    }
}

private fun resolveIds(api: LinodeApi, targets: List<String>): List<Int> {
    val instances by lazy { api.listInstances() }
    return targets.map { target ->
        target.toIntOrNull() ?: instances.firstOrNull { it.label == target }?.id
        ?: error("Instance not found: $target")
    }
}

fun main(args: Array<String>) = Linoder()
    .subcommands(
        RegionGroup().subcommands(RegionList()),
        TypeGroup().subcommands(TypeList()),
        InstanceGroup().subcommands(
            InstanceList(),
            InstanceShow(),
            InstanceBoot(),
            InstanceClone(),
            InstanceRemove(),
        ),
    )
    .main(args)
