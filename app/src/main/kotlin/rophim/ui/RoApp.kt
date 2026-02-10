package rophim.ui


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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.manutd.ronaldo.api.HomeNavKey
import com.manutd.ronaldo.designsystem.component.RoBackground
import com.manutd.ronaldo.designsystem.component.RoTopAppBar
import com.manutd.ronaldo.designsystem.component.RoNavigationSuiteScaffold
import com.manutd.ronaldo.designsystem.theme.LightAndroidBackgroundTheme
import com.manutd.ronaldo.designsystem.theme.LightDefaultColorScheme
import com.manutd.ronaldo.impl.navigation.homeEntry
import com.manutd.ronaldo.navigation.Navigator
import com.manutd.ronaldo.navigation.toEntries
import rophim.navigation.TOP_LEVEL_NAV_ITEMS


@Composable
fun RoApp(
    appState: RoAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val shouldShowGradientBackground = appState.navigationState.currentTopLevelKey == HomeNavKey
    var showMessageDialog by rememberSaveable { mutableStateOf(false) }

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
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            RoApp(
                appState = appState,
                modifier = modifier,
                windowAdaptiveInfo = windowAdaptiveInfo,
                onLogoClick = {
                    showMessageDialog = true
                },
                onMessageDismissed = {
                    showMessageDialog = false
                },
                showMessageDialog = showMessageDialog
            )
        }
    }
}

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState state should be initialized at runtime")
}

@Composable
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
)
internal fun RoApp(
    appState: RoAppState,
    modifier: Modifier = Modifier,
    showMessageDialog: Boolean,
    onLogoClick: () -> Unit,
    onMessageDismissed: () -> Unit,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val snackbarHostState = LocalSnackbarHostState.current

    val navigator = remember { Navigator(appState.navigationState) }
    if (showMessageDialog) {
        //todo:show message dialog and dismiss

    }
    RoNavigationSuiteScaffold(
        navigationSuiteItems = {
            TOP_LEVEL_NAV_ITEMS.forEach { (navKey, navItem) ->
                // check if has any message
                // val hasUnread = unreadNavKeys.contains(navKey)
                val selected = navKey == appState.navigationState.currentTopLevelKey
                item(
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
                    label = { Text(stringResource(navItem.iconTextId)) },
                )
            }
        },
        windowAdaptiveInfo = windowAdaptiveInfo,
    ) {
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
                        WindowInsets.safeDrawing.exclude(
                            WindowInsets.ime,
                        ),
                    ),
                )
            },
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                Box(
                    modifier = Modifier.systemBarsPadding().consumeWindowInsets(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    ),
                ) {
                    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

                    val entryProvider = entryProvider {
                        homeEntry(navigator)
                    }

                    NavDisplay(
                        entries = appState.navigationState.toEntries(entryProvider),
                        sceneStrategy = listDetailStrategy,
                        onBack = { navigator.goBack() },
                    )
                }

                // TODO: We may want to add padding or spacer when the snackbar is shown so that
                //  content doesn't display behind it.
            }
        }
    }
}


