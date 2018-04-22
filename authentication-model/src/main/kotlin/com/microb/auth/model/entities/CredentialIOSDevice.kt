package com.microb.auth.model.entities

import com.microb.auth.model.entities.Account
import com.microb.auth.model.entities.Credential
import javax.persistence.Column
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(value = Credential.CredentialType.IOS_VENDOR_ID_DISCRIMINATOR)
class CredentialVendorId(
        account: Account,
        vendorId: String
) : Credential(account) {

    @Column(updatable = false,
            unique = true)
    var vendorId: String = vendorId
        private set
}