package com.tyshko.getblock.models.block

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val meta: Meta,
    val transaction: TransactionX
)