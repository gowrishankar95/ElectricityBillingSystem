package com.gowri.ElectricityBillingSystem.dao.impl

import com.gowri.ElectricityBillingSystem.UplinkDataToDatabase
import com.gowri.ElectricityBillingSystem.UplinkMapper
import com.gowri.ElectricityBillingSystem.dao.UplinkRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Timestamp

@Repository
class UplinkRepositoryImpl : UplinkRepository {


    internal var logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null

    override fun insertUplink(upLink: UplinkDataToDatabase): UplinkDataToDatabase                            {
        val sql = "INSERT INTO uplink (devEUI,data) VALUES (?, ?)"
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate!!.update(object : PreparedStatementCreator {
            @Throws(SQLException::class)
            override fun createPreparedStatement(connection: Connection): PreparedStatement {
                val ps = connection.prepareStatement(sql)
                ps.setString(1, upLink.devEUI)
                ps.setString(2, upLink.data)
                return ps
            }
        })

        logger.info("successfully updated uplink data")
        return upLink

    }
    override fun getLastUplinkData(devUI: String): String? {
        val sql = ("SELECT * "
                + "FROM uplink" + " where devEUI = " + "\'" + devUI + "\'"+ "ORDER BY uploadedTime DESC LIMIT 1")
        val uplinkData = jdbcTemplate!!.query(sql, UplinkMapper())
        if (uplinkData == null) {
            logger.warn("devUI does not exist", devUI)
            return null
        } else {
            logger.warn("returning user")
            return uplinkData?.get(0).data
        }

    }

    override fun getFirstUplinkDataAfterGivenTimeStamp(devUI: String,timstamp:Timestamp): String? {
        val sql = ("SELECT * "
                + "FROM uplink" + " where devEUI = " + "\'" + devUI + "\'"+" AND uploadedTime >"+ "\'" + timstamp  +"ORDER BY uploadedTime ASC LIMIT 1")
        val uplinkData = jdbcTemplate!!.query(sql, UplinkMapper())
        if (uplinkData == null) {
            logger.warn("devUI does not exist", devUI)
            return null
        } else {
            logger.warn("returning user")
            return uplinkData?.get(0).data
        }

    }


//    }fun deleteUser(id: Int): User? {
//        val user = isIdExist(id)
//        if (user == null) {
//            logger.warn("User with id {} not found", id)
//            return null
//        } else {
//            val sql = "DELETE  FROM users  WHERE UserId=?"
//            jdbcTemplate!!.update(sql, id)
//            println("user id $id deleted")
//            logger.info("user succesfully deleted")
//            return user
//
//        }
//    }
//
//    fun viewAllUsers(): List<User> {
//
//        val sql = "SELECT UserId, userName, passowrd" + " FROM users"
//        return jdbcTemplate!!.query(sql, UserMapper())
//    }
//
//    fun getUser(userId: Int): User? {
//        val sql = ("SELECT * "
//                + "FROM users" + " where UserId = " + "\'" + userId + "\'")
//        val user = jdbcTemplate!!.query(sql, UserMapper())
//        if (user == null) {
//            logger.warn("user with user id {} does not exist", userId)
//            return null
//        } else {
//            logger.warn("returning user")
//            return user!!.get(0)
//        }
//
//    }
//
//    fun isIdExist(userId: Int): User? {
//        val sql = ("SELECT * "
//                + "FROM users" + " where UserId = " + "\'" + userId + "\'")
//        val user = jdbcTemplate!!.query(sql, UserMapper())
//        return if (user.isEmpty()) {
//            null
//        } else {
//            user.get(0)
//        }
//    }
//
//    fun updatePassword(id: Int, oldPassword: String, newPassword: String): String {
//        val user = isIdExist(id)
//        if (user == null) {
//            logger.warn("User with id {} not found", id)
//            return "User with id $id not found"
//        } else if (!user!!.getPassword().equals(oldPassword)) {
//            return ExceptionCodeDescription.INVALID_PASSWORD.getDescription()
//        } else {
//            val sql = "UPDATE users SET passowrd=? WHERE UserId=?"
//            jdbcTemplate!!.update(sql, newPassword, id)
//            logger.info("User Id {} password succesfully updated", id)
//            return "User Id $id password succesfully updated"
//
//        }
}
