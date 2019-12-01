package com.gowri.ElectricityBillingSystem

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.sql.SQLException

class DayByDayReadingMapper : RowMapper<dayByDayReadingResponse> {

    @Throws(SQLException::class)
    override fun mapRow(resultSet: ResultSet, i: Int): dayByDayReadingResponse? {
        try {

            val dayByDayReading = dayByDayReadingResponse(resultSet.getDate("date"),
                    resultSet.getInt("unitsConsumed")
            )


            return dayByDayReading

        } catch (e: EmptyResultDataAccessException) {
            return null
        }


    }
}
