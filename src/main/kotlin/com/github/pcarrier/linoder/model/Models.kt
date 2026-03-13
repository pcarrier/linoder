package com.github.pcarrier.linoder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Region(
    val id: String,
    val label: String,
    val country: String,
    val capabilities: List<String> = emptyList(),
    val status: String,
)

@Serializable
data class Price(
    val monthly: Double? = null,
    val hourly: Double? = null,
)

@Serializable
data class LinodeType(
    val id: String,
    val label: String,
    val memory: Int,
    val vcpus: Int,
    val disk: Int,
    val transfer: Int,
    val price: Price,
)

@Serializable
data class Instance(
    val id: Int,
    val label: String,
    val status: String,
    val type: String? = null,
    val region: String,
    val ipv4: List<String> = emptyList(),
    val ipv6: String? = null,
    val specs: Specs? = null,
    val tags: List<String> = emptyList(),
    @SerialName("backups") val backups: BackupInfo? = null,
    @SerialName("watchdog_enabled") val watchdogEnabled: Boolean = false,
)

@Serializable
data class Specs(
    val disk: Int = 0,
    val memory: Int = 0,
    val vcpus: Int = 0,
    val transfer: Int = 0,
)

@Serializable
data class BackupInfo(
    val enabled: Boolean = false,
)

@Serializable
data class IpAddress(
    val address: String,
    val type: String,
    val public: Boolean,
    val rdns: String? = null,
    @SerialName("linode_id") val linodeId: Int,
)

@Serializable
data class IPv4(
    val public: List<IpAddress> = emptyList(),
    val private: List<IpAddress> = emptyList(),
)

@Serializable
data class IpInfo(
    val ipv4: IPv4? = null,
)

@Serializable
data class PaginatedResponse<T>(
    val data: List<T>,
    val page: Int,
    val pages: Int,
    val results: Int,
)

@Serializable
data class CloneRequest(
    val region: String? = null,
    val type: String? = null,
    val label: String? = null,
)
