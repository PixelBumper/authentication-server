package com.microb.auth.model.entities

import com.microb.auth.hibernate.InstantAttributeConverter
import java.time.Instant
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Credential(account: Account) : BaseEntity() {

    @ManyToOne
    @JoinColumn
    open var account: Account = account
        internal set

    @Convert(converter = InstantAttributeConverter::class)
    open var firstTimeSeen: Instant = Instant.now()
        internal set

    @Convert(converter = InstantAttributeConverter::class)
    open var lastTimeSeen: Instant = Instant.now()

}