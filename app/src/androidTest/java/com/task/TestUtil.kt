package com.task

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.task.data.dto.recipes.Recipes
import com.task.data.dto.recipes.RecipesItem
import java.io.InputStream

object TestUtil {
    var dataStatus: DataStatus = DataStatus.Success
    var recipes: Recipes = Recipes(arrayListOf())
    fun initData(): Recipes {
        val jsonString = getJson("RecipesApiResponse.json")
        val recipesList = GsonBuilder().create().fromJson<ArrayList<RecipesItem>>(jsonString, object : TypeToken<ArrayList<RecipesItem>>(){}.type)
        recipes = Recipes(recipesList)
        return recipes
    }

    private fun getJson(path: String): String {
        val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val inputStream: InputStream = ctx.classLoader.getResourceAsStream(path)
        return inputStream.bufferedReader().use { it.readText() }
    }
}
