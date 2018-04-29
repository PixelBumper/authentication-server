package com.microb.auth.security

import com.microb.auth.model.repositories.CredentialIOSDeviceRepository
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

private const val IOS_DEVICE_ID = "iOSDeviceId"

@Component
class AnonymousIOSDeviceAuthenticationProvider(
        val credentialIOSDeviceRepository: CredentialIOSDeviceRepository
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication? {

        if (authentication.name != IOS_DEVICE_ID) {
            return null
        }

        val vendorId = authentication.credentials?.toString()
        if (vendorId == null || vendorId.isBlank()) {
            throw BadCredentialsException("No iOS device id provided by the client")
        }

        val credentialIOSDevice = credentialIOSDeviceRepository.findByVendorId(vendorId)
                ?: throw BadCredentialsException("there was no credential for vendor id $vendorId")

        return UsernamePasswordAuthenticationToken(credentialIOSDevice.account, vendorId, listOf())

    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}