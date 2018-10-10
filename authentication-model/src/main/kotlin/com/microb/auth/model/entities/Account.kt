package com.microb.auth.model.entities

import com.microb.auth.hibernate.InstantAttributeConverter
import java.security.Principal
import java.time.Instant
import javax.persistence.*

@Entity
class Account(
        creationDate: Instant,
        credentials: MutableList<Credential> = mutableListOf()
) : BaseEntity(), Principal {

    @Column(
            nullable = false,
            updatable = false)
    @Convert(converter = InstantAttributeConverter::class)
    var creationDate: Instant = creationDate
        internal set

    @OneToMany(
            mappedBy = "account",
            cascade = [CascadeType.ALL],
            targetEntity = Credential::class)
    var credentials = credentials

    override fun getName(): String = "$id"
}

