package com.microb.auth.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

/**
 * This authentication provider is responsible for processing [JWTSpringAuthenticationToken]
 *
 * Usually this is where entities are retrieved from the database when needed
 */
class JWTAuthenticationProvider: AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        return authentication
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return JWTSpringAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}
