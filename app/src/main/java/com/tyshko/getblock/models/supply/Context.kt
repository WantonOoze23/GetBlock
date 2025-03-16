package com.tyshko.getblock.models.supply

import kotlinx.serialization.Serializable

@Serializable
data class Context(
    val apiVersion: String,
    val slot: Int
)