package com.pixelbumper.auth.jersey.mappers


import com.pixelbumper.auth.AuthenticationServiceApplication.Companion.LOG
import com.pixelbumper.auth.jersey.dtos.ErrorMessageDTO
import javax.ws.rs.ClientErrorException
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class WebApplicationExceptionMapper : ExceptionMapper<WebApplicationException> {


    override fun toResponse(exception: WebApplicationException): Response {

        if (exception is ClientErrorException) {
            LOG.info(exception.message, exception)
        } else {
            LOG.error(exception.message, exception)
        }

        val message: String = if (exception.message != null) {
            exception.message!!
        } else {
            ""
        }
        val status = exception.response.status

        return Response
                .status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(ErrorMessageDTO(message = message, code = status))
                .build()
    }
}
