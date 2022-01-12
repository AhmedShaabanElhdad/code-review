package com.netguru.codereview.network



/*
* make Repository  singleton
* */

class ShopListRepository(private val shopListApi: ShopListApi) {

    suspend fun getShopLists() = shopListApi.getShopLists()

    suspend fun getShopListItems(listId: String) = shopListApi.getShopListItems(listId)


    /*
    * make it suspend function and  make also getUpdateEvents suspend also to make use of coroutine
    * */
    fun updateEvents() = shopListApi.getUpdateEvents()
}
