package com.fardeen.intervueai.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.fardeen.intervueai.HomeScreenRoot
import com.fardeen.intervueai.SelectTopicScreenRoot
import com.fardeen.intervueai.SelectTopicViewModel
import com.fardeen.intervueai.createchatMeta.presentation.CreateChatMetaScreenRoot
import com.fardeen.intervueai.interviwChatRootScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun mainNavigation() {

    val backstack = rememberSaveable { mutableStateListOf<NavigationRoutes>(Home) }
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
                scaleIn(initialScale = 0.7f),
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


                HomeScreenRoot {
                    backstack.add(SelectTopic)
                }

            }

            entry<SelectTopic> { entry ->
                SelectTopicScreenRoot() {
                    backstack.add(createChatMeta)
                }
            }

            entry<createChatMeta>{entry->
                CreateChatMetaScreenRoot(){
                    backstack.add(chatScreen)
                }
            }

            entry<chatScreen>{entry->
                interviwChatRootScreen()
            }


        }


    )


}