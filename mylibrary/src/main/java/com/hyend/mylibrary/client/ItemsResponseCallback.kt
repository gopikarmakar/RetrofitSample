package com.hyend.mylibrary.client

import com.hyend.mylibrary.api.responses.ItemResultResponse

interface ItemsResponseCallback {

    /**
     * Returns itemResult response re
     */
    fun onSuccess(itemResult: ItemResultResponse?)

    /**
     * Returns the failure message
     */
    fun onFailure(errorMessage: String)
}