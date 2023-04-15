package com.aminul.companion.appTheme

import kotlinx.coroutines.flow.StateFlow

enum class AppTheme {
    AUTO,
    LIGHT,
    DARK;

    companion object {
        fun fromOrdinal(ordinal: Int) = values()[ordinal]
    }
}

interface ThemeSetting {
    val themeStream: StateFlow<AppTheme>
    var theme: AppTheme
}