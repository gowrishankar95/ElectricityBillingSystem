package com.gowri.ElectricityBillingSystem.service

import com.gowri.ElectricityBillingSystem.ApiException
import com.gowri.ElectricityBillingSystem.User
import com.gowri.ElectricityBillingSystem.dao.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.regex.Matcher
import java.util.regex.Pattern
import org.springframework.security.crypto.factory.PasswordEncoderFactories


//todo: java doc is needed
interface ValidationService {


    /**
     * Validate registration request
     */
    fun validateRegistrationRequest(request: User)


    /**
     * Validate login Api
     */
    fun validateLogin(username: String, passowrd: String)

}


@Suppress("DEPRECATION")
@Service
class ValidationServiceImpl : ValidationService {

    @Autowired
    lateinit var userRepository: UserRepository



    @Autowired
    lateinit var apiService: ApiService

    lateinit var pattern: Pattern

    lateinit var matcher: Matcher




    private val logger: Logger = LoggerFactory.getLogger(ValidationServiceImpl::class.java)



    override fun validateLogin(email: String, passowrd: String) {
        val user = userRepository.findByEmail(email)

        if (user == null) {
            throw ApiException("1024", "Invalid Email",
                    "user does not exist")
        }

        if (user != null) {
            validateLoginPassword(user, passowrd)
        }
    }

    private fun validateLoginPassword(user: User, password: String) {
        if (password.isEmpty() || !PasswordEncoderFactories.createDelegatingPasswordEncoder()
                        .matches(password, user.password))

            throw ApiException("1025", "Invalid Password",
                    "Error occurred while validating logged in user's password")

    }


    override fun validateRegistrationRequest(request: User) {

        val isValidPassword = validatePasswordPattern(request.password)
        if (! isValidPassword || request.password.isEmpty()) {
            throw ApiException("101", "INVALID PASSOWORD",
                    "Error occurred while validating  [ password : $request.password ]")
        }

        validateUsername(request.username)

        val user = userRepository.findByUsername(request.username)
        if (user != null) {
            throw ApiException("103", "INVALID_USERNAME",
                    "Error occurred while validating  [ username : $request.username ]")
        }


        validateEmail(request.email)
        validateAccountNum(request.accountNum)

    }

    fun validateUsername(username: String) {
        if (username.isEmpty()) {
            throw ApiException("102", "INVALID_USERNAME",
                    "Error occurred while validating  [ username : $username ]")
        }
    }

    private fun validateAccountNum(accountNum: String) {
        val user = userRepository.findByAccountNum(accountNum)
        if (user!=null) {
            throw ApiException("1050", "account number already exists",
                    "Error occurred while validating  [ account number : $accountNum]")
        }
    }





    private  fun validatePasswordPattern(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$"
        pattern = Pattern.compile(passwordPattern)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    private fun validateEmail(email: String) {
        val user = userRepository.findByEmail(email)
        if (user != null) {
            throw ApiException("1023", "email already exists",
                    "Error occurred while validating  [ email : $email ]")
        }

    }

    private fun validateEmailPattern(email: String) : Matcher{
        val emailPattern = "^[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"
        pattern = Pattern.compile(emailPattern)
        matcher = pattern.matcher(email)
        return matcher
    }



}
