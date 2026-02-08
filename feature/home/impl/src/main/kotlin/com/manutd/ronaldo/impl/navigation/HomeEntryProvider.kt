package com.manutd.ronaldo.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.manutd.ronaldo.api.HomeNavKey
import com.manutd.ronaldo.impl.screen.HomeScreen
import com.manutd.ronaldo.navigation.Navigator

fun EntryProviderScope<NavKey>.homeEntry(navigator: Navigator) {
    entry<HomeNavKey> {
        HomeScreen()
    }
}