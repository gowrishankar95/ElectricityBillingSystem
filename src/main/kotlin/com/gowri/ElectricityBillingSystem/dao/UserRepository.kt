package com.gowri.ElectricityBillingSystem.dao

import com.gowri.ElectricityBillingSystem.User

interface UserRepository {

    /**
     * This is the method to be used to create
     * user.
     */
    fun create(user: User): User

    /**
     * This is the method to be used to update
     * password of a user. should provide userId,
     * old password and new password
     */
    fun updatePassword(UserId: Int, oldPassword: String, newPassword: String): String

    /**
     * This is the method to be used to delete
     * user. should provide userID
     */
    fun deleteUser(id: Int): User

    /**
     * This is the method to be used to view
     * all users.
     */
    fun viewAllUsers(): List<User>

    /**
     * This is the method to be used to get
     * a user with perticular userId
     */
    fun getUser(userId: Int): User

    fun findByUsername(username: String): User?


}
