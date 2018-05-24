package com.microb.auth.security

import com.microb.auth.services.JWTService
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder


/**
 * This filter is responsible for getting the JWT token from the request cookie if present.
 * If the JWT token is valid it will be set as authentication in the SecurityContext.
 */
class JWTAuthenticationFilter(
        private val jwtService: JWTService
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain) {


        val jwtCookieList = request.cookies
                ?.filter { it.name == "jwt"}
                ?: emptyList()

        if (jwtCookieList.size == 1) {
            val jws = jwtService.getTokenIfValid(jwtCookieList[0].value)
            if (jws != null) {
                SecurityContextHolder.getContext().authentication = JWTSpringAuthenticationToken.createJWTSpringAuthenticationToken(jws)
            }
        }
        filterChain.doFilter(request, response)
    }

    companion object {
        val SECRET = "SecretKeyToGenJWTs"
        val EXPIRATION_TIME: Long = 864000000 // 10 days
    }
}
