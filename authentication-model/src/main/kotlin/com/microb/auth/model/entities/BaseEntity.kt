package com.microb.auth.model.entities

import java.util.*
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity {

    @Id
    @Column(
            columnDefinition = "uuid",
            updatable = false)
    var id = UUID.randomUUID()
        private set

    override final fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseEntity) return false

        if (id != other.id) return false

        return true
    }

    override final fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }


}