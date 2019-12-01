package com.gowri.ElectricityBillingSystem.dao

import com.gowri.ElectricityBillingSystem.UplinkDataToDatabase
import com.gowri.ElectricityBillingSystem.dayByDayReading
import com.gowri.ElectricityBillingSystem.dayReadingResponse
import javax.persistence.criteria.CriteriaBuilder

interface DayByDayRepository {
    abstract fun insertDayByDayReading(day: dayByDayReading): dayByDayReading

    abstract fun getDayByDayReadingForMoonth(devEUI: String, month: Int): MutableList<dayReadingResponse>?
}
