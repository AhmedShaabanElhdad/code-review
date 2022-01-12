package com.netguru.codereview.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.netguru.codereview.network.model.ShopListItemResponse
import com.netguru.codereview.network.model.ShopListResponse
import com.netguru.codereview.shoplist.R
import com.netguru.codereview.ui.model.ShopList
import javax.inject.Inject

class MainFragment : Fragment() {

    /*
    * case    :- use annotation @Inject with viewmodel declaration
    * desc    :- you ask dagger to get instance for viewmodel from fragment component
    *            but you had initialize it by yourself using ViewModelProvider (factory)
    * Solution:-  remove @Inject or user
    * */
    @Inject
    private var viewModel: MainViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        layoutInflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        /*
        * case:- using this in observe which refer to lifecycleOwner
        * desc:- may lead to memory leak when onCreateView called again
        * Solution:- use viewLifecycleOwner
        *
        * */

        /*
        * case:- style problem of write lambda
        * desc:- Lambda argument should be moved out of parentheses
        * */


        /*
        * use ? instead of assertion !!
        * */


        /*
        * use single responsability and make observation in seperated functions
        * */
        viewModel!!.shopLists.observe(this, { lists ->

            /*
            * case:- declare object for view in observer
            * desc:- every time observer change will create new instance of view object which in turn will lead to memory leak
            * Solution :- should be done in onCreateView() function
            * */


            /*
            * case:- use findview by id
            * desc:- may lead to null pointer exception if there is miss spelling in id
            * Solution:- use viewbinding
            * */
            val progressBar = view.findViewById<ProgressBar>(R.id.message)
            val latestIcon = view.findViewById<ImageView>(R.id.latest_list_icon)



            /*
            * case:- unused variable
            * take space in memory and not used
            * */
            val shopLists = lists.map { mapShopList(it.first, it.second) }.also {
                /*
                * what if the list is empty will through an exception
                * solution use scope function like takeIf or handle exception
                * */
                latestIcon?.load(it.first().iconUrl)
            }


            /*
            * after move progressBar declaration make it visible
            * */
            progressBar?.isVisible = false

            Log.i("LOGTAG", "LOLOLOL Is it done already?")


            // Display the list in recyclerview
            // adapter.submitList(shopLists)
        })


        /*
        * the same like previous case for observer
        * */
        viewModel!!.events().observe(this, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun mapShopList(list: ShopListResponse, items: List<ShopListItemResponse>) =
        ShopList(
            list.list_id,
            list.userId,
            list.listName,
            list.listName,
            items
        )
}
