package com.manutd.ronaldo.impl.screen

import android.R.attr.text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.manutd.ronaldo.impl.screen.item.ActorItem
import com.manutd.ronaldo.network.model.Actor
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ActorSection(
    title: String,
    actors: ImmutableList<Actor>,
    onActorClick: (Actor) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Text(
            modifier= Modifier.padding(start = 16.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Actors List
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = actors,
                key = { it.id }
            ) { actor ->
                ActorItem(
                    actor = actor,
                    onClick = { onActorClick(actor) }
                )
            }
        }
    }
}