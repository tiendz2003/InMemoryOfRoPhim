package rophim.ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.manutd.ronaldo.designsystem.component.RoBackground
import com.manutd.ronaldo.designsystem.component.RoNavigationBar
import com.manutd.ronaldo.designsystem.component.RoNavigationBarItem
import com.manutd.ronaldo.designsystem.component.RoNavigationSuiteScaffold
import com.manutd.ronaldo.designsystem.theme.LightAndroidBackgroundTheme
import com.manutd.ronaldo.impl.navigation.homeEntry
import com.manutd.ronaldo.impl.navigation.watchScreenEntry
import com.manutd.ronaldo.navigation.Navigator
import com.manutd.ronaldo.navigation.toEntries
import com.ronaldo.rophim.navigation.moviesDetailEntry
import rophim.navigation.TOP_LEVEL_NAV_ITEMS
import java.lang.System.exit


@Composable
fun RoApp(
    appState: RoAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {

    RoBackground(modifier = modifier) {
        val snackbarHostState = remember { SnackbarHostState() }
        val isOffline by appState.isOffline.collectAsStateWithLifecycle()

        // If user is not connected to the internet show a snack bar to inform them.
        val notConnectedMessage = "Không có internet"
        LaunchedEffect(isOffline) {
            if (isOffline) {
                snackbarHostState.showSnackbar(
                    message = notConnectedMessage,
                    duration = Indefinite,
                )
            }
        }
        SharedTransitionLayout {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                RoPhimApp(
                    appState = appState,
                    modifier = modifier,
                    windowAdaptiveInfo = windowAdaptiveInfo,
                )
            }
        }
    }
}

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState state should be initialized at runtime")
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
)
@Composable
internal fun RoPhimApp(
    appState: RoAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val snackbarHostState = LocalSnackbarHostState.current
    val shouldShowBottomBar =
        appState.navigationState.currentKey in appState.navigationState.topLevelKeys

    val navigator = remember { Navigator(appState.navigationState) }

    // Dùng 1 Scaffold duy nhất làm Root cho toàn bộ App
    Scaffold(
        modifier = modifier.semantics {
            testTagsAsResourceId = true
        },
        containerColor = LightAndroidBackgroundTheme.color,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.exclude(WindowInsets.ime),
                ),
            )
        },
        // Gắn BottomBar vào đây
        bottomBar = {
            RoBottomBar(
                appState = appState,
                navigator = navigator,
                shouldShowBottomBar = shouldShowBottomBar
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding) // Áp dụng padding động
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
                ),
        ) {
            Box(
                modifier = Modifier
                    .systemBarsPadding()
                    .consumeWindowInsets(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    ),
            ) {
                val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

                val entryProvider = entryProvider {
                    homeEntry(navigator)
                    moviesDetailEntry(navigator)
                    watchScreenEntry(navigator)
                }

                NavDisplay(
                    entries = appState.navigationState.toEntries(entryProvider),
                    sceneStrategy = listDetailStrategy,
                    onBack = { navigator.goBack() },
                    transitionSpec = {
                        fadeIn(tween(300)) + scaleIn(
                            initialScale = 0.95f,
                            animationSpec = tween(300)
                        ) togetherWith ExitTransition.None
                    },

                    popTransitionSpec = {
                        fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                    },

                    predictivePopTransitionSpec = {
                        fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                    },
                )
            }
        }
    }
}

@Composable
private fun RoBottomBar(
    appState: RoAppState,
    navigator: Navigator,
    shouldShowBottomBar: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = shouldShowBottomBar,
        // Trượt từ dưới lên mượt mà (YouTube style)
        enter = fadeIn(nonSpatialExpressiveSpring()) + slideInVertically(
            spatialExpressiveSpring(),
        ) {
            it
        },
        // Trượt tuột xuống dưới giấu đi
        exit = fadeOut(nonSpatialExpressiveSpring()) + slideOutVertically(
            spatialExpressiveSpring(),
        ) {
            it
        }
    ) {
        RoNavigationBar(
            modifier = modifier
        ) {
            TOP_LEVEL_NAV_ITEMS.forEach { (navKey, navItem) ->
                val selected = navKey == appState.navigationState.currentTopLevelKey
                RoNavigationBarItem(
                    selected = selected,
                    onClick = { navigator.navigate(navKey) },
                    icon = {
                        Icon(
                            imageVector = navItem.unselectedIcon,
                            contentDescription = null,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = navItem.selectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = { Text(stringResource(navItem.iconTextId)) }
                )
            }
        }
    }
}

fun <T> spatialExpressiveSpring() = spring<T>(
    dampingRatio = 0.8f,
    stiffness = 380f,
)

fun <T> nonSpatialExpressiveSpring() = spring<T>(
    dampingRatio = 1f,
    stiffness = 1600f,
)