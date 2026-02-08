package rophim.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.manutd.ronaldo.api.HomeNavKey
import com.manutd.ronaldo.navigation.NavigationState
import com.manutd.ronaldo.navigation.rememberNavigationState
import com.manutd.rophim.core.data.utils.NetworkMonitor
import rophim.navigation.TOP_LEVEL_NAV_ITEMS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberRoAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    networkMonitor: NetworkMonitor,
): RoAppState {
    val navigationState = rememberNavigationState(HomeNavKey, TOP_LEVEL_NAV_ITEMS.keys)


    return remember(
        navigationState,
        coroutineScope,
    ) {
        RoAppState(
            navigationState = navigationState,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor
        )
    }
}

@Stable
class RoAppState(
    val navigationState: NavigationState,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val isOffline = networkMonitor.isOnline.map(Boolean::not)
        .stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

}

