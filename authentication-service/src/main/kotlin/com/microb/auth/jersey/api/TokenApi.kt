package com.microb.auth.jersey.api

import com.microb.auth.security.JWTAuthenticationFilter.Companion.EXPIRATION_TIME
import com.microb.auth.services.JWTService
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.stereotype.Component
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.NewCookie
import javax.ws.rs.core.Response

const val TOKENS_RESOURCE_BASE = "/tokens"
const val TOKEN_CREATION = "/create-token"
const val CREATE_TOKEN_PATH = "$TOKENS_RESOURCE_BASE$TOKEN_CREATION"

@Path(TOKENS_RESOURCE_BASE)
@Produces(MediaType.APPLICATION_JSON)
@Component
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
class TokenApi(
        private val JWTService: JWTService) {


    @Path(TOKEN_CREATION)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    fun createToken(
            @NotBlank(message = "")
            @FormParam("username")
            username: String,

            @NotNull
            @FormParam("password")
            password: String

    ): Response {

        val token = JWTService.createToken(username, password)

        return Response
                .ok()
                .cookie(NewCookie(
                        "jwt",
                        token,
                        "/",
                        "0.0.0.0",
                        "JWT Token",
                        (EXPIRATION_TIME / 1000).toInt(),
                        false,
                        true)
                ).build()
    }


}
