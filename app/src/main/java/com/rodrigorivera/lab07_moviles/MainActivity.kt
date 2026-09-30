package com.rodrigorivera.lab07_moviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.rodrigorivera.lab07_moviles.navigation.AppNavHost
import com.rodrigorivera.lab07_moviles.ui.theme.Lab07_movilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab07_movilesTheme {
                Surface {
                    AppNavHost()
                }
            }
        }
    }
}