package com.microb.auth.security

import com.lambdaworks.crypto.SCryptUtil
import org.springframework.stereotype.Service


@Service
class PasswordService {
    fun verify(password: String, hash: String) = SCryptUtil.check(password, hash)

    fun hash(password: String) = SCryptUtil.scrypt(password, 16384, 8, 1)
}