@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.manutd.ronaldo.impl.component

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.manutd.ronaldo.designsystem.component.PlainTooltipBox
import com.manutd.ronaldo.designsystem.component.RoIcon
import com.manutd.ronaldo.designsystem.icon.IconType
import com.manutd.ronaldo.feature.watchscreen.impl.R
import com.rophim.player.state.PlayPauseButtonState
import com.rophim.player.state.SeekButtonState

@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun CenterControls(
    playPauseButtonState: PlayPauseButtonState,
    seekButtonState: SeekButtonState,
    modifier: Modifier = Modifier
) {
    val iconSize = 30.dp
    val iconBgColor = Color.Black.copy(alpha = 0.3f)
    val (replaySeekIcon, forwardSeekIcon) = remember {
        when (seekButtonState.seekBackAmountMs) {
            5000L -> R.drawable.ic_backward_5 to R.drawable.ic_forward_5
            10000L -> R.drawable.ic_backward to R.drawable.ic_fast_forward
            else -> R.drawable.ic_backward_30 to R.drawable.ic_forward_30
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(60.dp)
    ) {
        PlainTooltipBox(
            description = stringResource(R.string.seek_backward),
        ) {
            IconButton(
                onClick = seekButtonState::onSeekBack,
                enabled = seekButtonState.isSeekBackEnabled,
                modifier = Modifier.background(
                    iconBgColor,
                    shape = CircleShape
                )
            ) {
                RoIcon(
                    icon = IconType.Drawable(replaySeekIcon),
                    contentDescription = stringResource(R.string.seek_backward),
                    dp = iconSize
                )
            }
        }

        AnimatedContent(
            targetState = playPauseButtonState.isBuffering,
            transitionSpec = {
                ContentTransform(
                    targetContentEnter = fadeIn(),
                    initialContentExit = fadeOut(),
                )
            }
        ){state->
            if (state) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(iconSize * 2.2f)
                        .background(
                            iconBgColor,
                            shape = CircleShape
                        )
                        .padding(5.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }else{
                PlainTooltipBox(
                    description = stringResource(R.string.play_pause),
                ) {
                    IconButton(
                        onClick = playPauseButtonState::onClick,
                        enabled = playPauseButtonState.isEnabled,
                        modifier = Modifier
                            .background(
                                iconBgColor,
                                shape = CircleShape
                            )
                            .padding(5.dp)
                    ){
                        AnimatedContent(
                            targetState = playPauseButtonState.showPlay,
                            transitionSpec = {
                                ContentTransform(
                                    targetContentEnter = fadeIn(),
                                    initialContentExit = fadeOut(),
                                )
                            }
                        ) {
                            val icon = if (it) {
                                R.drawable.ic_player_play
                            } else {
                                R.drawable.ic_player_paused
                            }

                            RoIcon(
                                icon = IconType.Drawable(icon),
                                contentDescription = stringResource(R.string.play_pause),
                                dp = iconSize * 2.5f,
                            )
                        }
                    }
                }
            }

        }

        PlainTooltipBox(
            description = stringResource(R.string.seek_forward),
        ) {
            IconButton(
                onClick = seekButtonState::onSeekForward,
                enabled = seekButtonState.isSeekForwardEnabled,
                modifier = Modifier.background(
                    iconBgColor,
                    shape = CircleShape
                )
            ) {
                RoIcon(
                    icon = IconType.Drawable(forwardSeekIcon),
                    contentDescription = stringResource(R.string.seek_forward),
                    dp = iconSize
                )
            }
        }
    }
}