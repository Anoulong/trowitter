package com.anou.trowitter.navigation

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.anou.prototype.core.db.ModuleEntity
import com.anou.prototype.core.db.category.CategoryEntity
import com.anou.prototype.core.db.feature.FeatureEntity
import com.anou.trowitter.R
import com.anou.trowitter.ui.MainActivity
import com.anou.trowitter.utils.Constants

class MainRouter {

    fun onModuleSelected(mainActivity: MainActivity, module: ModuleEntity, isLaunchModule: Boolean) {
        val navBuilder = NavOptions.Builder()
        val navOptions = if (isLaunchModule) navBuilder.setPopUpTo(R.id.loadingFragmentDestination, true).build() else null

        var bundle = Bundle()

        module.let {
            bundle.putString(Constants.MODULE_EID, module.eid)
            bundle.putString(Constants.MODULE_TITLE, module.title)

            when (module.type) {
                ModuleEntity.FAQ -> {
//                    Navigation.findNavController(mainActivity, R.id.mainNavigationHost).navigate(R.id.categoryFragmentDestination, bundle, navOptions)
                }
                ModuleEntity.ABOUT -> {
                    Navigation.findNavController(mainActivity, R.id.mainNavigationHost).navigate(R.id.aboutFragmentDestination, bundle, navOptions)
                }
                ModuleEntity.TEXT_TYPE -> {
//                    Navigation.findNavController(mainActivity, R.id.mainNavigationHost).navigate(R.id.textFragmentDestination, bundle, navOptions)
                }
                else -> Toast.makeText(mainActivity, module.title, Toast.LENGTH_SHORT).show()
            }
        }

        mainActivity.closeDrawer()
    }

    fun onCategorySelected(mainActivity: MainActivity, category: CategoryEntity) {
        var bundle = Bundle()
        category.let {
            bundle.putString(Constants.CATEGORY_EID, category.eid)
            bundle.putString(Constants.CATEGORY_TITLE, category.title)

//            Navigation.findNavController(mainActivity, R.id.mainNavigationHost).navigate(R.id.featureFragmentDestination, bundle)
        }
        Toast.makeText(mainActivity, category.title, Toast.LENGTH_SHORT).show()
    }

    fun onFeatureSelected(mainActivity: MainActivity, featureEntity: FeatureEntity) {
        Toast.makeText(mainActivity, featureEntity.eid, Toast.LENGTH_SHORT).show()
    }

     fun onFragmentViewed(mainActivity: MainActivity,string: String) {
        println("Log state ==> $string")
        mainActivity.supportActionBar?.title = string
    }
}