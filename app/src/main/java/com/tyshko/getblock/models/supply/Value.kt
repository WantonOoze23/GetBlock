package com.tyshko.getblock.models.supply

import kotlinx.serialization.Serializable

@Serializable
data class Value(
    val circulating: Long,
    val nonCirculating: Long,
    val nonCirculatingAccounts: List<String>,
    val total: Long
)