package com.microb.auth.model.entities

import javax.persistence.Column
import javax.persistence.Entity

@Entity
class CredentialPassword(

        account: Account,

        @Column(unique = true)
        var email: String,
        var password: String
) : Credential(account = account)
