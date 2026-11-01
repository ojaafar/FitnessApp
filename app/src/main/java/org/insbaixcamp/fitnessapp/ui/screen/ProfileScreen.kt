package org.insbaixcamp.fitnessapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.insbaixcamp.fitnessapp.ui.components.AvatarPerfil
import org.insbaixcamp.fitnessapp.ui.components.DadaPerfil
import org.insbaixcamp.fitnessapp.ui.components.TargetaPerfil
import java.util.Calendar
import java.util.Locale

@Composable
fun ProfileScreen(
    nom: String,
    email: String,
    dataNaixement: String,
    genere: String,
    alçada: Int,
    pes: Double,
    pesObjectiu: Double,
    objectius: String,
    nivellActivitat: String,
    fotoUri: String?,
    editar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Perfil",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            TextButton(onClick = editar) {
                Text("Editar")
            }
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AvatarPerfil(
                nom = nom,
                fotoUri = fotoUri,
                mida = 120.dp
            )

            Text(
                text = nom,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = email,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }


        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                DadaPerfil(
                    etiqueta = "Edat",
                    valor = calcularEdat(dataNaixement)?.toString() ?: "—",
                    modifier = Modifier.weight(1f)
                )

                DadaPerfil(
                    etiqueta = "Gènere",
                    valor = genere,
                    modifier = Modifier.weight(1f)
                )

                DadaPerfil(
                    etiqueta = "Alçada",
                    valor = "$alçada cm",
                    modifier = Modifier.weight(1f)
                )

                DadaPerfil(
                    etiqueta = "Pes",
                    valor = String.format(
                        Locale.getDefault(),
                        "%.1f kg",
                        pes
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        TargetaPerfil(
            titol = "Objectiu principal",
            valor = objectius
        )

        TargetaPerfil(
            titol = "Nivell d'activitat",
            valor = nivellActivitat
        )

        TargetaPerfil(
            titol = "Pes objectiu",
            valor = String.format(
                Locale.getDefault(),
                "%.1f kg",
                pesObjectiu
            )
        )
    }
}


fun calcularEdat(dataNaixement: String): Int? {

    val parts = dataNaixement.trim().split("/")

    if (parts.size != 3) return null

    val dia = parts[0].toIntOrNull() ?: return null
    val mes = parts[1].toIntOrNull() ?: return null
    val any = parts[2].toIntOrNull() ?: return null

    if (any <= 0) return null

    val avui = Calendar.getInstance()

    val naixement = Calendar.getInstance().apply {
        clear()
        isLenient = false
        set(any, mes - 1, dia)
    }


    try {
        if (naixement.timeInMillis > avui.timeInMillis) {
            return null
        }
    } catch (_: IllegalArgumentException) {
        return null
    }

    var edat = avui.get(Calendar.YEAR) - any

    val mesActual = avui.get(Calendar.MONTH) + 1
    val diaActual = avui.get(Calendar.DAY_OF_MONTH)


    if (mesActual < mes || (mesActual == mes && diaActual < dia)) {
        edat--
    }

    return edat
}