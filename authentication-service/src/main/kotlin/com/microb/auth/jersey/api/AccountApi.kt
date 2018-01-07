package com.microb.auth.jersey.api

import com.microb.auth.model.entities.Account
import com.microb.auth.model.entities.Credential
import com.microb.auth.model.entities.CredentialPassword
import com.microb.auth.model.entities.CredentialVendorId
import com.microb.auth.model.repositories.AccountRepository
import com.microb.auth.model.repositories.CredentialRepository
import com.microb.auth.jersey.dtos.AccountDTO
import com.microb.auth.jersey.dtos.assembleDto
import com.microb.auth.security.PasswordService
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.transaction.Transactional
import javax.validation.constraints.NotNull
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Component
@OpenAPIDefinition(info = Info(title="wapow"))
class AccountApi @Autowired constructor(
        val accountRepository: AccountRepository,
        val credentialRepository: CredentialRepository,
        val passwordService: PasswordService) {

    @GET
    @Transactional
    fun getAccounts(): List<AccountDTO> {
//        accountRepository.findAll().forEach { System.err.println(it.credentials[0].type) }
        return accountRepository.findAll().map { it.assembleDto() }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    fun createAccount(
            @NotNull
            //            @Valid
            accountDTO: AccountDTO?
    ): AccountDTO? {
        val account = Account(
                displayName = "")

//        PasswordService.hash("")
        val credential: Credential = CredentialPassword(
                account = account,
                email = accountDTO!!.email!!,
                password = passwordService.hash(accountDTO!!.password!!))

        account.credentials.add(credential)

        accountRepository.save(account)


        return account.assembleDto()
    }

    @POST
    @Path("create-account-for-ios-device")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    fun createAccountForIOSDevice(
            @NotNull
            //            @Valid
            @FormParam("vendorId")
            vendorId: String
    ): AccountDTO? {
        val account = Account(displayName = "")

        val vendorIdCredential = CredentialVendorId(
                account = account,
                vendorId = vendorId)

        account.credentials.add(vendorIdCredential)
        accountRepository.save(account)

        return account.assembleDto()
    }


    @GET
    @Path("self")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun getAccountInfo(
            account: Account

    ): AccountDTO {
        return account.assembleDto()
    }


//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Transactional
//    fun createAccount(
//            @NotNull
//            @Valid
//            account: AccountDTO?
//    ): Response {
//        val account = Account(username = account!!.username!!)
//        accountRepository.save(account)
//
//        return Response.ok(assembleDto(account)).build()
//    }

}