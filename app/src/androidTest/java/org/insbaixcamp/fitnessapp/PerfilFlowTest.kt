package org.insbaixcamp.fitnessapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

// S'executa en un emulador o dispositiu, des d'Android Studio.
@RunWith(AndroidJUnit4::class)
class PerfilFlowTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun mostraLaiaEnObrir() {
        compose.onNodeWithText("Laia Soler").assertIsDisplayed()
        compose.onNodeWithTag("editar").assertIsDisplayed()
    }

    @Test
    fun guardarActualitzaICancelarConservaElPerfil() {
        compose.onNodeWithTag("editar").performClick()
        compose.onNodeWithTag("nom").performScrollTo().performTextReplacement("Laia Actualitzada")
        compose.onNodeWithTag("pes").performScrollTo().performTextReplacement("62,5")
        compose.onNodeWithText("Guanyar massa muscular").performScrollTo().performClick()
        compose.onNodeWithText("Millorar la salut").performClick()
        compose.onNodeWithTag("guardar").performScrollTo().performClick()

        compose.onNodeWithText("Laia Actualitzada").assertIsDisplayed()
        compose.onNodeWithText(String.format(Locale.getDefault(), "%.1f kg", 62.5)).assertIsDisplayed()
        compose.onNodeWithText("Millorar la salut").performScrollTo().assertIsDisplayed()

        // Tornem a editar: el formulari ha de recuperar l'últim perfil guardat.
        compose.onNodeWithTag("editar").performScrollTo().performClick()
        compose.onNodeWithTag("nom").performScrollTo().assertTextContains("Laia Actualitzada")
        compose.onNodeWithTag("pes").performScrollTo().assertTextContains("62.5")
        compose.onNodeWithTag("nom").performScrollTo().performTextReplacement("NO GUARDAR")
        compose.onNodeWithTag("pes").performScrollTo().performTextReplacement("99")
        compose.onNodeWithText("Millorar la salut").performScrollTo().performClick()
        compose.onNodeWithText("Perdre pes").performClick()
        compose.onNodeWithTag("cancelar").performScrollTo().performClick()

        compose.onNodeWithText("Laia Actualitzada").assertIsDisplayed()
        compose.onNodeWithText(String.format(Locale.getDefault(), "%.1f kg", 62.5)).assertIsDisplayed()
        compose.onNodeWithText("Millorar la salut").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun esPotBuidarElPesSenseTancarLAplicacio() {
        compose.onNodeWithTag("editar").performClick()
        compose.onNodeWithTag("pes").performScrollTo().performTextReplacement("")
        compose.onNodeWithTag("guardar").performScrollTo().performClick()
        compose.onNodeWithText("Revisa els camps marcats abans de guardar.").assertIsDisplayed()
        compose.onNodeWithTag("pes").performScrollTo()
        compose.onNodeWithText("Escriu un pes positiu.").assertIsDisplayed()
    }
}
