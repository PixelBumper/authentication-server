package com.microb.auth.model.entities

import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class CredentialIOSDevice(
        account: Account,
        vendorId: String,
        deviceName: String,
        now: Instant
) : Credential(account, now) {

    @Column(nullable = false,
            updatable = false,
            unique = true)
    var vendorId: String = vendorId
        internal set

    @Column(nullable = false,
            updatable = false)
    var deviceName: String = deviceName
        internal set
}
