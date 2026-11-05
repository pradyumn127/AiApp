package com.example.aiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.aiapp.ui.navigation.ProductCatalogueNavGraph
import com.example.aiapp.ui.theme.AiAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ProductCatalogueNavGraph()
                }
            }
        }
    }
}
