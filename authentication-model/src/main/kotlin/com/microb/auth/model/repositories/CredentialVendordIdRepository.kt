package com.microb.auth.model.repositories

import com.microb.auth.model.entities.CredentialVendorId
import com.microb.auth.model.repositories.BaseRepository
import java.util.*

interface CredentialVendordIdRepository : BaseRepository<CredentialVendorId, UUID> {
    fun findByVendorId(vendorId:String): CredentialVendorId?
}