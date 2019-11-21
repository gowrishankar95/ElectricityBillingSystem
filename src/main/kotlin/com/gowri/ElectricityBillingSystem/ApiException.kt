/*
 * (C) Copyright 2008-2019 hSenid Software International (Pvt) Limited.
 *
 * All Rights Reserved.
 *
 * These materials are unpublished, proprietary, confidential source code of
 * hSenid Software International (Pvt) Limited and constitute a TRADE SECRET
 * of hSenid Software International (Pvt) Limited.
 *
 * hSenid Software International (Pvt) Limited retains all title to and intellectual
 * property rights in these materials.
 */

package com.gowri.ElectricityBillingSystem

class ApiException(val errorCode: String, val errorMessage: String, message: String, cause: Exception?)
    : BaseException(message, cause) {

    constructor(errorCode: String, errorMessage: String, message: String): this(errorCode, errorMessage, message, null)

    override fun customAttributes(): MutableMap<String, Any> {
        val customAttributes = LinkedHashMap<String, Any>()
        customAttributes["errorCode"] = errorCode
        customAttributes["errorMessage"] = errorMessage
        return customAttributes
    }
}
