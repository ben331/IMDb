package tech.benhack.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val trailerxShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

val movieShape = AbsoluteRoundedCornerShape(
    topLeft = 0.dp,
    topRight = 0.dp,
    bottomLeft = 16.dp,
    bottomRight = 16.dp
)