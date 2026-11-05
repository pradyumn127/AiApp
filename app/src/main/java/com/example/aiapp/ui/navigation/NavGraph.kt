package com.example.aiapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aiapp.ui.screens.CategoryProductsScreen
import com.example.aiapp.ui.screens.EnquiryScreen
import com.example.aiapp.ui.screens.HomeScreen
import com.example.aiapp.ui.screens.ProductDetailScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CategoryProducts : Screen("category/{categoryName}") {
        fun createRoute(categoryName: String) = "category/$categoryName"
    }
    object ProductDetail : Screen("detail/{productId}") {
        fun createRoute(productId: Int) = "detail/$productId"
    }
    object Enquiry : Screen("enquiry/{productId}/{productTitle}") {
        fun createRoute(productId: Int, productTitle: String) = "enquiry/$productId/$productTitle"
    }
}

@Composable
fun ProductCatalogueNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onCategoryClick = { category ->
                    navController.navigate(Screen.CategoryProducts.createRoute(category))
                },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                }
            )
        }
        composable(
            route = Screen.CategoryProducts.route,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) {
            CategoryProductsScreen(
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                }
            )
        }
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            ProductDetailScreen(
                onBackClick = { navController.popBackStack() },
                onEnquireClick = { productId, productTitle ->
                    navController.navigate(Screen.Enquiry.createRoute(productId, productTitle))
                }
            )
        }
        composable(
            route = Screen.Enquiry.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType },
                navArgument("productTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            val productTitle = backStackEntry.arguments?.getString("productTitle") ?: "Product"
            EnquiryScreen(
                productId = productId,
                productTitle = productTitle,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
