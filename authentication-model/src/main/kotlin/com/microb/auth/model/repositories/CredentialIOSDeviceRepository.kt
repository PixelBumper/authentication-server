package com.microb.auth.model.repositories

import com.microb.auth.model.entities.CredentialIOSDevice
import java.util.*

interface CredentialIOSDeviceRepository : BaseRepository<CredentialIOSDevice, UUID> {
    fun findByVendorId(vendorId: String): CredentialIOSDevice?
}