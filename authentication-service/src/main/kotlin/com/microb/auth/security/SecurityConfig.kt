package com.microb.auth.security

import com.microb.auth.jersey.JERSEY_BASE_PATH
import com.microb.auth.jersey.api.CREATE_TOKEN_PATH
import com.microb.auth.jersey.api.FULL_ACCOUNT_CREATION_PATH_FOR_IOS_DEVICES
import com.microb.auth.jersey.api.FULL_EMAIL_ACCOUNT_CREATION_PATH
import com.microb.auth.services.JWTService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository


@Configuration
@EnableWebSecurity
class SecurityConfig(
        private val jwtService: JWTService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
                .csrf()
                // ensure that the CSFR cookies are accessible by JS
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringAntMatchers("/h2-console/**")
                .and()
                .headers().frameOptions().disable()
                .and()
                // ensure that Spring Security will never create an HttpSession and it will never use it to obtain the SecurityContext
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/*.html",
                        "/*.js",
                        "/*.css",
                        "/*.png",
                        "/h2-console/**",
                        "$JERSEY_BASE_PATH/openapi.json",
                        "$JERSEY_BASE_PATH/$FULL_EMAIL_ACCOUNT_CREATION_PATH",
                        "$JERSEY_BASE_PATH/$FULL_ACCOUNT_CREATION_PATH_FOR_IOS_DEVICES",
                        "$JERSEY_BASE_PATH/$CREATE_TOKEN_PATH")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(JWTAuthenticationFilter(jwtService), BasicAuthenticationFilter::class.java)

    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(JWTAuthenticationProvider())
    }

    companion object {
        @Bean
        @JvmStatic
        fun passwordEncoder(): NoOpPasswordEncoder {
            return NoOpPasswordEncoder.getInstance() as NoOpPasswordEncoder
        }
    }
}
