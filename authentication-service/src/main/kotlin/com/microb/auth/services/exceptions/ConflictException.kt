package com.microb.auth.services.exceptions

import javax.ws.rs.ClientErrorException
import javax.ws.rs.core.Response

class ConflictException : ClientErrorException {

    constructor(message: String) : super(message, Response.Status.CONFLICT, null)


    constructor(message: String, cause: Throwable) : super(message, Response.Status.CONFLICT, cause)
}