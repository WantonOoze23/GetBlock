package com.tyshko.getblock.models.stack

data class UiStack(
    //Supply
    val circulatingSupply: Long = 0,
    val nonCirculatingSupply: Long = 0,
    val totalSupply: Long = 0,

    //Epoch
    val absoluteSlotEpoch: Int = 0,
    val blockHeightEpoch: Int = 0,
    val epoch: Int = 0,
    val slotIndexEpoch: Int = 0,
    val slotsInEpochEpoch: Int = 0,
    val transactionCountEpoch: Long = 0,

    //Block

)
