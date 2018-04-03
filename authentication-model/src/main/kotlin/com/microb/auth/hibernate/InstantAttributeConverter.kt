package com.microb.auth.hibernate

import java.time.Instant
import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class InstantAttributeConverter : AttributeConverter<Instant, Date> {
    override fun convertToEntityAttribute(dbData: Date?): Instant? {
        return dbData?.toInstant()
    }

    override fun convertToDatabaseColumn(attribute: Instant?): Date? {
        return attribute?.let { Date.from(it) }
    }
}