package com.pixelbumper.auth.model.entities

import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table
class CredentialFacebook(
        account: Account,
        now: Instant,
        facebookID: String) : Credential(account, now) {

    @Column(nullable = false,
            unique = true)
    var facebookID = facebookID
}
