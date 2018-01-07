package com.microb.auth.jersey.dtos

import com.microb.auth.model.entities.Account
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class AccountDTO(
        @NotNull
        var id:UUID? = null,

        @NotBlank
        var displayName:String? = null,//TODO really needed? probably better off keeping this one in the target application model and just keep just minimal information

        @NotBlank
        var email:String? = null,

        @NotBlank
        var password:String? = null)

fun Account.assembleDto(): AccountDTO = AccountDTO(
        id= this.id,
        displayName = this.displayName)