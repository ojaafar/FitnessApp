package org.insbaixcamp.fitnessapp.ui.components

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable
fun AvatarPerfil(
    nom: String,
    fotoUri: String?,
    mida: Dp = 110.dp
) {
    val context = LocalContext.current

    val fotografia by produceState<ImageBitmap?>(
        initialValue = null,
        key1 = fotoUri
    ) {
        value = null

        if (fotoUri != null) {
            value = withContext(Dispatchers.IO) {
                carregarFoto(
                    resolver = context.contentResolver,
                    ruta = fotoUri
                )
            }
        }
    }

    val foto = fotografia

    if (fotoUri != null && foto != null) {
        Image(
            bitmap = foto,
            contentDescription = "Foto de perfil de $nom",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(mida)
                .clip(CircleShape)
        )
    } else {

        val inicials = nom.trim()
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.take(1).uppercase() }
            .ifBlank { "?" }

        Box(
            modifier = Modifier
                .size(mida)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = inicials,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun DadaPerfil(
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = valor,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TargetaPerfil(
    titol: String,
    valor: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = titol,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = valor,
                modifier = Modifier.padding(18.dp)
            )
        }
    }
}

@Composable
fun CampPerfil(
    etiqueta: String,
    valor: String,
    canviar: (String) -> Unit,
    teclat: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = canviar,
        label = { Text(etiqueta) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = teclat
        ),
        modifier = Modifier.fillMaxWidth()
    )
}


private fun carregarFoto(
    resolver: ContentResolver,
    ruta: String
): ImageBitmap? {
    return try {
        val uri = Uri.parse(ruta)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            val origen = ImageDecoder.createSource(resolver, uri)

            ImageDecoder.decodeBitmap(origen) { decoder, info, _ ->
                val costatMajor = maxOf(
                    info.size.width,
                    info.size.height
                )

                val mostra = (costatMajor + 1023) / 1024
                decoder.setTargetSampleSize(mostra.coerceAtLeast(1))
            }.asImageBitmap()
        } else {

            val opcions = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            resolver.openInputStream(uri)?.use { entrada ->
                BitmapFactory.decodeStream(entrada, null, opcions)
            }

            if (opcions.outWidth <= 0 || opcions.outHeight <= 0) {
                return null
            }

            var mostra = 1

            while (
                opcions.outWidth / mostra > 1024 ||
                opcions.outHeight / mostra > 1024
            ) {
                mostra *= 2
            }

            opcions.inJustDecodeBounds = false
            opcions.inSampleSize = mostra

            resolver.openInputStream(uri)?.use { entrada ->
                BitmapFactory.decodeStream(
                    entrada,
                    null,
                    opcions
                )?.asImageBitmap()
            }
        }
    } catch (_: IOException) {
        null
    } catch (_: SecurityException) {
        null
    } catch (_: IllegalArgumentException) {
        null
    }
}