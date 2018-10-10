package com.microb.auth.model.entities

import com.microb.auth.hibernate.InstantAttributeConverter
import java.time.Instant
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Credential(
        account: Account,
        now: Instant
) : BaseEntity() {

    @ManyToOne
    @JoinColumn(
            nullable = false,
            updatable = false)
    open var account: Account = account
        internal set

    @Column(
            nullable = false,
            updatable = false)
    @Convert(converter = InstantAttributeConverter::class)
    open var firstTimeSeen: Instant = now
        internal set

    @Column(nullable = false)
    @Convert(converter = InstantAttributeConverter::class)
    open var lastTimeSeen: Instant = now

}
