package com.lgguzman.example.kotlinseed.ui.fragments


import android.support.v4.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.lgguzman.example.kotlinseed.R
import com.lgguzman.example.kotlinseed.databinding.FragmentHomeBinding
import com.lgguzman.example.kotlinseed.general.App
import com.lgguzman.example.kotlinseed.models.User
import com.lgguzman.example.kotlinseed.services.UserService
import com.lgguzman.example.kotlinseed.ui.activities.main.MainActivity
import com.lgguzman.example.kotlinseed.utils.UIUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.SimpleDateFormat

/**
 * Created by lgguzman on 29/11/17.
 */


open class HomeFragment : Fragment() {

    lateinit var swipe: SwipeRefreshLayout
    lateinit var binding: FragmentHomeBinding



    val listProducts = ArrayList<User>()
    val filterProducts = ArrayList<User>()
    lateinit var productAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val view = binding.getRoot();
        updateUI()
        return view;
    }

    private fun updateUI() {
        (activity as MainActivity).showSearchView("",
                { filter(it) },{  App.insecureUI { UIUtil.hideKeyBoard(activity!!, (activity as MainActivity).search_toolbar) }})
        val view = binding.getRoot()
        productAdapter = UserListAdapter((activity as MainActivity), filterProducts)
        with(view.recycler_view) {
            layoutManager = object: LinearLayoutManager(
                    view.context,
                    LinearLayoutManager.VERTICAL,
                    false){
                override fun isAutoMeasureEnabled(): Boolean {
                    return false
                }
            }
            adapter = productAdapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
        }
        swipe = (activity as MainActivity).swipe
        swipe.isRefreshing = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as MainActivity).selectItemMenu(0)
        iniload()
    }

    private fun iniload(){
        App.insecureUINow { (activity as MainActivity).selectItemMenu(1) }
        swipe.setOnRefreshListener { load() }
        if (listProducts.isEmpty()) {
            swipe.post({
                swipe.isRefreshing = true
                load()
            })
        }
    }


    private fun filter(text: String?) {
        if((activity as MainActivity).getFragment() != null && (activity as MainActivity).getFragment() is HomeFragment) {
            filterProducts.clear()
            if (text == null || text.trim().equals("")) {
                filterProducts.addAll(listProducts)
            } else {
                for (s in listProducts) {
                    s.name?.let {
                        if (it.toLowerCase().contains(text.toLowerCase())  ) {
                            filterProducts.add(s)
                        }
                    }

                }
            }
            productAdapter.notifyDataSetChanged()
        }
    }

     fun load() {
        App.clickUI {
            swipe.isRefreshing = true
            UserService.getUsers()
                    .doOnSubscribe { App.insecureUI { App.prefs!!.isLoading = true; swipe.isRefreshing = true } }
                    .doFinally { App.insecureUI { App.prefs!!.isLoading = false; swipe.isRefreshing = false } }
                    .subscribe({
                            resetList(it)
                    },{
                            resetData()
                    })
        }

    }


    private fun resetList(it: List<User>){
        App.insecureBlock {
            resetData()
            listProducts.addAll(it)
            filterProducts.addAll(listProducts)
            productAdapter.notifyDataSetChanged()
        }

    }
    fun resetData() {
        listProducts.clear()
        filterProducts.clear()
    }


}