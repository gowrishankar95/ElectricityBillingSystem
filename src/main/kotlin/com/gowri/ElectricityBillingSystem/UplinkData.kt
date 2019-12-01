package com.gowri.ElectricityBillingSystem

import java.sql.Timestamp
import java.util.*

data class RecievedData(
        val applicationID: String,
        val applicationName: String,
        val devEUI: String,
//        val rxInfo: String,
//        val txInfo: String,
        val adr: String,
        val fCnt: String,
        val fPort: String,
        val data:String
)

data class ReadingObject(
        val reading: String
)


data class UplinkDataToDatabase(
        val devEUI: String,
        //val uploadedTime: Timestamp,
        val data:String
)

data class getUplinkDataFromDatabase(
        //val devEUI: String,
        val uploadedTime: Timestamp,
        var data:String
)

data class getUplinkDataFromDatabaseInteger(
        //val devEUI: String,
        val x: Date,
        var y: Int
)

data class dayByDayReading(
        val devEUI: String,
        val date: Date,
        var unitsUsed: Int
)

data class dayByDayReadingResponse(
        val date: Date,
        var unitsUsed: Int
)

data class dayReadingResponse(
        val content: MutableList<getUplinkDataFromDatabaseInteger>?,
        val statusCode: String

)



data class LoraServerLogin(
        val password: String,
        val username:String
)

data class AdminLogin(
        val password: String,
        val user:String
)

data class User(
        var id: Int?,
        val username: String,
        val email: String,
        val password: String,
        val accountNum: String,
        val devEUI: String
)

data class getNumberOfUnitsConsumedForCurrentMonth(
        val devEUI: String
)

data class devEUI(
        val devEUI: String
)

data class LoginResponse(
    val id: String,
    val success: Boolean
)
