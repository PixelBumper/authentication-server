package com.microb.auth.jersey.mappers

import com.microb.auth.AuthenticationServiceApplication.Companion.LOG
import com.microb.auth.jersey.dtos.ErrorMessageDTO
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
@Component
class AllExceptionMapper : ExceptionMapper<Throwable> {

    override fun toResponse(exception: Throwable): Response {

        LOG.error(exception.message, exception)

        return Response
                .serverError()
                .entity(ErrorMessageDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error"))
                .build()
    }
}