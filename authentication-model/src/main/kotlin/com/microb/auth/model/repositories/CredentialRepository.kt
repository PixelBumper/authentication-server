package com.microb.auth.model.repositories

import com.microb.auth.model.entities.Credential
import java.util.*

interface CredentialRepository : BaseRepository<Credential, UUID> {
}