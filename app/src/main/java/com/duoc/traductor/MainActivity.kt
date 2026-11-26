package com.duoc.traductor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.duoc.traductor.ui.navigation.AppNavGraph
import com.duoc.traductor.ui.theme.TucoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TucoTheme {
                Surface { AppNavGraph() }
            }
        }
    }
}
