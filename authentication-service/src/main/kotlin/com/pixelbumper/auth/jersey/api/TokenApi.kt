package com.pixelbumper.auth.jersey.api

import com.pixelbumper.auth.services.EXPIRATION_TIME
import com.pixelbumper.auth.services.JWTService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "TokenApi", description = "can be used to create access tokens")
class TokenApi(
        private val JWTService: JWTService) {


    @Path(TOKEN_CREATION)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Operation(summary = "Creates a JWT token and returns it in a cookie")
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
                        true,
                        true)
                ).build()
    }


}
