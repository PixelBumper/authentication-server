package com.pixelbumper.auth.jersey.dtos

import com.pixelbumper.auth.model.entities.Account
import java.util.*
import javax.validation.constraints.NotNull

class AccountDTO(
        @NotNull
        var id: UUID? = null)

fun Account.assembleDto(): AccountDTO = AccountDTO(id = this.id)
