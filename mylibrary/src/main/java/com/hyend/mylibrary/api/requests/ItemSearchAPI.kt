package com.hyend.mylibrary.api.requests

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.hyend.mylibrary.client.NetworkConfig
import com.hyend.mylibrary.api.responses.ItemResultResponse

internal interface ItemSearchAPI {
    @GET(NetworkConfig.ENDPOINT)
    fun getItems(
        @Query("applicationId") applicationId: String?,
        @Query("keyword") itemCode: String?
    ): Call<ItemResultResponse>
}