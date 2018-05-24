package com.microb.auth.services

import com.microb.auth.model.repositories.CredentialIOSDeviceRepository
import com.microb.auth.model.repositories.CredentialPasswordRepository
import com.microb.auth.security.PasswordService
import io.jsonwebtoken.*
import org.springframework.stereotype.Service
import java.util.*
import javax.ws.rs.NotAuthorizedException

const val SECRET = "SecretKeyToGenJWTs"
const val EXPIRATION_TIME: Long = 864000000 // 10 days

@Service
class JWTService(
        private val credentialPasswordRepository: CredentialPasswordRepository,
        private val credentialIOSDeviceRepository: CredentialIOSDeviceRepository,
        private val passwordService: PasswordService) {

    fun createToken(username: String, password: String): String {
        val credential = when (username) {
            "deviceId" -> credentialIOSDeviceRepository.findByVendorId(password)
                    ?: throw NotAuthorizedException("no account for deviceId '$password'")
            else -> {
                val emailCredential = credentialPasswordRepository.findByEmail(username)
                        ?: throw NotAuthorizedException("no account for email '$username'")

                if (passwordService.verify(password, emailCredential.password) == false) {
                    throw NotAuthorizedException("wrong password for email '$username'")
                }
                emailCredential
            }
        }

        return Jwts.builder()
                .setSubject(credential.account.name)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.toByteArray())
                .compact()
    }

    fun getTokenIfValid(token: String): Jws<Claims>? {
        return try {
            Jwts.parser().setSigningKey(SECRET.toByteArray()).parseClaimsJws(token)
        } catch (e: Exception) {
            when (e) {
                is ExpiredJwtException,
                is UnsupportedJwtException,
                is MalformedJwtException,
                is SignatureException,
                is IllegalArgumentException -> null
                else -> throw e
            }
        }
    }
}
