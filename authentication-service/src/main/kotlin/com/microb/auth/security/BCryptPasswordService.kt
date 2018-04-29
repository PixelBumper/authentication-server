package com.microb.auth.security

//import org.apache.shiro.authc.credential.PasswordService


class BCryptPasswordService
//    : PasswordService {
//    override fun passwordsMatch(submittedPlaintext: Any?, encrypted: String?): Boolean {
//        return BCrypt.checkpw(String(submittedPlaintext as CharArray), encrypted)
//    }
//
//    override fun encryptPassword(plaintextPassword: Any): String {
//        val str: String
//        if (plaintextPassword is CharArray) {
//            str = String(plaintextPassword)
//        } else if (plaintextPassword is String) {
//            str = plaintextPassword
//        } else {
//            throw SecurityException("Unsupported password type: " + plaintextPassword.javaClass.name)
//        }
//        return BCrypt.hashpw(str, BCrypt.gensalt())
//    }
//}