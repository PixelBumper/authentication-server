package com.pixelbumper.auth.model.repositories

import com.pixelbumper.auth.model.entities.CredentialIOSDevice
import java.util.*

interface CredentialIOSDeviceRepository : BaseRepository<CredentialIOSDevice, UUID> {
    fun findByVendorId(vendorId: String): CredentialIOSDevice?
}
