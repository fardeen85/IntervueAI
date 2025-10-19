package com.fardeen.intervueai

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

/*
@Composable
fun rememberWindowInfo(): WindowInfo{

    val configuration = LocalConfiguration.current
    return WindowInfo(
        screenWidthInfo = when{
            configuration.screenWidthDp < 600 -> WindowType.Compact
            configuration.screenWidthDp < 840 -> WindowType.Medium
            else -> WindowType.Expanded
        },

        screenHeightInfo = when{
            configuration.screenHeightDp < 480 -> WindowType.Compact
            configuration.screenHeightDp < 900 -> WindowType.Medium
            else -> WindowType.Expanded
        },
        screenWidth = configuration.screenWidthDp,
        screenHeight = configuration.screenHeightDp
    )

}

data class WindowInfo(

    val screenWidthInfo:WindowType,
    val screenHeightInfo:WindowType,
    val screenWidth:Int,
    val screenHeight:Int
)

sealed class WindowType{
    object Compact : WindowType()
    object Medium : WindowType()
    object Expanded : WindowType()
}


@Composable
fun mockWindowInfo(windowType: WindowType = WindowType.Compact): WindowInfo {
    return WindowInfo(
        screenWidthInfo = windowType,
        screenHeightInfo = windowType,
        screenWidth = when (windowType) {
            WindowType.Compact -> 320
            WindowType.Medium -> 720
            WindowType.Expanded -> 1024
        },
        screenHeight = when (windowType) {
            WindowType.Compact -> 480
            WindowType.Medium -> 900
            WindowType.Expanded -> 1200
        }
    )
}
*/
