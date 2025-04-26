package com.near_reality.api.util

import com.near_reality.api.ApiConstants
import com.near_reality.api.requests.UserLoginRequest


suspend fun UserLoginRequest.decryptPassword() =
    AES.decrypt(password, ApiConstants.AES_SECRET)
