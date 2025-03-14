package com.tyshko.getblock.models.block

data class TransactionX(
    val message: Message,
    val signatures: List<String>
)