package com.microb.auth.services

import com.microb.auth.jersey.filters.REQUEST_TIME
import org.springframework.stereotype.Service
import org.springframework.web.context.annotation.RequestScope
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.WebRequest
import java.time.Instant

@Service
@RequestScope
class TimeService {

    fun getCurrentTime(): Instant {
        return RequestContextHolder.currentRequestAttributes().getAttribute(REQUEST_TIME, WebRequest.SCOPE_REQUEST) as Instant
    }
}
