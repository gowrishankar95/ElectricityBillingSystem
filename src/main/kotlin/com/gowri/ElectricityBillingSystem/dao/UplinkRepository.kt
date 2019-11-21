package com.gowri.ElectricityBillingSystem.dao

import com.gowri.ElectricityBillingSystem.UplinkDataToDatabase
import java.sql.Timestamp

interface UplinkRepository {
    /**
     * This is the method to be used to save
     * data from loraserver to the database.
     */
    abstract fun insertUplink(upLink: UplinkDataToDatabase): UplinkDataToDatabase

    /**
     * This is the method to be used to get the
     * last uploaded data to the databse. should provide devUI,
     */
    abstract fun getLastUplinkData(devUI: String): String?

    abstract fun getFirstUplinkDataAfterGivenTimeStamp(devUI: String,timstamp: Timestamp): String?

}
