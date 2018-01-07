package com.microb.auth

import com.microb.auth.jersey.api.AccountApi
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.EnableLoadTimeWeaving
import org.springframework.context.annotation.aspectj.EnableSpringConfigured


fun main(args: Array<String>) {
    runApplication<AuthenticationServiceApplication>(*args)
}



@EnableAutoConfiguration
@SpringBootApplication(
        scanBasePackageClasses = arrayOf(AccountApi::class)
)
@ComponentScan
//@EnableLoadTimeWeaving(aspectjWeaving= EnableLoadTimeWeaving.AspectJWeaving.ENABLED)
@EnableSpringConfigured
//@EnableAspectJAutoProxy
class AuthenticationServiceApplication