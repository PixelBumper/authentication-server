package com.microb.auth.model.entities

import com.microb.auth.model.entities.Account
import com.microb.auth.model.entities.Credential
import javax.persistence.Column
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(value = Credential.CredentialType.PASSWORD_DISCRIMINATOR)
class CredentialPassword(

        account: Account,

        @Column(unique = true)
        var email: String,
        var password: String
) : Credential(account = account)
