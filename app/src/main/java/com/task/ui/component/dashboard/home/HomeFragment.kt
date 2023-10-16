package com.task.ui.component.dashboard.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.R
import com.task.RECIPE_ITEM_KEY
import com.task.data.Resource
import com.task.data.dto.recipes.Recipes
import com.task.data.dto.recipes.RecipesItem
import com.task.databinding.HomeFragmentBinding
import com.task.ui.base.bindables.BindingFragment
import com.task.ui.component.details.DetailsActivity
import com.task.ui.component.recipes.adapter.RecipesAdapter
import com.task.utils.SingleEvent
import com.task.utils.observe
import com.task.utils.toGone
import com.task.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@Deprecated(
    "This file will remove after implement its feature",
    ReplaceWith("HomeScreen.kt",""),
    DeprecationLevel.WARNING
)
@AndroidEntryPoint
class HomeFragment : BindingFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val homeViewModel: HomeFragmentViewModel by viewModels()
    private lateinit var recipesAdapter: RecipesAdapter

    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null


    override fun observeViewModel() {
        observe(homeViewModel.recipesLiveData, ::handleRecipesList)
        observe(homeViewModel.openRecipeDetails, ::navigateToDetailsScreen)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments?.getString(ARG_PARAM1)
            mParam2 = arguments?.getString(ARG_PARAM2)
        }

    }

    fun newInstance(param1: String?, param2: String?): HomeFragment? {
        val fragment = HomeFragment()
        val args = Bundle()
        args.putString(ARG_PARAM1, param1)
        args.putString(ARG_PARAM2, param2)
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.viewmodel = homeViewModel
        val layoutManager = LinearLayoutManager(activity)
        binding.rvPostList.layoutManager = layoutManager
        binding.rvPostList.setHasFixedSize(true)
        homeViewModel.getRecipes()

        return binding.root
    }

    private fun handleRecipesList(status: Resource<Recipes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(recipes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { homeViewModel.showToastMessage(it) }
            }
        }
    }

    private fun bindListData(recipes: Recipes) {
        if (!(recipes.recipesList.isNullOrEmpty())) {
            recipesAdapter = RecipesAdapter(homeViewModel, recipes.recipesList)
            binding.rvPostList.adapter = recipesAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
    }


    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.rvPostList.toGone()
    }


    private fun showDataView(show: Boolean) {
        binding.tvNoData.visibility = if (show) View.GONE else View.VISIBLE
        binding.rvPostList.visibility = if (show) View.VISIBLE else View.GONE
        binding.pbLoading.toGone()
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<RecipesItem>) {
        navigateEvent.getContentIfNotHandled()?.let {
            val nextScreenIntent = Intent(requireActivity(), DetailsActivity::class.java).apply {
                putExtra(RECIPE_ITEM_KEY, it)
            }
            startActivity(nextScreenIntent)
        }
    }
}