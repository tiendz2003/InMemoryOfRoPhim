package com.manutd.ronaldo.impl.component

import android.R.attr.contentDescription
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manutd.ronaldo.designsystem.component.PlainTooltipBox
import com.manutd.ronaldo.designsystem.component.RoIcon
import com.manutd.ronaldo.designsystem.icon.IconType
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.ronaldo.designsystem.utils.AdaptiveTextStyle.asAdaptiveTextStyle
import com.manutd.ronaldo.feature.watchscreen.impl.R
import com.manutd.ronaldo.network.model.Episode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlayerTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    episode: Episode? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        PlainTooltipBox(
            description = stringResource(R.string.navigate_up)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(top = 5.dp, end = 5.dp)
                    .align(Alignment.CenterStart)
            ) {
                RoIcon(
                    icon = IconType.Drawable(com.manutd.rophim.core.designsystem.R.drawable.ic_back),
                    contentDescription = stringResource(R.string.navigate_up)
                )
            }
        }
        PlayerLabel(
            title = title,
            episode = episode,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun PlayerLabel(
    title: String,
    episode: Episode?,
    modifier: Modifier = Modifier
) {
    val titleStyle =
        MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp).asAdaptiveTextStyle()
    val randomSeason = remember {
        (1..10).random()
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (episode != null) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = titleStyle.toSpanStyle()) {
                        append("S${randomSeason} E${episode.episodeNumber}: ")
                    }

                    withStyle(
                        style = titleStyle.copy(
                            fontWeight = FontWeight.Light,
                            color = Color.White.copy(alpha = 0.8F),
                        ).toSpanStyle()
                    ) {
                        append(episode.title)
                    }
                },
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        } else {
            Text(
                text = title,
                style = titleStyle,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    PlainTooltipBox(
        description = contentDescription,
    ) {
        TextButton(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(vertical = 2.dp, horizontal = 12.dp),
            modifier = modifier
                .defaultMinSize(minWidth = 1.dp, minHeight = 30.dp)
        ) {
            RoIcon(
                icon = IconType.Drawable(icon),
                contentDescription = contentDescription
            )

            Text(
                text = contentDescription,
                style = LocalTextStyle.current.asAdaptiveTextStyle(),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun LabeledButtonPreview() {
    RoTheme() {
        LabeledButton(
            icon = com.manutd.rophim.core.designsystem.R.drawable.ic_back,
            contentDescription = "Back",
            onClick = {}
        )

    }
}