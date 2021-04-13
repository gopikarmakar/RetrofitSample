package com.hyend.mylibrary.api

import com.hyend.mylibrary.api.responses.Item
import com.hyend.mylibrary.api.responses.ItemResultResponse
import com.hyend.mylibrary.api.responses.Items
import com.hyend.mylibrary.api.responses.MediumImageUrls
import com.hyend.mylibrary.client.NetworkConfig
import com.hyend.mylibrary.extensions.enqueueResponse
import com.hyend.mylibrary.testutils.createRequest
import junit.framework.Assert.assertEquals
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import strikt.api.expectThat

/**
 * Test Cases
 */
class ItemSearchWrapperTesting {

    private val PORT = 8080
    private val mockWebServer = MockWebServer()

    @Before
    fun before() {
        mockWebServer.start(PORT)
        NetworkConfig.BASE_URL = mockWebServer.url("client").toString() + "/"
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    //@Test
    fun `it should be a https request`() {

        mockWebServer.enqueueResponse("fetch-items-json-1-200.json", 200)

        // Using Strikt
        expectThat(createRequest().request()) {
            assertThat("Is it a https request") {
                it.isHttps
            }
        }
    }

    @Test
    fun `it should be a GET request`() {

        mockWebServer.enqueueResponse("fetch-items-json-1-200.json", 200)

        // Using Strikt
        expectThat(createRequest().request()) {
            assertThat("Is it a GET method") {
                it.method == "GET"
            }
        }
    }

    @Test
    fun `should match request parameters`() {

        mockWebServer.enqueueResponse("fetch-items-json-1-200.json", 200)

        // Request params
        val keyword: String = "house"
        val applicationId: String = "smart_device_japan"

        // Using Strikt to verify
        expectThat(createRequest().request()) {
            assertThat("Has given a search query") {
                it.url.queryParameterValues("applicationId") == listOf(applicationId)
                it.url.queryParameterValues("keyword") == listOf(keyword)
            }
        }
    }

    @Test
    fun `request execution should succeed`() {

        mockWebServer.enqueueResponse("fetch-items-json-1-200.json", 200)
        // Using Kluent
        createRequest().execute().isSuccessful.`should be true`()
    }

    @Test
    fun `response should not be null after request execution`() {

        mockWebServer.enqueueResponse("fetch-items-json-1-200.json", 200)
        // Using Kluent
        createRequest().execute().body().shouldNotBeNull()
    }

    @Test
    fun `response type should be itemresult class type`() {

        mockWebServer.enqueueResponse("fetch-items-json-1-200.json", 200)
        // Using Kluent
        createRequest().execute().body().`should be instance of`(ItemResultResponse::class.java)
    }

    @Test
    fun `it should match the response type 1`() {

        mockWebServer.enqueueResponse("fetch-items-json-1-200.json", 200)
        val expected = ItemResultResponse(
            100,
            30,
            30,
            listOf(
                Items(
                    Item(
                        213310,
                        "book:20153591",
                        4136,
                        "楽天ブックス",
                        "https://www.rakuten.co.jp/book/",
                        "HOUSE ハウス【Blu-ray】 [ 佐藤美恵子 ]",
                        "https://item.rakuten.co.jp/book/16476261/",
                        listOf(
                            MediumImageUrls(
                                "https://thumbnail.image.rakuten.co.jp/@0_mall/book/cabinet/5583/4988104125583.jpg?_ex=128x128"
                            )
                        )
                    )
                )
            )
        )

        // Using Strikt
        expectThat(createRequest().execute()) {
            assertThat("Verifying the received response") {
                it.body()?.pageCount == 100
                //it.body().toString() == expected.toString()
            }
        }
    }

    @Test
    fun `it should match the response type 2`() {

        mockWebServer.enqueueResponse("fetch-items-json-1-200.json", 200)

        /**
         * Parameterized Test
         */
        val actual = createRequest().execute().body()

        actual?.let {
            assertEquals(100, it.pageCount)
            assertEquals(30, it.hits)
            assertEquals(30, it.last)
            assertEquals(213310, it.items[0].item.shopId)
            assertEquals("book:20153591", it.items[0].item.itemCode)
            assertEquals(4136, it.items[0].item.itemPrice)
            assertEquals("楽天ブックス", it.items[0].item.shopName)
            assertEquals("https://www.rakuten.co.jp/book/", it.items[0].item.shopUrl)
            assertEquals("HOUSE ハウス【Blu-ray】 [ 佐藤美恵子 ]", it.items[0].item.itemName)
            assertEquals("https://item.rakuten.co.jp/book/16476261/", it.items[0].item.itemUrl)
            assertEquals(
                "https://thumbnail.image.rakuten.co.jp/@0_mall/book/cabinet/5583/4988104125583.jpg?_ex=128x128",
                it.items[0].item.mediumImageUrls[0].imageUrl
            )
        }
    }
}

/**
 * Positive Parameterized Tests
 */
@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(sdk = [21], manifest = Config.NONE)
class ParameterizedTests(
    private val jsonFile: String,
    private val statusCode: Int,
    private val errorMessage: String,
    private val pageCount: Int,
    private val hits: Int,
    private val last: Int,
    private val shopId: Int,
    private val itemCode: String,
    private val itemPrice: Int,
    private val shopName: String,
    private val shopUrl: String,
    private val itemName: String,
    private val itemUrl: String,
    private val imageUrl: String
) {

    private val PORT = 8080
    private val mockWebServer = MockWebServer()

    @Before
    fun before() {
        mockWebServer.start(PORT)
        NetworkConfig.BASE_URL = mockWebServer.url("client").toString() + "/"
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `it should match the positive response`() {

        mockWebServer.enqueueResponse(jsonFile, statusCode)

        /**
         * Positive Parameterized Test
         */
        val request = createRequest().execute()

        if(request.code() == 200) {

            val response = request.body()

            response?.let {
                // Using Kluent
                it.pageCount `should be equal to` pageCount
                it.hits `should be equal to` hits
                it.last `should be equal to` last
                it.items[0].item.shopId `should be equal to` shopId
                it.items[0].item.itemCode `should be equal to` itemCode
                it.items[0].item.itemPrice `should be equal to` itemPrice
                it.items[0].item.shopName `should be equal to` shopName
                it.items[0].item.shopUrl `should be equal to` shopUrl
                it.items[0].item.itemName `should be equal to` itemName
                it.items[0].item.itemUrl `should be equal to` itemUrl
                it.items[0].item.mediumImageUrls[0].imageUrl `should be equal to` imageUrl
            }
        }
        else {
            val response = request.toString()
            when(request.code()) {

                400 -> { response.shouldContain(errorMessage) }
                500 -> { response.shouldContain(errorMessage) }
            }
        }
    }

    /**
     * Setting parameters for Robolectric parameterised test
     */
    companion object {
        @JvmStatic
        // name argument is optional, it will show up on the test results
        @ParameterizedRobolectricTestRunner.Parameters(name = "Input: {0}")
        // parameters are provided as arrays, allowing more than one parameter
        fun params(): List<Array<Any>> {
            return listOf(
                arrayOf<Any>(
                    "fetch-items-json-1-200.json",
                    200,
                    "Successful",
                    100,
                    30,
                    30,
                    213310,
                    "book:20153591",
                    4136,
                    "楽天ブックス",
                    "https://www.rakuten.co.jp/book/",
                    "HOUSE ハウス【Blu-ray】 [ 佐藤美恵子 ]",
                    "https://item.rakuten.co.jp/book/16476261/",
                    "https://thumbnail.image.rakuten.co.jp/@0_mall/book/cabinet/5583/4988104125583.jpg?_ex=128x128"
                ),
                arrayOf<Any>(
                    "fetch-items-json-2-200.json",
                    200,
                    "Successful",
                    100,
                    5,
                    3,
                    272452,
                    "yokoaunty:10022240",
                    29700,
                    "ヨコアンティ",
                    "https://www.rakuten.co.jp/yokoaunty/",
                    "ゴールデングース (Golden Goose) HAUSロゴパーカー コットンスウェット ホワイト g26d087a1 2015SS レディース春夏 送料無料 【正規取扱】",
                    "https://item.rakuten.co.jp/yokoaunty/g26d087a1/",
                    "https://thumbnail.image.rakuten.co.jp/@0_mall/t-on/cabinet/19ss01/qqcnjx04-top.jpg?_ex=128x128"
                ),
                arrayOf<Any>(
                    "error-json-400.json",
                    400,
                    "Client Error",
                    0,
                    0,
                    0,
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    ""
                ),
                arrayOf<Any>(
                    "error-json-500.json",
                    500,
                    "Server Error",
                    0,
                    0,
                    0,
                    0,
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",
                    ""
                )
            )
        }
    }
}
