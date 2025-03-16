package com.tyshko.getblock.models.supply

import kotlinx.serialization.Serializable

@Serializable
data class GetSupply(
    val context: Context,
    val value: Value
)