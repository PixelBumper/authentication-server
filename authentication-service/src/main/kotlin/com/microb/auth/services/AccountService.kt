package com.microb.auth.services

import com.microb.auth.AuthenticationServiceApplication.Companion.AUTHENTICATION_SERVER
import com.microb.auth.model.entities.Account
import com.microb.auth.model.entities.CredentialIOSDevice
import com.microb.auth.model.repositories.AccountRepository
import com.microb.auth.model.repositories.CredentialRepository
import com.microb.auth.security.PasswordService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import java.util.logging.Level
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response.Status.CONFLICT


@Service
class AccountService(
        val accountRepository: AccountRepository,
        val credentialRepository: CredentialRepository,
        val passwordService: PasswordService,
        val transactionTemplate: TransactionTemplate
) {


    fun createAccountForIOSDevice(vendorId: String, deviceName: String): Account {
        try {
            AUTHENTICATION_SERVER.log(Level.WARNING, "wapow")
            return transactionTemplate.execute {

                val account = Account()

                val vendorIdCredential = CredentialIOSDevice(
                        account = account,
                        vendorId = vendorId,
                        deviceName = deviceName)

                account.credentials.add(vendorIdCredential)

                accountRepository.save(account)

                account
            }!!

        } catch (e: DataIntegrityViolationException) {
            // TODO check if the vendorIdConstraint was violated
            throw WebApplicationException("there is already an account for this device id", CONFLICT)
        }

    }



}