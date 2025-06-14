package com.aiinty.copayment.presentation.ui._components.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.aiinty.copayment.R

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    localImageUri: Uri? = null,
    onClick: (() -> Unit)? = null,
) {
    val context = LocalContext.current

    val imageBitmap: Bitmap? = remember(localImageUri) {
        localImageUri?.let { uri ->
            try {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } catch (e: Exception) {
                null
            }
        }
    }

    Box(
        modifier = modifier
            .size(110.dp)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
    ) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    spotColor = Color.Black.copy(alpha = 0.5f)
                )
                .background(Color.White, shape = CircleShape)
                .padding(10.dp)
                .clip(CircleShape)
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                SubcomposeAsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    loading = {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(strokeWidth = 2.dp)
                        }
                    }
                )
            }
        }

        if (onClick != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset((-4).dp, (-4).dp)
                    .size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.edit),
                    tint = Color.Unspecified,
                    contentDescription = stringResource(R.string.edit_avatar)
                )
            }
        }
    }
}
