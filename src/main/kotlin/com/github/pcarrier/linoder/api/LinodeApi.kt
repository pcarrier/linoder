package com.github.pcarrier.linoder.api

import com.github.pcarrier.linoder.model.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ApiException(message: String, val statusCode: Int? = null) : RuntimeException(message)

class LinodeApi(
    private val token: String,
    private val baseUrl: String = "https://api.linode.com/v4",
    private val debug: Boolean = false,
) {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    private fun request(path: String): Request.Builder =
        Request.Builder()
            .url("$baseUrl$path")
            .header("Authorization", "Bearer $token")

    private fun execute(request: Request): String {
        if (debug) System.err.println("${request.method} ${request.url}")
        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: ""
        if (debug) System.err.println("${response.code}: $body")
        if (!response.isSuccessful) {
            throw ApiException("API error ${response.code}: $body", response.code)
        }
        return body
    }

    fun listRegions(): List<Region> {
        val body = execute(request("/regions").build())
        return json.decodeFromString<PaginatedResponse<Region>>(body).data
    }

    fun listTypes(): List<LinodeType> {
        val body = execute(request("/linode/types").build())
        return json.decodeFromString<PaginatedResponse<LinodeType>>(body).data
    }

    fun listInstances(): List<Instance> {
        val body = execute(request("/linode/instances").build())
        return json.decodeFromString<PaginatedResponse<Instance>>(body).data
    }

    fun getInstance(id: Int): Instance {
        val body = execute(request("/linode/instances/$id").build())
        return json.decodeFromString<Instance>(body)
    }

    fun getIps(instanceId: Int): IpInfo {
        val body = execute(request("/linode/instances/$instanceId/ips").build())
        return json.decodeFromString<IpInfo>(body)
    }

    fun bootInstance(id: Int) {
        val req = request("/linode/instances/$id/boot")
            .post("{}".toRequestBody("application/json".toMediaType()))
            .build()
        execute(req)
    }

    fun rebootInstance(id: Int) {
        val req = request("/linode/instances/$id/reboot")
            .post("{}".toRequestBody("application/json".toMediaType()))
            .build()
        execute(req)
    }

    fun shutdownInstance(id: Int) {
        val req = request("/linode/instances/$id/shutdown")
            .post("{}".toRequestBody("application/json".toMediaType()))
            .build()
        execute(req)
    }

    fun cloneInstance(id: Int, clone: CloneRequest): Instance {
        val payload = json.encodeToString(CloneRequest.serializer(), clone)
        val req = request("/linode/instances/$id/clone")
            .post(payload.toRequestBody("application/json".toMediaType()))
            .build()
        return json.decodeFromString<Instance>(execute(req))
    }

    fun deleteInstance(id: Int) {
        val req = request("/linode/instances/$id")
            .delete()
            .build()
        execute(req)
    }
}
