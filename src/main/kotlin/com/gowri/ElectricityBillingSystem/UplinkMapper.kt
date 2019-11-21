package com.gowri.ElectricityBillingSystem

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.sql.SQLException

class UplinkMapper : RowMapper<UplinkDataToDatabase> {

    @Throws(SQLException::class)
    override fun mapRow(resultSet: ResultSet, i: Int):UplinkDataToDatabase?{
        try {

            val uplinkData = UplinkDataToDatabase(resultSet.getString("devEUI"),resultSet.getTimestamp(""),resultSet.getString("data"));

            return uplinkData

        } catch (e: EmptyResultDataAccessException) {
            return null
        }


    }
}
