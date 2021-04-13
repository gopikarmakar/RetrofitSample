package com.hyend.mylibrary.api.responses

import com.google.gson.annotations.SerializedName

/**
 * Response Data Class
 */
data class ItemResultResponse(
    val pageCount: Int,
    val hits: Int,
    val last: Int,
    // val count: Int,
    @SerializedName("Items") val items: List<Items>
) {
    /**
     * Printing the first record
     */
    override fun toString(): String {
        return "Page Count = " + pageCount + "\n" +
                "Hits = " + hits + "\n" +
                "Last = " + last + "\n" +
                // "Count = " + count + "\n" +
                // "Total Items = " + items?.size + "\n" +
                "Item Code = " + items[0].item.itemCode + "\n" +
                "Item Name = " + items[0].item.itemName + "\n" +
                "Item Price = " + items[0].item.itemPrice + "\n" +
                "Item URL = " + items[0].item.itemUrl + "\n" +
                "Shop Id = " + items[0].item.shopId + "\n" +
                "Shop Name = " + items[0].item.shopName + "\n" +
                "Shop URL = " + items[0].item.shopUrl + "\n" +
                "Medium Image URL = " + items[0].item.mediumImageUrls[0].imageUrl
    }
}

/**
 * Items list
 */
data class Items(
    @SerializedName("Item") var item: Item
)

/**
 * Each item
 */
data class Item(
    val shopId: Int,
    val itemCode: String,
    val itemPrice: Int,
    val shopName: String,
    val shopUrl: String,
    val itemName: String,
    val itemUrl: String,
    @SerializedName("mediumImageUrls") var mediumImageUrls: List<MediumImageUrls>
)

/**
 * Item Image Url Lists
 */
data class MediumImageUrls(
    val imageUrl: String
)