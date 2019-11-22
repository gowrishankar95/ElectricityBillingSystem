package com.gowri.ElectricityBillingSystem

import java.sql.Timestamp

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
data class UplinkDataToDatabase(
        val devEUI: String,
        //val uploadedTime: Timestamp,
        val data:String
)

data class getUplinkDataFromDatabase(
        val devEUI: String,
        val uploadedTime: Timestamp,
        val data:String
)

data class LoraServerLogin(
        val password: String,
        val username:String
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
