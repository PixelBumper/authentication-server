package com.microb.auth.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JWTSpringAuthenticationToken
    private constructor(val jws: Jws<Claims>, authorities: Collection<out GrantedAuthority>?) : AbstractAuthenticationToken(authorities) {


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