package com.microb.auth

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.aspectj.EnableSpringConfigured


fun main(args: Array<String>) {
    runApplication<AuthenticationServiceApplication>(*args)
}

@EnableAutoConfiguration
@SpringBootApplication
@EnableSpringConfigured
class AuthenticationServiceApplication