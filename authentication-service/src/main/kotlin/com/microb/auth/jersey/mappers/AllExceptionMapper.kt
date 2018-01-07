package com.microb.auth.jersey.mappers

import org.springframework.stereotype.Component
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
@Component
class AllExceptionMapper:ExceptionMapper<Throwable>{
    override fun toResponse(exception: Throwable): Response {
        exception.printStackTrace()
        return Response.serverError().build()
    }
}