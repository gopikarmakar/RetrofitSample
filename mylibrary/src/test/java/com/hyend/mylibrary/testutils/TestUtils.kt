package com.hyend.mylibrary.testutils

import com.hyend.mylibrary.api.requests.ItemSearchAPI
import com.hyend.mylibrary.api.responses.ItemResultResponse
import com.hyend.mylibrary.client.ApiClient
import retrofit2.Call

internal fun createRequest(): Call<ItemResultResponse> {

    val itemSearchApi = ApiClient.apiClient.create(ItemSearchAPI::class.java)
    val applicationId: String = "smart_device_japan"
    val keyword: String = "house"
    return itemSearchApi.getItems(applicationId, keyword)
}