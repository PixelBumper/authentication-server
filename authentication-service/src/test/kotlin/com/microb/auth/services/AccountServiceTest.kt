package com.microb.auth.services

import com.microb.auth.model.entities.Account
import com.microb.auth.model.entities.CredentialIOSDevice
import com.microb.auth.model.entities.CredentialPassword
import com.microb.auth.model.entities.QCredentialIOSDevice
import com.microb.auth.model.repositories.AccountRepository
import com.microb.auth.model.repositories.CredentialIOSDeviceRepository
import com.microb.auth.model.repositories.CredentialPasswordRepository
import com.microb.auth.security.PasswordService
import com.microb.auth.services.exceptions.ConflictException
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.transaction.support.TransactionTemplate
import org.junit.rules.ExpectedException
import org.springframework.dao.DataIntegrityViolationException
import java.security.Principal
import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.core.SecurityContext


private const val PASSWORD = "password"

private const val EMAIL = "email"

private const val HASHED_PASSWORD = "hashedpassword"

private const val DEVICE_NAME = "My Iphone"

private const val VENDOR_ID = "vendorId"

@RunWith(MockitoJUnitRunner::class)
class AccountServiceTest {

    @get:Rule
    val exception = ExpectedException.none()!!

    @Mock
    lateinit var accountRepository: AccountRepository

    @Mock
    lateinit var emailCredentialRepository: CredentialPasswordRepository

    @Mock
    lateinit var credentialIOSDeviceRepository: CredentialIOSDeviceRepository

    @Mock
    lateinit var passwordService: PasswordService

    @Mock
    lateinit var transactionTemplate: TransactionTemplate

    lateinit var accountService: AccountService

    @Before
    fun setUp(){
        accountService = AccountService(
                accountRepository,
                emailCredentialRepository,
                credentialIOSDeviceRepository,
                passwordService,
                transactionTemplate)
    }

    @Test
    fun `test account creation for email and password`() {
        doReturn(HASHED_PASSWORD).`when`(passwordService).hash(PASSWORD)
        doAnswer(returnsFirstArg<Account>()).`when`(accountRepository).save(any())

        val account = accountService.createAccountForEmail(EMAIL, PASSWORD)

        val credentials = account.credentials

        assertThat(credentials.size, `is`(1))

        val emailAndPasswordCredential = credentials[0] as CredentialPassword
        assertThat(emailAndPasswordCredential.account, `is`(account))
        assertThat(emailAndPasswordCredential.email, `is`(EMAIL))
        assertThat(emailAndPasswordCredential.password, `is`(HASHED_PASSWORD))

        verify(emailCredentialRepository).findByEmail(EMAIL)
        verify(accountRepository).save(account)
    }

    @Test
    fun `ensure that a ConflictException is thrown when there is already an account associated with the given email`(){
        doReturn(mock(CredentialPassword::class.java)).`when`(emailCredentialRepository).findByEmail(EMAIL)

        exception.expect(ConflictException::class.java)
        exception.expectMessage(EMAIL_IS_ALREADY_LINKED_TO_ANOTHER_ACCOUNT)
        accountService.createAccountForEmail(EMAIL, PASSWORD)
    }

    @Test
    fun `ensure that a ConflictException is thrown when the unique constraint on the email field fails`(){
        doReturn(HASHED_PASSWORD).`when`(passwordService).hash(PASSWORD)

        // simulate unique constraint violation
        val integrityViolationException = mock(DataIntegrityViolationException::class.java)
        doThrow(integrityViolationException).`when`(accountRepository).save(ArgumentMatchers.any())

        exception.expect(ConflictException::class.java)
        exception.expectMessage(EMAIL_IS_ALREADY_LINKED_TO_ANOTHER_ACCOUNT)
        exception.expectCause(`is`(integrityViolationException))
        accountService.createAccountForEmail(EMAIL, PASSWORD)
    }

    @Test
    fun `test account creation for iosDevice`() {
        doAnswer(returnsFirstArg<Account>()).`when`(accountRepository).save(any())

        val account = accountService.createAccountForIOSDevice(VENDOR_ID, DEVICE_NAME)

        val credentials = account.credentials

        assertThat(credentials.size, `is`(1))

        val credentialIOSDevice = credentials[0] as CredentialIOSDevice
        assertThat(credentialIOSDevice.account, `is`(account))
        assertThat(credentialIOSDevice.vendorId, `is`(VENDOR_ID))
        assertThat(credentialIOSDevice.deviceName, `is`(DEVICE_NAME))

        verify(accountRepository).save(account)
    }

    @Test
    fun `ensure that a ConflictException is thrown when there is already an account associated with the given device`(){

        doReturn(listOf(mock(CredentialIOSDevice::class.java)))
                .`when`(credentialIOSDeviceRepository)
                .findAll(eq(QCredentialIOSDevice.credentialIOSDevice.vendorId.eq(VENDOR_ID)))

        exception.expect(ConflictException::class.java)
        exception.expectMessage(DEVICE_ID_ALREADY_LINKED_TO_ANOTHER_ACCOUNT)
        accountService.createAccountForIOSDevice(VENDOR_ID, DEVICE_NAME)
    }

    @Test
    fun `ensure that a ConflictException is thrown when the deviceId unique constraint fails`(){
        // simulate unique constraint violation
        val integrityViolationException = mock(DataIntegrityViolationException::class.java)
        doThrow(integrityViolationException).`when`(accountRepository).save(ArgumentMatchers.any())

        exception.expect(ConflictException::class.java)
        exception.expectMessage(DEVICE_ID_ALREADY_LINKED_TO_ANOTHER_ACCOUNT)
        exception.expectCause(`is`(integrityViolationException))

        accountService.createAccountForIOSDevice(VENDOR_ID, DEVICE_NAME)
    }

    // this little hack is necessary since mockitos eq will return null and therefore cause kotlin to throw an IllegalStateException at runtime
    private fun <T> eq(value:T): T {
        ArgumentMatchers.eq<T>(value)
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T

    @Test
    fun `ensure the account is retrieved from the security context`(){
        val account = Account()

        doReturn(account).`when`(accountRepository).findById(eq(account.name))

        val securityContext = mock(SecurityContext::class.java)

        doReturn(account).`when`(securityContext).userPrincipal

        val accountOfSecurityContext = accountService.getAccountOfSecurityContext(securityContext)

        assertThat(accountOfSecurityContext, `is`(account))

    }

    @Test
    fun `ensure that a NotAuthorizedException is thrown when no principal is provided`() {

        exception.expect(NotAuthorizedException::class.java)
        exception.expectMessage(THERE_WAS_NO_PRINCIPAL_SET_IN_THE_SECURITY_CONTEXT)

        accountService.getAccountOfSecurityContext(mock(SecurityContext::class.java))
    }

    @Test
    fun `ensure that a NotAuthorizedException is thrown when the principal name is blank`() {
        val principal = mock(Principal::class.java)
        doReturn(" ").`when`(principal).name

        val securityContext = mock(SecurityContext::class.java)
        doReturn(principal).`when`(securityContext).userPrincipal

        exception.expect(NotAuthorizedException::class.java)
        exception.expectMessage(THE_PRINCIPAL_DID_NOT_HAVE_A_NON_BLANK_NAME)

        accountService.getAccountOfSecurityContext(securityContext)
    }

    @Test
    fun `ensure that a NotAuthorizedException is thrown when the principal name is null`() {
        val principal = mock(Principal::class.java)
        doReturn(null).`when`(principal).name

        val securityContext = mock(SecurityContext::class.java)
        doReturn(principal).`when`(securityContext).userPrincipal

        exception.expect(NotAuthorizedException::class.java)
        exception.expectMessage(THE_PRINCIPAL_DID_NOT_HAVE_A_NON_BLANK_NAME)

        accountService.getAccountOfSecurityContext(securityContext)
    }

    @Test
    fun `ensure that a RuntimeException is thrown when trying to get an account for a principal that has no account`(){
        val principal = mock(Principal::class.java)
        doReturn("some accountId").`when`(principal).name

        val securityContext = mock(SecurityContext::class.java)
        doReturn(principal).`when`(securityContext).userPrincipal

        exception.expect(RuntimeException::class.java)
        exception.expectMessage(THERE_WAS_NO_ACCOUNT_FOR_ACCOUNT_ID)

        accountService.getAccountOfSecurityContext(securityContext)
    }

}
