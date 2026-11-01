package org.insbaixcamp.fitnessapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDesplegable(
    label: String,
    opcions: List<String>,
    valorSeleccionat: String,
    onValorSeleccionat: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var desplegat by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = desplegat,
        onExpandedChange = { desplegat = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = valorSeleccionat,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = desplegat
                )
            },
            modifier = Modifier
                .menuAnchor(
                    ExposedDropdownMenuAnchorType.PrimaryNotEditable
                )
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = desplegat,
            onDismissRequest = { desplegat = false }
        ) {
            opcions.forEach { opcio ->
                DropdownMenuItem(
                    text = { Text(opcio) },
                    onClick = {

                        onValorSeleccionat(opcio)


                        desplegat = false
                    }
                )
            }
        }
    }
}