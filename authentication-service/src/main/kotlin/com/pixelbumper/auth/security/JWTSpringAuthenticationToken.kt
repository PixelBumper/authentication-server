package com.pixelbumper.auth.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JWTSpringAuthenticationToken
private constructor(private val jws: Jws<Claims>, authorities: Collection<GrantedAuthority>?) : AbstractAuthenticationToken(authorities) {


    companion object {
        fun createJWTSpringAuthenticationToken(jws: Jws<Claims>): JWTSpringAuthenticationToken {
            return JWTSpringAuthenticationToken(jws, emptyList())
        }
    }

    override fun getCredentials(): Any {
        return jws
    }

    override fun getPrincipal(): Any {
        return jws.body.subject
    }
}
