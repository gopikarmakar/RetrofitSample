package com.hyend.retrofitsample.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.StringSignature
import com.hyend.mylibrary.api.responses.ItemResultResponse
import com.hyend.mylibrary.client.ItemSearchAPIClient
import com.hyend.mylibrary.client.ItemsResponseCallback
import com.hyend.retrofitsample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @NonNull
    private lateinit var _itemResult: ItemResultResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Fetching the items for the search query
     */
    fun fetchAndDisplayItems(view: View) {

        val params = mapOf("keyword" to "house", "applicationId" to "smart_device_japan")

        ItemSearchAPIClient().fetchItems(params, object : ItemsResponseCallback {
            override fun onSuccess(itemResult: ItemResultResponse?) {

                if(itemResult != null) {
                    _itemResult = itemResult

                    val imageUrl = itemResult.items[0].item.mediumImageUrls[0].imageUrl

                    //Will show the item's image in case of success
                    loadImage(imageUrl)
                    itemName.text = itemResult.items[0].item.itemName
                }
                else {
                    showErrorDialog(resources.getString(R.string.responseErrorMessage))
                }
            }

            override fun onFailure(errorMessage: String) {
                //Will show the error dialog in case of failure
                showErrorDialog(errorMessage)
            }
        })
    }

    /**
     * Displaying the item's image received in response
     */
    private fun loadImage(url: String) {

        Glide.with(this)
            .load(url)
            .signature(StringSignature((System.currentTimeMillis()-100).toString()))
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .centerCrop()
            .into(itemImage)
    }

    /**
     * Displaying the error dialog message on failure
     */
    private fun showErrorDialog(errorMessage: String) {

        lateinit var alertDialog: AlertDialog

        val alertBuilder = AlertDialog.Builder(this)

        alertBuilder.setTitle(R.string.dialogTitle)
        alertBuilder.setMessage(errorMessage)

        alertBuilder.setPositiveButton(resources.getString(R.string.retryButton)) { _, _ ->
            fetchAndDisplayItems(fetchButton)
        }

        alertBuilder.setNegativeButton(resources.getString(R.string.cancelButton)) {_, _ ->
            alertDialog.dismiss()
        }

        alertDialog = alertBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}