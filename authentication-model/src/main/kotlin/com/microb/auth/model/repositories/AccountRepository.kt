package com.microb.auth.model.repositories

import com.microb.auth.model.entities.Account
import java.util.*

interface AccountRepository : BaseRepository<Account, UUID> {

    @JvmDefault
    fun findById(uuid:String): Account?{
        return try {
            findById(UUID.fromString(uuid)).orElse(null)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}