package com.gowri.ElectricityBillingSystem.dao

import com.gowri.ElectricityBillingSystem.User

interface UserRepository {

    /**
     * This is the method to be used to create
     * user.
     */
    fun create(user: User): User

    fun getUser(userId: Int): User?

    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User?

    fun findByAccountNum(accountNum: String): User?


}
