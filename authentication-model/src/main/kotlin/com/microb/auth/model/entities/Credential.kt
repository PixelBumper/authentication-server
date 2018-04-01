package com.microb.auth.model.entities

import com.microb.auth.hibernate.InstantAttributeConverter
import java.time.Instant
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Credential(
        account: Account) : BaseEntity() {

    @ManyToOne
    @JoinColumn
    var account: Account = account
        private set

    @Convert(converter = InstantAttributeConverter::class)
    var firstTimeSeen: Instant = Instant.now()
        private set

    @Convert(converter = InstantAttributeConverter::class)
    var lastTimeSeen: Instant = Instant.now()


    enum class CredentialType(val discriminator: String) {
        PASSWORD(CredentialType.PASSWORD_DISCRIMINATOR),
        IOS_VENDOR_ID(CredentialType.IOS_VENDOR_ID_DISCRIMINATOR);

        companion object {
            const val PASSWORD_DISCRIMINATOR = "PASSWORD"
            const val IOS_VENDOR_ID_DISCRIMINATOR = "IOS_VENDOR_ID"
        }

    }

}