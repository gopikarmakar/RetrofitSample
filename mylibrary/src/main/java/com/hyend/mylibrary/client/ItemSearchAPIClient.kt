package com.hyend.mylibrary.client

import com.hyend.mylibrary.api.requests.ItemSearchAPI
import com.hyend.mylibrary.api.responses.ItemResultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemSearchAPIClient {

    companion object {

        private const val TAG = "ItemSearchClient"

        private const val KEYWORD = "keyword"
        private const val APPLICATION_ID = "applicationId"
    }

    /**
     * API to fetch the items response
     */
    fun fetchItems(params: Map<String, String>, responseCallback: ItemsResponseCallback) {

        val itemSearchApi = ApiClient.apiClient.create(ItemSearchAPI::class.java)
        val call: Call<ItemResultResponse> = itemSearchApi.getItems(params[APPLICATION_ID], params[KEYWORD])

        call.enqueue(object : Callback<ItemResultResponse> {

            override fun onResponse(call: Call<ItemResultResponse>, response: Response<ItemResultResponse>) {

                if (response.isSuccessful) {

                    when (response.code()) {
                        200 -> {
                            val itemResult: ItemResultResponse? = response.body()
                            responseCallback.onSuccess(itemResult)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ItemResultResponse>, t: Throwable?) {
                t?.printStackTrace()
                responseCallback.onFailure("Failed to fetch the search items!")
            }
        })
    }
}