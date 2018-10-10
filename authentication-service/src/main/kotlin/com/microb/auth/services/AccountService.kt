package com.microb.auth.services

import com.microb.auth.model.entities.*
import com.microb.auth.model.repositories.AccountRepository
import com.microb.auth.model.repositories.CredentialIOSDeviceRepository
import com.microb.auth.model.repositories.CredentialPasswordRepository
import com.microb.auth.security.PasswordService
import com.microb.auth.services.exceptions.ConflictException
import com.microb.auth.services.exceptions.NotAuthorizedException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.time.Instant
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.SecurityContext

const val EMAIL_IS_ALREADY_LINKED_TO_ANOTHER_ACCOUNT = "there is already an account associated with this email"
const val DEVICE_ID_ALREADY_LINKED_TO_ANOTHER_ACCOUNT = "there is already an account associated with this device id"

const val THERE_WAS_NO_PRINCIPAL_SET_IN_THE_SECURITY_CONTEXT = "there was no principal set in the security context"
const val THE_PRINCIPAL_DID_NOT_HAVE_A_NON_BLANK_NAME = "the principal did not have a non blank name"

const val THERE_WAS_NO_ACCOUNT_FOR_ACCOUNT_ID = "there was no account for accountId "

@Service
class AccountService(
        private val accountRepository: AccountRepository,
        private val emailCredentialRepository: CredentialPasswordRepository,
        private val credentialIOSDeviceRepository: CredentialIOSDeviceRepository,
        private val passwordService: PasswordService,
        private val transactionTemplate: TransactionTemplate
) {


    /**
     * @throws ConflictException when the email is already linked to another account
     */
    fun createAccountForEmail(email: String, password: String): Account {
        try {
            return createAccountForEmailInternal(email, password)
        } catch (e: DataIntegrityViolationException) {
            // TODO check if the emailConstraint was violated
            throw ConflictException(EMAIL_IS_ALREADY_LINKED_TO_ANOTHER_ACCOUNT, e)
        }

    }

    @Transactional
    private fun createAccountForEmailInternal(email: String, password: String): Account {
        emailCredentialRepository.findByEmail(email)?.let { throw ConflictException(EMAIL_IS_ALREADY_LINKED_TO_ANOTHER_ACCOUNT) }

        val now = Instant.now()
        val account = Account(now)

        val credential: Credential = CredentialPassword(
                account = account,
                email = email,
                password = passwordService.hash(password),
                now = now)

        account.credentials.add(credential)

        return accountRepository.save(account)
    }

    /**
     * @throws ConflictException when the vendorId is already linked to another account
     */
    fun createAccountForIOSDevice(vendorId: String, deviceName: String): Account {
        try {
            return createAccountForIOSDeviceInternal(vendorId, deviceName)
        } catch (e: DataIntegrityViolationException) {
            // TODO check if the vendorIdConstraint was violated
            throw ConflictException(DEVICE_ID_ALREADY_LINKED_TO_ANOTHER_ACCOUNT, e)
        }

    }

    @Transactional
    private fun createAccountForIOSDeviceInternal(vendorId: String, deviceName: String): Account {
        val credentialIOSDevice = QCredentialIOSDevice.credentialIOSDevice
        credentialIOSDeviceRepository.findAll(credentialIOSDevice.vendorId.eq(vendorId)).any { throw ConflictException(DEVICE_ID_ALREADY_LINKED_TO_ANOTHER_ACCOUNT) }

        val now = Instant.now()
        val account = Account(now)

        val vendorIdCredential = CredentialIOSDevice(
                account = account,
                vendorId = vendorId,
                deviceName = deviceName,
                now = now)

        account.credentials.add(vendorIdCredential)

        return accountRepository.save(account)
    }


    @Transactional
    fun getAccountOfSecurityContext(securityContext: SecurityContext): Account {
        val userPrincipal = securityContext.userPrincipal
                ?: throw NotAuthorizedException(THERE_WAS_NO_PRINCIPAL_SET_IN_THE_SECURITY_CONTEXT)

        val accountId = userPrincipal.name
        if (accountId.isNullOrBlank()) {
            throw NotAuthorizedException(THE_PRINCIPAL_DID_NOT_HAVE_A_NON_BLANK_NAME)
        }

        return accountRepository.findById(accountId)
                ?: throw NotFoundException("$THERE_WAS_NO_ACCOUNT_FOR_ACCOUNT_ID'$accountId'")
    }

    fun deleteAccount(securityContext: SecurityContext) {
        accountRepository.delete(getAccountOfSecurityContext(securityContext))
    }

}
