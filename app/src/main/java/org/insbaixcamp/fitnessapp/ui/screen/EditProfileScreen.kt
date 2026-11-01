package org.insbaixcamp.fitnessapp.ui.screen

import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.insbaixcamp.fitnessapp.ui.components.AvatarPerfil
import org.insbaixcamp.fitnessapp.ui.components.CampPerfil
import org.insbaixcamp.fitnessapp.ui.components.MenuDesplegable

@Composable
fun EditProfileScreen(
    nom: String,
    email: String,
    dataNaixement: String,
    genere: String,
    alçada: Int,
    pes: Double,
    pesObjectiu: Double,
    objectiu: String,
    nivellActivitat: String,
    fotoUri: String?,

    guardar: (
        String, String, String, String,
        Int, Double, Double, String, String, String?
    ) -> Unit,

    cancelar: () -> Unit
) {

    var nomNou by remember { mutableStateOf(nom) }
    var emailNou by remember { mutableStateOf(email) }
    var dataNaixementNou by remember { mutableStateOf(dataNaixement) }
    var genereNou by remember { mutableStateOf(genere) }


    var alçadaNou by remember { mutableStateOf(alçada.toString()) }
    var pesNou by remember { mutableStateOf(pes.toString()) }
    var pesObjectiuNou by remember {
        mutableStateOf(pesObjectiu.toString())
    }

    var objectiuNou by remember { mutableStateOf(objectiu) }
    var nivellActivitatNou by remember {
        mutableStateOf(nivellActivitat)
    }

    var fotoNova by remember { mutableStateOf(fotoUri) }
    var missatgeError by remember { mutableStateOf("") }

    val teclat = LocalSoftwareKeyboardController.current


    val tornar: () -> Unit = {
        teclat?.hide()
        cancelar()
    }

    BackHandler(onBack = tornar)


    val selectorFoto = rememberLauncherForActivityResult(
        contract = PickVisualMedia()
    ) { uri ->

        if (uri != null) {
            fotoNova = uri.toString()
        }
    }

    val opcionsGenere = listOf(
        "Home",
        "Dona",
        "No binari"
    )

    val opcionsObjectiu = listOf(
        "Guanyar massa muscular",
        "Millorar la salut",
        "Mantenir el pes"
    )

    val opcionsActivitat = listOf(
        "Sedentari",
        "Lleuger",
        "Moderat",
        "Actiu",
        "Molt actiu"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(onClick = tornar) {
                Text("Enrere")
            }

            Text(
                text = "Editar perfil",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "Foto de perfil",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AvatarPerfil(
                nom = nomNou,
                fotoUri = fotoNova,
                mida = 90.dp
            )

            Column(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = {
                        teclat?.hide()

                        selectorFoto.launch(
                            PickVisualMediaRequest(
                                PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Canviar foto")
                }

                TextButton(
                    onClick = { fotoNova = null },
                    enabled = fotoNova != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar foto")
                }
            }
        }

        Text(
            text = "Dades personals",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        CampPerfil(
            etiqueta = "Nom complet",
            valor = nomNou,
            canviar = { nomNou = it }
        )

        CampPerfil(
            etiqueta = "Correu electrònic",
            valor = emailNou,
            canviar = { emailNou = it },
            teclat = KeyboardType.Email
        )

        CampPerfil(
            etiqueta = "Data de naixement (dd/mm/aaaa)",
            valor = dataNaixementNou,
            canviar = { dataNaixementNou = it }
        )

        MenuDesplegable(
            label = "Gènere",
            opcions = opcionsGenere,
            valorSeleccionat = genereNou,
            onValorSeleccionat = { genereNou = it },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Dades físiques",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        CampPerfil(
            etiqueta = "Alçada (cm)",
            valor = alçadaNou,
            canviar = { alçadaNou = it },
            teclat = KeyboardType.Number
        )

        CampPerfil(
            etiqueta = "Pes actual (kg)",
            valor = pesNou,
            canviar = { pesNou = it },
            teclat = KeyboardType.Decimal
        )

        CampPerfil(
            etiqueta = "Pes objectiu (kg)",
            valor = pesObjectiuNou,
            canviar = { pesObjectiuNou = it },
            teclat = KeyboardType.Decimal
        )

        MenuDesplegable(
            label = "Objectiu principal",
            opcions = opcionsObjectiu,
            valorSeleccionat = objectiuNou,
            onValorSeleccionat = { objectiuNou = it },
            modifier = Modifier.fillMaxWidth()
        )

        MenuDesplegable(
            label = "Nivell d'activitat",
            opcions = opcionsActivitat,
            valorSeleccionat = nivellActivitatNou,
            onValorSeleccionat = { nivellActivitatNou = it },
            modifier = Modifier.fillMaxWidth()
        )

        if (missatgeError.isNotEmpty()) {
            Text(
                text = missatgeError,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {

                val altura = alçadaNou.trim().toIntOrNull()

                val peso = pesNou.trim()
                    .replace(',', '.')
                    .toDoubleOrNull()

                val pesoObjetivo = pesObjectiuNou.trim()
                    .replace(',', '.')
                    .toDoubleOrNull()

                when {
                    nomNou.isBlank() -> {
                        missatgeError = "Escriu el nom."
                    }

                    !Patterns.EMAIL_ADDRESS
                        .matcher(emailNou.trim())
                        .matches() -> {
                        missatgeError = "Escriu un correu electrònic vàlid."
                    }

                    calcularEdat(dataNaixementNou) == null -> {
                        missatgeError =
                            "Escriu una data vàlida i no futura: dd/mm/aaaa."
                    }

                    altura == null || altura <= 0 -> {
                        missatgeError = "Introdueix una alçada vàlida."
                    }

                    peso == null || !peso.isFinite() || peso <= 0 -> {
                        missatgeError = "Introdueix un pes vàlid."
                    }

                    pesoObjetivo == null ||
                            !pesoObjetivo.isFinite() ||
                            pesoObjetivo <= 0 -> {
                        missatgeError = "Introdueix un pes objectiu vàlid."
                    }

                    else -> {
                        missatgeError = ""
                        teclat?.hide()


                        guardar(
                            nomNou.trim(),
                            emailNou.trim(),
                            dataNaixementNou.trim(),
                            genereNou,
                            altura,
                            peso,
                            pesoObjetivo,
                            objectiuNou,
                            nivellActivitatNou,
                            fotoNova
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar canvis")
        }

        OutlinedButton(
            onClick = tornar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel·lar")
        }
    }
}