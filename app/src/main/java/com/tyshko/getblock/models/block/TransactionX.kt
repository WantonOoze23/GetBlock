package com.tyshko.getblock.models.block

import kotlinx.serialization.Serializable

@Serializable
data class TransactionX(
    val message: Message,
    val signatures: List<String>
)