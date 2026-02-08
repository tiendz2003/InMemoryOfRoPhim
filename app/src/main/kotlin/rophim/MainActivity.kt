package rophim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.rophim.core.data.utils.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import rophim.ui.RoApp
import rophim.ui.rememberRoAppState
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberRoAppState(
                networkMonitor = networkMonitor
            )
            CompositionLocalProvider(
                //todo:Truyền các api sử dụng xuyên suốt app
            ) {
                RoTheme(
                    androidTheme = true,
                    disableDynamicTheming = true
                ) {
                    RoApp(appState)
                }
            }
        }
    }
}