package com.near_reality.api

import com.zenyte.cores.CoresManager
import com.zenyte.game.GameConstants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
import okhttp3.Interceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * Represents a HTTP client that handles communication with game-api.
 *
 * @author Stan van der Bend
 */
object APIClient {

    val logger: Logger = LoggerFactory.getLogger(APIClient::class.java)

    val enabled get() = GameConstants.WORLD_PROFILE.isApiEnabled()
    val apiSettings get() = GameConstants.WORLD_PROFILE.api?: throw IllegalStateException("API settings are not configured.")

    val client by lazy {
        HttpClient(OkHttp) {
            install(Logging) {
                level = LogLevel.BODY
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 10_000
            }
            install(ContentNegotiation) {
                json()
            }
            install(Resources)
            engine {
                config {
                    connectTimeout(10_000, TimeUnit.MILLISECONDS)
                    readTimeout(10_000, TimeUnit.MILLISECONDS)
                    writeTimeout(10_000, TimeUnit.MILLISECONDS)
                    dispatcher(Dispatcher().apply {
                        maxRequests = 500
                        maxRequestsPerHost = 500
                    })
                }
                addNetworkInterceptor { chain: Interceptor.Chain ->
                    chain.proceed(
                        chain.request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer " + apiSettings.token)
                            .addHeader("User-Agent", "Game Server")
                            .build()
                    )
                }
            }
        }
    }

    inline fun <reified T : Any, reified R> post(
        path: String,
        request: T? = null,
        async: Boolean = true,
        crossinline onFailed: HttpResponse?.(Exception?) -> Unit = {},
        crossinline onSuccess: R.() -> Unit = {},
    ): Boolean {
        if (!enabled) {
            logger.debug("Ignored POST request (route={}) {} because API is disabled on this world.", path, request)
            return false
        }
        request(
            path,
            async,
            builder = { requestBuilder ->
                post {
                    takeFrom(requestBuilder)
                    if (request != null) {
                        contentType(ContentType.Application.Json)
                        setBody(request)
                    }
                }
            },
            onSuccess,
            onFailed
        )
        return true
    }

    inline fun <reified T : Any, reified R, reified RESOURCE : Any> post(
        resource: RESOURCE,
        request: T?,
        async: Boolean = true,
        crossinline onFailed: HttpResponse?.(Exception?) -> Unit = {},
        crossinline onSuccess: R.() -> Unit = {},
    ): Boolean {
        return post(client.href(resource), request, async, onFailed, onSuccess)
    }

    inline fun <reified R, reified RESOURCE : Any> post(
        resource: RESOURCE,
        async: Boolean = true,
        crossinline onFailed: HttpResponse?.(Exception?) -> Unit = {},
        crossinline onSuccess: R.() -> Unit = {},
    ): Boolean {
        return post(client.href(resource), null, async, onFailed, onSuccess)
    }

    inline fun <reified R> get(
        path: String,
        async: Boolean = true,
        crossinline onFailed: HttpResponse?.(Exception?) -> Unit = {},
        crossinline onSuccess: R.() -> Unit,
    ): Boolean {
        if (!enabled) {
            logger.debug("Ignored GET request (route={}) because API is disabled on this world.", path)
            return false
        }
        request(
            path,
            async,
            builder = { requestBuilder -> get(requestBuilder) },
            onSuccess,
            onFailed
        )
        return true
    }


    inline fun <reified R, reified RESOURCE : Any> get(
        resource: RESOURCE,
        async: Boolean = true,
        crossinline onFailed: HttpResponse?.(Exception?) -> Unit = {},
        crossinline onSuccess: R.() -> Unit,
    ): Boolean {
        return get(client.href(resource), async, onFailed, onSuccess)
    }

    inline fun <reified R> request(
        path: String,
        async: Boolean,
        crossinline builder: suspend HttpClient.(HttpRequestBuilder) -> HttpResponse,
        crossinline onResponse: R.() -> Unit,
        crossinline onFailed: HttpResponse?.(Exception?) -> Unit,
    ) {
        val runTask = Runnable {
            runBlocking {
                try {
                    val response = builder(
                        client, HttpRequestBuilder(
                            scheme = apiSettings.scheme,
                            host = apiSettings.host,
                            port = apiSettings.port,
                            path = path
                        )
                    )
                    if (response.status == HttpStatusCode.OK) {
                        try {
                            val body = response.body<R>()
                            try {
                                onResponse(body)
                            } catch (e: Exception) {
                                logger.error("Failed to handle response {}", body, e)
                                onFailed(response, e)
                            }
                        } catch (e: Exception) {
                            logger.error("Failed to parse response {}", response, e)
                            onFailed(response, e)
                        }
                    } else {
                        logger.warn("Received response {}", response)
                        onFailed(response, null)
                    }
                } catch(e: Exception) {
                    logger.warn("Request at {} (async={}) failed", path, async, e)
                    onFailed(null, null)
                }
            }
        }
        if (async)
            CoresManager.getServiceProvider().submit(runTask)
        else
            runTask.run()
    }


    @PublishedApi
    internal fun HttpClient.resources(): io.ktor.resources.Resources {
        return pluginOrNull(Resources) ?: throw IllegalStateException("Resources plugin is not installed")
    }
}
