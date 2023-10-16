package com.task.ui.component.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.task.R
import com.task.RECIPE_ITEM_KEY
import com.task.data.dto.recipes.RecipesItem
import com.task.databinding.DetailsLayoutBinding
import com.task.ui.base.BaseActivity
import com.task.utils.observe
import com.task.utils.toGone
import com.task.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : BaseActivity() {

    private val viewModel: DetailsViewModel by viewModels()

    private lateinit var binding: DetailsLayoutBinding
    private var menu: Menu? = null

    override fun initViewBinding() {
        binding = DetailsLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initIntentData(intent.getParcelableExtra(RECIPE_ITEM_KEY) ?: RecipesItem())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        this.menu = menu
        viewModel.isFavourites()
        return true
    }

    fun onClickFavorite(mi: MenuItem) {
        mi.isCheckable = false
        if (viewModel.isFavourite.value == true) {
            viewModel.removeFromFavourites()
        } else {
            viewModel.addToFavourites()
        }
    }

    override fun observeViewModel() {
        observe(viewModel.recipeData, ::initializeView)
        observe(viewModel.isFavourite, ::handleIsFavourite)
        observe(viewModel.loading, ::loading)
        observe(viewModel.isError, ::showError)
    }

    private fun handleIsFavourite(isFavourite: Boolean) {
        handleIsFavouriteUI(isFavourite)
        menu?.findItem(R.id.add_to_favorite)?.isCheckable = true
        binding.pbLoading.toGone()
    }

    fun loading(isLoading: Boolean) {
        binding.pbLoading.toVisible()
    }

    private fun showError(errorCode: Int) {
        menu?.findItem(R.id.add_to_favorite)?.isCheckable = true
        binding.pbLoading.toGone()
        Toast.makeText(this, "Error Code: $errorCode", Toast.LENGTH_SHORT).show()
    }

    private fun handleIsFavouriteUI(isFavourite: Boolean) {
        menu?.let {
            it.findItem(R.id.add_to_favorite)?.icon =
                if (isFavourite) {
                    ContextCompat.getDrawable(this, R.drawable.ic_star_24)
                } else {
                    ContextCompat.getDrawable(this, R.drawable.ic_outline_star_border_24)
                }
        }
    }

    private fun initializeView(recipesItem: RecipesItem) {
        binding.tvName.text = recipesItem.name
        binding.tvHeadline.text = recipesItem.headline
        binding.tvDescription.text = recipesItem.description
        Picasso.get().load(recipesItem.image).placeholder(R.drawable.ic_healthy_food_small)
            .into(binding.ivRecipeImage)

    }
}
