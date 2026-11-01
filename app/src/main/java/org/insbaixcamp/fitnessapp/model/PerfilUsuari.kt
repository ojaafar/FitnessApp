package org.insbaixcamp.fitnessapp.model

import java.util.Calendar

// Agrupem totes les dades en un objecte per passar-les entre les pantalles.
// Les propietats són val: copy() permet crear un perfil actualitzat.
data class PerfilUsuari(
    val nom: String = "Laia Soler",
    val email: String = "laia.soler@exemple.com",
    val dataNaixement: String = "14/05/1999",
    val genere: String = "Dona",
    val alcada: Int = 165,
    val pes: Double = 60.0,
    val pesObjectiu: Double = 55.0,
    val objectiu: String = "Guanyar massa muscular",
    val nivellActivitat: String = "Moderat",
    val fotoUri: String? = null
)

// Admet decimals amb punt o coma. Un camp buit o incorrecte retorna null.
fun llegirDecimalPositiu(text: String): Double? {
    return text.trim().replace(',', '.').toDoubleOrNull()
        ?.takeIf { it.isFinite() && it > 0.0 }
}

// Comprovem una data real en format dd/mm/aaaa, que no sigui futura.
fun llegirDataNaixement(text: String, avui: Calendar = Calendar.getInstance()): Calendar? {
    val valor = text.trim()
    if (!Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}").matches(valor)) return null
    val parts = valor.split('/')
    val dia = parts[0].toIntOrNull() ?: return null
    val mes = parts[1].toIntOrNull() ?: return null
    val any = parts[2].toIntOrNull() ?: return null
    if (any <= 0) return null

    return try {
        val data = Calendar.getInstance().apply {
            clear()
            isLenient = false // Rebutja, per exemple, el 31 de febrer.
            set(any, mes - 1, dia)
        }
        if (data.timeInMillis > avui.timeInMillis) null else data
    } catch (_: IllegalArgumentException) {
        null
    }
}

fun calcularEdat(dataNaixement: String, avui: Calendar = Calendar.getInstance()): Int? {
    val naixement = llegirDataNaixement(dataNaixement, avui) ?: return null
    var edat = avui.get(Calendar.YEAR) - naixement.get(Calendar.YEAR)
    val mesActual = avui.get(Calendar.MONTH)
    val mesNaixement = naixement.get(Calendar.MONTH)
    if (mesActual < mesNaixement ||
        (mesActual == mesNaixement &&
            avui.get(Calendar.DAY_OF_MONTH) < naixement.get(Calendar.DAY_OF_MONTH))) {
        edat--
    }
    return edat
}
