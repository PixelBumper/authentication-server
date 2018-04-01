package com.microb.auth.model.entities

import java.security.Principal
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Account(
        @OneToMany(
                mappedBy = "account",
                cascade = [CascadeType.ALL],
                targetEntity = Credential::class)
        var credentials: MutableList<Credential> = mutableListOf()
) : BaseEntity(), Principal {

    override fun getName(): String = "$id"
}

