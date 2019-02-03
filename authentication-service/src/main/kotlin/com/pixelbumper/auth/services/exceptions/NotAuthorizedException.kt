package com.pixelbumper.auth.services.exceptions

import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.core.Response

class NotAuthorizedException : NotAuthorizedException {
    constructor(message: String) : super(message, Response.status(Response.Status.UNAUTHORIZED).build())
    constructor(message: String, cause: Throwable) : super(message, Response.status(Response.Status.UNAUTHORIZED).build(), cause)
}
