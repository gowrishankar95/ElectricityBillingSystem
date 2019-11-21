package com.gowri.ElectricityBillingSystem.connector


import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.gowri.ElectricityBillingSystem.LoraServerLogin
import com.gowri.ElectricityBillingSystem.connector.HttpConnector.Companion.CONTENT_TYPE
import com.gowri.ElectricityBillingSystem.connector.HttpConnector.Companion.CONTENT_TYPE_APPLICATION_JSON
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

interface HttpSmsSenderConnector {
    fun sendMessage(): JsonObject?
}

@Service
class HttpSmsSenderConnectorImpl : HttpSmsSenderConnector {

    @Value("\${lora.endpointUri}")
    private lateinit var endpointUri: String



    @Autowired
    private lateinit var httpConnector: HttpConnector

    private val logger = LogManager.getLogger(HttpSmsSenderConnector::class.java)

    override fun sendMessage(): JsonObject? {
        try {
            val request = HttpPost(endpointUri)
            val jsonStr = Gson().toJson(LoraServerLogin("admin","admin"))
            request.entity = StringEntity(jsonStr)
            request.addHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
            request.addHeader("Accept", "*/*")

            val response = httpConnector.sendRequest(request)
            if(response == null){
                return null
            }

            logger.info("loggin request send")
            return JsonParser().parse(response).asJsonObject
        } catch (e: Exception) {
            logger.error("Unable to send login request due to exception  :", e)
            return null
        }
    }

//    private fun convert(smsMessage: SmsMessage): MtRequest {
//        val formater = SimpleDateFormat(mtRequestTimestampFormat)
//        val drRequest = requestDeliveryReport.toLowerCase() != "false"
//        return MtRequest(
//                username,
//                password,
//                iName,
//                action,
//                smsMessage.from,
//                smsMessage.to,
//                smsMessage.messageId,
//                drRequest,
//                formater.format(Date()),
//                smsMessage.message
//        )
//    }

}
