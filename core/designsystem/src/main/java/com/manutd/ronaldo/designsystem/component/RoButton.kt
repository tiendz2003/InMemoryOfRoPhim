package com.manutd.ronaldo.designsystem.component

import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector? = null,
    gradient: Brush? = null, // Truyền Gradient vào đây
    containerColor: Color = MaterialTheme.colorScheme.primary, // Màu mặc định nếu không có gradient
    contentColor: Color = Color.Black,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    val buttonColors = ButtonDefaults.buttonColors(
        // MẸO: Nếu có gradient, ta set nền button trong suốt để lớp gradient bên dưới hiện lên
        containerColor = if (gradient != null) Color.Transparent else containerColor,
        contentColor = contentColor
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .height(42.dp)
            // Nếu có gradient -> vẽ background gradient và cắt theo shape
            // Nếu không -> để nguyên (Button tự tô màu theo containerColor)
            .then(
                gradient?.let {
                    Modifier.background(brush = gradient, shape = shape)
                } ?: run {
                    Modifier.background(
                        color = containerColor,
                        shape = shape
                    )
                }
            ),
        colors = buttonColors,
        shape = shape,
        contentPadding = PaddingValues(horizontal = 16.dp) // Reset padding để tránh bị đè
    ) {
        // --- CONTENT (Icon + Text) ---
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}