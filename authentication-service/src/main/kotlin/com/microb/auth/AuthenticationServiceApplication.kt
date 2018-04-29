package com.microb.auth

import com.sun.tools.attach.VirtualMachine
import org.aspectj.weaver.loadtime.Agent
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.AdviceMode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.EnableLoadTimeWeaving
import org.springframework.context.annotation.aspectj.EnableSpringConfigured
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.io.File
import java.lang.management.ManagementFactory


fun isAspectJAgentLoaded(): Boolean {
    try {
        Agent.getInstrumentation()
    } catch (e: NoClassDefFoundError) {
        println(e)
        return false
    } catch (e: UnsupportedOperationException) {
        println(e)
        return dynamicallyLoadAspectJAgent()
    }

    return true
}

fun dynamicallyLoadAspectJAgent(): Boolean {
    val nameOfRunningVM = ManagementFactory.getRuntimeMXBean().name
    val p = nameOfRunningVM.indexOf('@')
    val pid = nameOfRunningVM.substring(0, p)
    try {
        val vm = VirtualMachine.attach(pid)

        val jarFilePath = System.getProperty("java.class.path")
                .split(File.pathSeparator).stream()
                .filter { it.contains("aspectjweaver") }
                .findAny()
                .get()

        vm.loadAgent(jarFilePath)
        vm.detach()
    } catch (e: Exception) {
        println(e)
        return false
    }

    return true
}

fun main(args: Array<String>) {
    if (!isAspectJAgentLoaded()) {
        throw RuntimeException()
    }
    runApplication<AuthenticationServiceApplication>(*args)
}

@EnableAutoConfiguration
@SpringBootApplication
@EnableSpringConfigured
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableLoadTimeWeaving(aspectjWeaving = EnableLoadTimeWeaving.AspectJWeaving.ENABLED)
class AuthenticationServiceApplication {

    companion object {
        val LOG = LoggerFactory.getLogger("AuthenticationService")!!
    }

    /**
     * this ensures spring uses the right load time weaver
     */
    @Bean
    @Throws(Throwable::class)
    fun loadTimeWeaver(): InstrumentationLoadTimeWeaver {
        return InstrumentationLoadTimeWeaver()
    }
}