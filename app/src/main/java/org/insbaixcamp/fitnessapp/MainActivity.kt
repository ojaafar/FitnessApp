package org.insbaixcamp.fitnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.insbaixcamp.fitnessapp.ui.screen.EditProfileScreen
import org.insbaixcamp.fitnessapp.ui.screen.ProfileScreen
import org.insbaixcamp.fitnessapp.ui.theme.FitnessAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FitnessAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    ) {
                        FitnessApp()
                    }
                }
            }
        }
    }
}

@Composable
fun FitnessApp() {


    var nom by remember { mutableStateOf("Laia Soler") }
    var email by remember { mutableStateOf("laia.soler@exemple.com") }
    var dataNaixement by remember { mutableStateOf("14/05/1999") }
    var genere by remember { mutableStateOf("Dona") }

    var alçada by remember { mutableStateOf(165) }
    var pes by remember { mutableStateOf(60.0) }
    var pesObjectiu by remember { mutableStateOf(55.0) }

    var objectius by remember {
        mutableStateOf("Guanyar massa muscular")
    }

    var nivellActivitat by remember {
        mutableStateOf("Moderat")
    }


    var fotoUri by remember { mutableStateOf<String?>(null) }


    var editant by remember { mutableStateOf(false) }

    if (editant) {
        EditProfileScreen(
            nom = nom,
            email = email,
            dataNaixement = dataNaixement,
            genere = genere,
            alçada = alçada,
            pes = pes,
            pesObjectiu = pesObjectiu,
            objectiu = objectius,
            nivellActivitat = nivellActivitat,
            fotoUri = fotoUri,

            guardar = {
                    nouNom, nouEmail, novaData, nouGenere,
                    novaAlçada, nouPes, nouPesObjectiu,
                    nouObjectiu, nouNivell, novaFoto ->


                nom = nouNom
                email = nouEmail
                dataNaixement = novaData
                genere = nouGenere
                alçada = novaAlçada
                pes = nouPes
                pesObjectiu = nouPesObjectiu
                objectius = nouObjectiu
                nivellActivitat = nouNivell
                fotoUri = novaFoto


                editant = false
            },

            cancelar = {

                editant = false
            }
        )
    } else {
        ProfileScreen(
            nom = nom,
            email = email,
            dataNaixement = dataNaixement,
            genere = genere,
            alçada = alçada,
            pes = pes,
            pesObjectiu = pesObjectiu,
            objectius = objectius,
            nivellActivitat = nivellActivitat,
            fotoUri = fotoUri,
            editar = { editant = true }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FitnessPreview() {
    FitnessAppTheme {
        FitnessApp()
    }
}