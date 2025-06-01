package com.fardeen.intervueai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.fardeen.intervueai.navigation.mainNavigation
import com.fardeen.intervueai.transition.IntervueAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntervueAITheme {
                Scaffold{ innerPadding->

                    Column(modifier = Modifier.padding(innerPadding)) {

                        mainNavigation()
                    }

                }

            }
        }
    }
}

