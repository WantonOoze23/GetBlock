package com.tyshko.getblock.data.repository

import com.tyshko.getblock.models.block.BlockResult
import com.tyshko.getblock.models.epoch.EpochResult
import com.tyshko.getblock.models.rpc.RpcResponse
import com.tyshko.getblock.models.supply.GetSupply

interface RpcRepository {
    suspend fun getEpoch(): RpcResponse<EpochResult>
    suspend fun getSupply(): RpcResponse<GetSupply>
    suspend fun getBlock(blockNumber: Long): RpcResponse<BlockResult>
    suspend fun getBlocks(startSlot: Long, endSlot: Long? = null): List<Long>

}