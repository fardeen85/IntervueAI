package com.fardeen.intervueai

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow



sealed interface UiEvent {

    // 👉 Snackbar-related events
    sealed class Snackbar : UiEvent {
        data class Show(val message: String) : Snackbar()
    }

    // 👉 Navigation-related events
    sealed class Navigation : UiEvent {
        data class To(val route: String) : Navigation()
        object Back : Navigation()
    }

    // 👉 Dialog-related events
    sealed class Dialog : UiEvent {
        object ShowLoading : Dialog()
        object HideLoading : Dialog()
        object ShowSuccess : Dialog()
        object Hide : Dialog()
    }

    // 👉 Toasts (if you use them)
    data class Toast(val message: String) : UiEvent
}

