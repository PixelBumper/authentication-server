package com.microb.auth.services

import com.microb.auth.model.entities.*
import com.microb.auth.model.repositories.AccountRepository
import com.microb.auth.model.repositories.CredentialIOSDeviceRepository
import com.microb.auth.model.repositories.CredentialPasswordRepository
import com.microb.auth.security.PasswordService
import com.microb.auth.services.exceptions.ConflictException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import javax.ws.rs.core.SecurityContext

const val EMAIL_IS_ALREADY_LINKED_TO_ANOTHER_ACCOUNT = "there is already an account associated with this email"
const val DEVICE_ID_ALREADY_LINKED_TO_ANOTHER_ACCOUNT = "there is already an account associated with this device id"

@Service
class AccountService(
        val accountRepository: AccountRepository,
        val emailCredentialRepository: CredentialPasswordRepository,
        val credentialIOSDeviceRepository: CredentialIOSDeviceRepository,
        val passwordService: PasswordService,
        val transactionTemplate: TransactionTemplate
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

        val account = Account()

        val credential: Credential = CredentialPassword(
                account = account,
                email = email,
                password = passwordService.hash(password))

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

        val account = Account()

        val vendorIdCredential = CredentialIOSDevice(
                account = account,
                vendorId = vendorId,
                deviceName = deviceName)

        account.credentials.add(vendorIdCredential)

        accountRepository.save(account)

        return account
    }


    @Transactional
    fun getAccountOfSecurityContext(securityContext: SecurityContext): Account {
        val accountId = securityContext.userPrincipal.name
        return accountRepository.findById(accountId)
                ?: throw RuntimeException("security contexts should always contain an existing account as principal")
    }

}