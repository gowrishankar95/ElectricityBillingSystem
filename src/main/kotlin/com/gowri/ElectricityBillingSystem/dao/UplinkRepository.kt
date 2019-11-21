package com.gowri.ElectricityBillingSystem.dao

import com.gowri.ElectricityBillingSystem.UplinkDataToDatabase

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
    abstract fun getUplinkData(devUI: String): String?

}
