package com.gowri.ElectricityBillingSystem

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.sql.SQLException

class UplinkMapper : RowMapper<getUplinkDataFromDatabase> {

    @Throws(SQLException::class)
    override fun mapRow(resultSet: ResultSet, i: Int):getUplinkDataFromDatabase?{
        try {

            val uplinkData = getUplinkDataFromDatabase(resultSet.getString("devEUI"),resultSet.getTimestamp(""),resultSet.getString("data"));

            return uplinkData

        } catch (e: EmptyResultDataAccessException) {
            return null
        }


    }
}
