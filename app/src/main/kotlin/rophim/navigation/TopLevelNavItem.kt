package rophim.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.manutd.ronaldo.api.HomeNavKey
import com.manutd.ronaldo.designsystem.icon.RoIcons
import com.manutd.rophim.R

data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
)

val HOME = TopLevelNavItem(
    selectedIcon = RoIcons.Upcoming,
    unselectedIcon = RoIcons.UpcomingBorder,
    iconTextId = R.string.home,
    titleTextId = R.string.home,
)

val SEARCH = TopLevelNavItem(
    selectedIcon = RoIcons.Bookmarks,
    unselectedIcon = RoIcons.BookmarksBorder,
    iconTextId = R.string.search,
    titleTextId = R.string.search,
)

val TOP_LEVEL_NAV_ITEMS = mapOf(
    HomeNavKey to HOME,
    //BookmarksNavKey to SEARCH,
)
