package com.microb.auth.jersey.filters

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.WebRequest
import java.time.Instant
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.ext.Provider

val REQUEST_TIME = "REQUEST_TIME"

@Provider
class TimeFilter : ContainerRequestFilter {


    override fun filter(requestContext: ContainerRequestContext) {

        val now = Instant.now()

        RequestContextHolder.currentRequestAttributes().setAttribute(REQUEST_TIME, now, WebRequest.SCOPE_REQUEST)
        if (requestContext.getProperty(REQUEST_TIME) != RequestContextHolder.currentRequestAttributes().getAttribute(REQUEST_TIME, WebRequest.SCOPE_REQUEST)) {
            throw RuntimeException("request time initialization failed")
        }
    }
}
