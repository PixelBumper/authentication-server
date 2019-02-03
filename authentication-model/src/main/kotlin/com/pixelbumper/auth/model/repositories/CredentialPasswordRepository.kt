package com.pixelbumper.auth.model.repositories

import com.pixelbumper.auth.model.entities.CredentialPassword
import java.util.*

interface CredentialPasswordRepository : BaseRepository<CredentialPassword, UUID> {
    fun findByEmail(email: String): CredentialPassword?
}
