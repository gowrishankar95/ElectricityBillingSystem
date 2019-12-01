package com.gowri.ElectricityBillingSystem.dao.impl

import com.gowri.ElectricityBillingSystem.User
import com.gowri.ElectricityBillingSystem.UserMapper
import com.gowri.ElectricityBillingSystem.dao.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException


@Repository
public class UserRepositoryImpl: UserRepository {

    override fun findByEmail(email: String): User? {
        val sql = ("SELECT * "
                + "FROM User" + " where email = " + "\'" + email + "\'")
        val user = jdbcTemplate!!.query(sql, UserMapper())
        if (user == null || user.isEmpty()) {
            logger.warn("user with user email {} does not exist", email)
            return null
        } else {
            logger.warn("returning user")
            return user.get(0)
        }

    }

    var logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null

    override fun create(user: User): User {
        val sql = "INSERT INTO User (userName,email,password,accountNum,devEUI) VALUES (?, ?,?,?,?)"
        jdbcTemplate!!.update(object : PreparedStatementCreator {
            @Throws(SQLException::class)
            override fun createPreparedStatement(connection: Connection): PreparedStatement {
                val ps = connection.prepareStatement(sql)
                ps.setString(1, user.username)
                ps.setString(2, user.email)
                ps.setString(3, user.password)
                ps.setString(4, user.accountNum)
                ps.setString(5, user.devEUI)

                return ps
            }
        })

        logger.info("successfully created user")
        return user

    }

//    fun deleteUser(id: Int): User? {
//        val user = isIdExist(id)
//        if (user == null) {
//            logger.warn("user with user id {} does not exist", userId)
//            return null
//        } else {
//            logger.warn("returning user")
//            return user[0]
//        }
//
//    }
//
//        val sql = "SELECT UserId, userName, passowrd" + " FROM users"
//        return jdbcTemplate!!.query(sql, UserMapper())
//    }

    override fun getUser(userId: Int): User? {
        val sql = ("SELECT * "
                + "FROM User" + " where UserId = " + "\'" + userId + "\'")
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
        println("find by username")
        val sql = ("SELECT * "
                + "FROM User" + " where username = " + "\'" + username + "\'")
        val user = jdbcTemplate?.query(sql, UserMapper())
        println("user"+user.toString())
        if (user == null || user.isEmpty()) {
            logger.warn("user with user id {} does not exist", username)
            return null
        } else {
            logger.warn("returning user")
            return user.get(0)
        }

    }

    override fun findByAccountNum(accountNum: String): User? {
        val sql = ("SELECT * "
                + "FROM User" + " where accountNum = " + "\'" + accountNum + "\'")
        val user = jdbcTemplate!!.query(sql, UserMapper())
        if (user == null || user.isEmpty()) {
            logger.warn("user with user id {} does not exist", accountNum)
            return null
        } else {
            logger.warn("returning user")
            return user.get(0)
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
