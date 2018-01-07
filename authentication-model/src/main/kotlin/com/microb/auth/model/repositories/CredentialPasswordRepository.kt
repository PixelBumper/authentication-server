package com.microb.auth.model.repositories

import com.microb.auth.model.entities.CredentialPassword
import com.microb.auth.model.repositories.BaseRepository
import java.util.*

interface CredentialPasswordRepository : BaseRepository<CredentialPassword, UUID> {
    fun findByEmail(email:String) : CredentialPassword?
}