package com.microb.auth.jersey.api

import com.microb.auth.jersey.dtos.AccountDTO
import com.microb.auth.jersey.dtos.assembleDto
import com.microb.auth.model.entities.Account
import com.microb.auth.model.repositories.AccountRepository
import com.microb.auth.services.AccountService
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import java.security.Principal
import javax.transaction.Transactional
import javax.validation.constraints.*
import javax.ws.rs.*
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

const val ACCOUNT_RESOURCE_BASE_PATH = "/accounts"
const val EMAIL_ACCOUNT_CREATION_PATH = "/create-account-for-email-and-password"
const val FULL_EMAIL_ACCOUNT_CREATION_PATH = "$ACCOUNT_RESOURCE_BASE_PATH$EMAIL_ACCOUNT_CREATION_PATH"
const val ACCOUNT_CREATION_PATH_FOR_IOS_DEVICES = "/create-account-for-ios-device"
const val FULL_ACCOUNT_CREATION_PATH_FOR_IOS_DEVICES = "$ACCOUNT_RESOURCE_BASE_PATH${ACCOUNT_CREATION_PATH_FOR_IOS_DEVICES}"

@Path(ACCOUNT_RESOURCE_BASE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Component
@OpenAPIDefinition(info = Info(title = "wapow"))
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
class AccountApi @Autowired constructor(
        val accountRepository: AccountRepository,
        val accountService: AccountService) {

    @GET
    @Transactional
    @SecurityRequirement(name = "basicAuth")
    @PreAuthorize("hasRole('USER')")
    fun getAccounts(
            @Parameter(hidden = true)
            principal: Principal?,
            @Context
            securityContext: SecurityContext?,
            @Context
            containerRequestContext: ContainerRequestContext?
    ): List<AccountDTO> {
        return accountRepository.findAll().map { it.assembleDto() }

    }

    @POST
    @Path(EMAIL_ACCOUNT_CREATION_PATH)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    fun createEmailAccount(
            @Parameter(schema = Schema(
                    type = "string",
                    format = "email"))
            @NotBlank(message = "you need to provide a valid email address")
            @Email
            @FormParam("email")
            @Max(254, message = "the email address was too long")
            email: String,

            @Parameter(schema = Schema(
                    type = "string",
                    format = "password"))
            @NotBlank(message = "you need to provide a password")
            @Min(8, message = "the password was too short")
            @Max(256, message = "the password was too long")
            @FormParam("password")
            password: String
    ): AccountDTO? {

        return accountService
                .createAccountForEmail(email, password)
                .assembleDto()
    }

    @POST
    @Path(ACCOUNT_CREATION_PATH_FOR_IOS_DEVICES)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiResponse(responseCode = "200")
    fun createAccountForIOSDevice(
            @NotBlank(message = "you need to provide a vendor id")
            @FormParam("vendorId")
            vendorId: String,

            @NotBlank(message = "you need to provide the name of the device")
            @FormParam("deviceName")
            deviceName: String
    ): AccountDTO? {

        return accountService
                .createAccountForIOSDevice(vendorId, deviceName)
                .assembleDto()

    }


    @GET
    @Path("self")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun getAccountInfo(
            @Context
            securityContext: SecurityContext
    ): AccountDTO {
        return accountService
                .getAccountOfSecurityContext(securityContext)
                .assembleDto()
    }



}
