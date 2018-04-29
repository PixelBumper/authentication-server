package com.microb.auth.security

import com.microb.auth.JERSEY_BASE_PATH
import com.microb.auth.jersey.api.FULL_ACCOUNT_CREATION_PATH_FOR_IOS_DEVICES
import com.microb.auth.jersey.api.FULL_EMAIL_ACCOUNT_CREATION_PATH
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder


@Configuration
@EnableWebSecurity
class SecurityConfig(
        val anonymousIOSDeviceAuthenticationProvider: AnonymousIOSDeviceAuthenticationProvider
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "$JERSEY_BASE_PATH/openapi.json",
                        "$JERSEY_BASE_PATH/$FULL_EMAIL_ACCOUNT_CREATION_PATH",
                        "$JERSEY_BASE_PATH/$FULL_ACCOUNT_CREATION_PATH_FOR_IOS_DEVICES").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()


    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth
                .authenticationProvider(anonymousIOSDeviceAuthenticationProvider)
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")

    }

    companion object {
        @Bean
        @JvmStatic
        fun passwordEncoder(): NoOpPasswordEncoder {
            return NoOpPasswordEncoder.getInstance() as NoOpPasswordEncoder
        }
    }
}