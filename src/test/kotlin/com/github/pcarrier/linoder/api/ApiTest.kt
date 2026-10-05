package com.github.pcarrier.linoder.api

import com.github.pcarrier.linoder.model.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApiTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `deserialize region list`() {
        val raw = """
            {"data":[{"id":"us-east","label":"Newark, NJ","country":"us","capabilities":["Linodes"],"status":"ok"}],"page":1,"pages":1,"results":1}
        """.trimIndent()
        val response = json.decodeFromString<PaginatedResponse<Region>>(raw)
        assertEquals(1, response.data.size)
        assertEquals("us-east", response.data[0].id)
        assertEquals("Newark, NJ", response.data[0].label)
    }

    @Test
    fun `deserialize type list`() {
        val raw = """
            {"data":[{"id":"g6-standard-2","label":"Linode 4GB","memory":4096,"vcpus":2,"disk":81920,"transfer":4000,"price":{"monthly":20.0,"hourly":0.03}}],"page":1,"pages":1,"results":1}
        """.trimIndent()
        val response = json.decodeFromString<PaginatedResponse<LinodeType>>(raw)
        assertEquals("g6-standard-2", response.data[0].id)
        assertEquals(4096, response.data[0].memory)
        assertEquals(20.0, response.data[0].price.monthly)
    }

    @Test
    fun `deserialize instance`() {
        val raw = """
            {"id":123,"label":"my-linode","status":"running","region":"us-east","type":"g6-standard-2","ipv4":["192.0.2.1"],"ipv6":"2600:3c01::1/64","specs":{"disk":81920,"memory":4096,"vcpus":2,"transfer":4000},"tags":["web"],"backups":{"enabled":true},"watchdog_enabled":true}
        """.trimIndent()
        val instance = json.decodeFromString<Instance>(raw)
        assertEquals(123, instance.id)
        assertEquals("running", instance.status)
        assertTrue(instance.watchdogEnabled)
        assertEquals(listOf("192.0.2.1"), instance.ipv4)
        assertEquals(4096, instance.specs?.memory)
    }

    @Test
    fun `deserialize ip info`() {
        val raw = """
            {"ipv4":{"public":[{"address":"192.0.2.1","type":"ipv4","public":true,"rdns":"example.com","linode_id":123}],"private":[]}}
        """.trimIndent()
        val ips = json.decodeFromString<IpInfo>(raw)
        assertEquals(1, ips.ipv4?.public?.size)
        assertEquals("192.0.2.1", ips.ipv4?.public?.get(0)?.address)
        assertEquals("example.com", ips.ipv4?.public?.get(0)?.rdns)
    }

    @Test
    fun `serialize clone request`() {
        val req = CloneRequest(region = "us-west", label = "clone-1")
        val serialized = json.encodeToString(CloneRequest.serializer(), req)
        assertTrue(serialized.contains("us-west"))
        assertTrue(serialized.contains("clone-1"))
    }
}
