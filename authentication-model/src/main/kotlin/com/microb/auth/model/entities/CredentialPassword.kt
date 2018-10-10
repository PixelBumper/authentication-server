package com.microb.auth.model.entities

import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class CredentialPassword(
        account: Account,
        email: String,
        password: String,
        now: Instant
) : Credential(account, now) {

    @Column(nullable = false,
            unique = true)
    var email = email

    @Column(nullable = false)
    var password = password
}
