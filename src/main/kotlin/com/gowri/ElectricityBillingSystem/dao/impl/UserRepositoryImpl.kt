package com.gowri.ElectricityBillingSystem.dao.impl

import com.gowri.ElectricityBillingSystem.User
import com.gowri.ElectricityBillingSystem.UserMapper
import com.gowri.ElectricityBillingSystem.dao.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

@Repository
public class UserRepositoryImpl: UserRepository {

    var logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null

    fun create(user: User): User {
        val sql = "INSERT INTO users (userName,email,password,invalidLoginAttempt,accountNum,sessionTime,lastLoginTimestamp) VALUES (?, ?,?,?,?,?,?)"
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate!!.update(object : PreparedStatementCreator {
            @Throws(SQLException::class)
            override fun createPreparedStatement(connection: Connection): PreparedStatement {
                val ps = connection.prepareStatement(sql)
                ps.setString(1, user.username)
                ps.setString(2, user.email)
                ps.setString(3, user.password)
                ps.setInt(4, user.invalidLoginAttempt)
                ps.setString(5, user.accountNum)
                ps.setTimestamp(6, user.sessionTime)
                ps.setTimestamp(7, user.lastLoginTimestamp)
                return ps
            }
        })

        logger.info("successfully created user")
        return user

    }

//    fun deleteUser(id: Int): User? {
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

    fun getUser(userId: Int): User? {
        val sql = ("SELECT * "
                + "FROM users" + " where UserId = " + "\'" + userId + "\'")
        val user = jdbcTemplate!!.query(sql, UserMapper())
        if (user == null) {
            logger.warn("user with user id {} does not exist", userId)
            return null
        } else {
            logger.warn("returning user")
            return user[0]
        }

    }

    override fun findByUsername(username: String): User? {
        val sql = ("SELECT * "
                + "FROM users" + " where userName = " + "\'" + username + "\'")
        val user = jdbcTemplate!!.query(sql, UserMapper())
        if (user == null) {
            logger.warn("user with user id {} does not exist", username)
            return null
        } else {
            logger.warn("returning user")
            return user[0]
        }

    }



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
//    }
}
