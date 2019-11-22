package com.gowri.ElectricityBillingSystem

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.sql.SQLException

class UserMapper : RowMapper<User> {

    @Throws(SQLException::class)
    override fun mapRow(resultSet: ResultSet, i: Int): User? {
        try {

            val user = User(resultSet.getInt("UserId"),
                    resultSet.getString("userName"),
                    resultSet.getString("email"),
                    resultSet.getString("passowrd"),
                    resultSet.getString("accountNum"),
                    resultSet.getString("devEUI"))


            return user

        } catch (e: EmptyResultDataAccessException) {
            return null
        }


    }
}
