package com.gowri.ElectricityBillingSystem.connector

import org.apache.http.HttpHost
import org.apache.http.client.HttpRequestRetryHandler
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase
import org.apache.http.config.SocketConfig
import org.apache.http.conn.HttpClientConnectionManager
import org.apache.http.conn.routing.HttpRoute
import org.apache.http.impl.DefaultConnectionReuseStrategy
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.util.EntityUtils
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Service
class HttpConnector {


    lateinit var client: CloseableHttpClient

    lateinit var connectionManager: HttpClientConnectionManager

    private lateinit var idleConnectionRemovalWorker: ScheduledExecutorService

    @Value("\${lora.connector.soTimeOutInMillis}")
    private lateinit var soTimeOutInMillis: String

    @Value("\${lora.connector.connectionRequestTimeoutInMilliSec}")
    internal lateinit var connectionRequestTimeoutInMilliSec: String

    @Value("\${lora.connector.connectTimeoutInMilliSec}")
    internal lateinit var connectTimeoutInMilliSec: String

    @Value("\${lora.connector.defaultMaxNoOfConnectionsPerRoute}")
    private lateinit var defaultMaxNoOfConnectionsPerRoute: String

    @Value("\${lora.connector.maxNoOfConnections}")
    private lateinit var maxNoOfConnections: String

    @Value("\${lora.connector.idleConnectionCheckPeriodInMilliSec}")
    private lateinit var idleConnectionCheckPeriodInMilliSec: String

    @Value("\${lora.connector.maxIdleTimeInMilliSec}")
    private lateinit var maxIdleTimeInMilliSec: String

    private var enableIdleConnectionCheck: Boolean = false

    @Value("\${lora.endpointUri}")
    private lateinit var endPointUrl: String



    private val logger = LogManager.getFormatterLogger(HttpConnector::class.java)

    @Throws(MalformedURLException::class)
    @PostConstruct
    fun init() {
        logger.info(String.format("Message sender start with [ url: %s]", endPointUrl));

        connectionManager = createConnectionManager(endPointUrl, maxNoOfConnections.toInt(),
                defaultMaxNoOfConnectionsPerRoute.toInt())
        client = createClient(soTimeOutInMillis.toInt(), connectionRequestTimeoutInMilliSec.toInt(),
                connectTimeoutInMilliSec.toInt(), connectionManager)

        configureIdleConnectionChecker()
    }

    @Throws(MalformedURLException::class)
    private fun createConnectionManager(urlStr: String?, maxNoOfConnections: Int,
                                        defaultMaxNoOfConnectionsPerRoute: Int): HttpClientConnectionManager {

        val connectionManager = PoolingHttpClientConnectionManager()
        connectionManager.maxTotal = maxNoOfConnections
        connectionManager.defaultMaxPerRoute = defaultMaxNoOfConnectionsPerRoute

        val url = URL(urlStr!!)
        val localhost = HttpHost(url.host, url.port)
        connectionManager.setMaxPerRoute(HttpRoute(localhost), defaultMaxNoOfConnectionsPerRoute)

        return connectionManager
    }

    private fun createClient(soTimeOutInMillis: Int,
                             connectionRequestTimeoutInMilliSec: Int,
                             connectTimeoutInMilliSec: Int,
                             connectionManager: HttpClientConnectionManager?): CloseableHttpClient {

        val NoRetryHandler = HttpRequestRetryHandler { e, i, httpContext -> false }

        val socketConfig = SocketConfig.custom().setSoKeepAlive(true).setSoTimeout(soTimeOutInMillis).build()
        return HttpClients.custom()
                .setDefaultSocketConfig(socketConfig)
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                .setConnectionReuseStrategy(DefaultConnectionReuseStrategy())
                .setRetryHandler(NoRetryHandler)
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(connectionRequestTimeoutInMilliSec)
                        .setConnectTimeout(connectTimeoutInMilliSec)
                        .setSocketTimeout(soTimeOutInMillis)
                        .build())
                .build()
    }


    private fun configureIdleConnectionChecker() {
        if (enableIdleConnectionCheck) {
            idleConnectionRemovalWorker = Executors.newSingleThreadScheduledExecutor()
            idleConnectionRemovalWorker.scheduleAtFixedRate({
                try {
                    connectionManager.closeExpiredConnections()
                    connectionManager.closeIdleConnections(maxIdleTimeInMilliSec.toLong(),
                            TimeUnit.MILLISECONDS)
                } catch (e: Exception) {
                    logger.error(String.format("Error while checking idle Connection [ endPoint: %s]",
                            endPointUrl), e);
                }
            }, 100, idleConnectionCheckPeriodInMilliSec.toLong(), TimeUnit.MILLISECONDS)

            logger.info(String.format("Stale connection checker enabled [%s]", endPointUrl));
        }
    }

    @PreDestroy
    fun destroy() {
        try {
            client.close()
        } catch (e: IOException) {
            logger.error("Error while shutdown http client", e);
        }

        idleConnectionRemovalWorker.shutdownNow()

        logger.info("Http client stopped");
    }


    @Throws(Exception::class)
    fun sendRequest(request: HttpEntityEnclosingRequestBase): String? {
        logger.debug("send request");
        println("send request")

        var httpResponse: CloseableHttpResponse? = null
        try {
            httpResponse = client.execute(request)
            println(httpResponse)
            if (httpResponse != null) {
                val entity = httpResponse.entity
                println(entity)
                if (entity != null) {
                    val response = EntityUtils.toString(entity);
                    println("response"+ response)
                    logger.debug(String.format("Received sms-guardianAction response [%s]", response));
                    return response
                } else {
                    return null
                }
            } else {
                return null
            }
        } catch (e: ConnectException) {
                return null
        } finally {
            httpResponse?.close()

        }
    }

    companion object {

        val CONTENT_TYPE_APPLICATION_JSON = "application/json"

        val CONTENT_TYPE = "Content-Type"
    }
}
