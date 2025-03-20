package com.tyshko.getblock.models.stack

data class UiStack(
    //Supply
    val circulatingSupply: Long = 1,
    val nonCirculatingSupply: Long = 1,
    val totalSupply: Long = 1,

    val percentCirculatingSupply: Double = 1.1,
    val percentNonCirculatingSupply: Double = 1.1,

    //Epoch
    val absoluteSlotEpoch: Int = 1,
    val blockHeightEpoch: Int = 1,
    val epoch: Int = 1,
    val slotIndexEpoch: Int = 1,
    val slotsInEpochEpoch: Int = 1,
    val transactionCountEpoch: Long = 1,

    //Block


)
