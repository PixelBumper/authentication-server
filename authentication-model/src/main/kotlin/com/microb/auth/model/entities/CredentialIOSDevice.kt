package com.microb.auth.model.entities

import javax.persistence.Column
import javax.persistence.Entity

@Entity
class CredentialIOSDevice(
        account: Account,
        vendorId: String
) : Credential(account) {

    @Column(updatable = false,
            unique = true)
    var vendorId: String = vendorId
        internal set
}