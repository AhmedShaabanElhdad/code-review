package com.netguru.codereview.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netguru.codereview.network.ShopListApiMock
import com.netguru.codereview.network.ShopListRepository
import com.netguru.codereview.network.model.ShopListItemResponse
import com.netguru.codereview.network.model.ShopListResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {


    /*
    * case :- when viewmodel cleared the instance for repository is still exist
    * solution :- assign it to null in onCleared()
    * */

    private val shopListRepository = ShopListRepository(ShopListApiMock())


    /*
    * case:-  user can in fragment assign value for shopLists live data
    * desc:- shopLists must be LiveData and had only get assign to  another mutable live data variable
    * */
    val shopLists = MutableLiveData<List<Pair<ShopListResponse, List<ShopListItemResponse>>>>()
    private val eventLiveData = MutableLiveData<String>()

    init {

        /*
        * getShopLists() should use io dispatcher as it may take some time
        * */

        viewModelScope.launch {
            val lists = shopListRepository.getShopLists()
            /*
            * you can use mapper class to map the two DTO objects to one ui model
            * */
            val data = mutableListOf<Pair<ShopListResponse, List<ShopListItemResponse>>>()
            for (list in lists) {
                val items = shopListRepository.getShopListItems(list.list_id)
                data.add(list to items)
            }
            shopLists.postValue(data)
        }
        getUpdateEvents()
    }

    fun events(): LiveData<String> = eventLiveData


    private fun getUpdateEvents() {

        /*
        * case :- use globalScope.launch will still running although our viewmodel is cleaned
        * Solution :- make viewmodel implement CoroutineScope and make job for it and close the job in onclean function or use viewModelScope
        * */
        GlobalScope.launch() {
            shopListRepository.updateEvents().collect {
                eventLiveData.postValue(it)
            }
        }
    }
}
