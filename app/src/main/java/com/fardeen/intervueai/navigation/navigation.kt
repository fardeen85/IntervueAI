package com.fardeen.intervueai.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.fardeen.intervueai.HomeScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun mainNavigation() {

    val backstack = remember { mutableStateListOf<NavigationRoutes>(Home) }
    var showSplash by remember { mutableStateOf(false) }
    val motionScheme = MaterialTheme.motionScheme

    NavDisplay(
        backStack = backstack,
        onBack = { backstack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()),
                fadeOut(motionScheme.defaultEffectsSpec()),
            )
        },
        popTransitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()),
                scaleOut(targetScale = 0.7f)
            )
        },
        entryProvider = entryProvider {

            entry<Home> { entry ->

                showSplash = false
                HomeScreen {}

            }
        }
    )


}