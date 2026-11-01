# FitnessApp · guía para continuar tu actividad

Esta versión parte de tu proyecto y completa la consulta, edición, guardado y cancelación del perfil. El código incluye comentarios en catalán; esta guía explica los cambios en castellano.

## 1. Abrir el proyecto

1. Descomprime el ZIP en una carpeta nueva para poder compararlo con tu versión anterior.
2. En Android Studio, pulsa **Open** y selecciona la carpeta `FitnessApp` que contiene `settings.gradle.kts`.
3. Espera a que termine la sincronización de Gradle. Se conserva la configuración de versiones de tu proyecto.
4. Selecciona un emulador o un móvil con API 24 o superior y ejecuta **app** con **Run**.

El ZIP contiene el código y los archivos de Gradle. Las carpetas de compilación, las cachés y `local.properties` se regeneran en tu ordenador. No contiene un APK nuevo compilado.

## 2. Qué había y qué faltaba

| Parte | En tu proyecto recibido | Cambio realizado |
| --- | --- | --- |
| Pantallas | Existían `ProfileScreen` y `EditProfileScreen`. | Se conservan sus funciones y se completa su funcionamiento. |
| Inicio | Abría directamente el editor con tus datos. | Abre el perfil con los datos iniciales de Laia Soler. |
| Guardado | El callback solo cambiaba de pantalla; el formulario no tenía botón de guardar. | El formulario devuelve un perfil actualizado a `FitnessApp`. |
| Cancelación | No había botón ni acción de cancelar. | Cancelar y Enrere descartan el borrador. |
| Números | Cada pulsación ejecutaba `toInt()` o `toDouble()`. | Se editan como texto y se validan antes de guardar. |
| Selectores | El menú de género ya estaba creado. | Se reutiliza `MenuDesplegable` para género, objetivo y actividad. |
| Fotografía | Los botones no tenían acciones. | Se puede seleccionar o quitar una fotografía. |
| Distribución | Sin desplazamiento ni uso de `innerPadding`; campos anchos en una fila. | Se añaden scroll, margen para las barras del sistema y campos con anchuras repartidas. |
| Paquete Kotlin | El editor usaba `org.indbaixcamp`. | Se unifica en `org.insbaixcamp.fitnessapp`. |

## 3. Orden recomendado para entender el código

Las rutas siguientes parten de `app/src/main/java/org/insbaixcamp/fitnessapp/`.

| Orden | Archivo | Qué debes mirar |
| --- | --- | --- |
| 1 | `model/PerfilUsuari.kt` | La `data class` agrupa los datos; los valores por defecto son los de Laia. |
| 2 | `MainActivity.kt` | `perfil` contiene los datos confirmados; `editant` decide la pantalla. |
| 3 | `ui/screen/ProfileScreen.kt` | Recibe el perfil y muestra sus datos. Editar cambia de pantalla. |
| 4 | `ui/screen/EditProfileScreen.kt` | Tiene una copia temporal de cada campo, valida y devuelve los cambios. |
| 5 | `ui/components/ComponentsPerfil.kt` | Avatar, datos, tarjetas y campos de texto reutilizables. |
| 6 | `ui/components/MenuDesplegable.kt` | Tu desplegable reutilizado para tres campos. |

`ui/theme/Theme.kt` aplica colores verdes próximos al ejemplo y mantiene un tema oscuro.

## 4. La idea central: guardar frente a cancelar

En `FitnessApp`, `perfil` es el estado confirmado y pertenece a la pantalla que coordina las otras dos:

```kotlin
var perfil by remember { mutableStateOf(PerfilUsuari()) }
var editant by remember { mutableStateOf(false) }
```

El editor crea estados temporales. Por ejemplo:

```kotlin
var nomNou by remember(perfil) { mutableStateOf(perfil.nom) }
```

Escribir en `nomNou` no modifica `perfil.nom`. Por eso puedes cancelar sin perder los datos anteriores.

Al guardar, el editor utiliza `perfil.copy(...)` y lo envía mediante `guardar`. En `MainActivity.kt`, esta función hace:

```kotlin
guardar = { perfilActualitzat ->
    perfil = perfilActualitzat
    editant = false
}
```

Al cancelar, únicamente se ejecuta `editant = false`. El editor sale de la composición y sus estados temporales se descartan. La siguiente vez que se abre, se inicializa con el último perfil confirmado.

## 5. Por qué los números se editan como texto

Si escribes `it.toDouble()` dentro de `onValueChange`, borrar un campo intenta convertir `""` en un número y provoca una excepción.

Ahora `pesText` guarda lo que estás escribiendo. La función `llegirDecimalPositiu()` lo convierte con `toDoubleOrNull()` y acepta tanto `62.5` como `62,5`. Si el contenido es inválido, muestra un mensaje y mantiene abierto el formulario.

También se comprueban el nombre, el correo, una fecha real no futura y los valores físicos positivos. No se modifica el perfil si hay errores.

## 6. Fotografía y datos iniciales

- El perfil inicial es Laia Soler, 165 cm y 60 kg. Los demás campos siguen la referencia visual más completa.
- La fecha inicial es `14/05/1999`. La edad se calcula con la fecha del dispositivo: no está fijada a los 25 años que aparecen en la imagen de ejemplo.
- Al iniciar aparece un avatar con las iniciales **LS**, porque el ZIP recibido no incluye una fotografía de Laia.
- En Editar, **Canviar foto** abre el selector de imágenes del sistema. Puedes elegir una imagen del dispositivo.
- **Eliminar foto** restaura el avatar de iniciales y se desactiva si no hay fotografía.
- La fotografía también forma parte del borrador: para confirmar su selección o eliminación hay que guardar. Cancelar conserva la fotografía anterior.
- El componente de avatar reduce el tamaño de lectura y carga la imagen fuera del hilo de la interfaz. Esta parte es más avanzada y puedes revisarla después de comprender el guardado.

## 7. Comprobar la lista del profesor

| Prueba | Pasos | Resultado esperado |
| --- | --- | --- |
| Inicio | Abre la aplicación. | Aparecen Laia Soler, 165 cm, 60 kg y el botón Editar. |
| Guardar | Editar → nombre `Laia Actualitzada`, peso `62,5`, objetivo `Millorar la salut` → Guardar canvis. | Se vuelve al perfil con los tres valores nuevos. |
| Reabrir | Pulsa Editar otra vez. | El formulario contiene lo último que has guardado. |
| Cancelar | Cambia el nombre por `NO GUARDAR`, el peso por `99` y el objetivo → Cancel·lar. | El perfil conserva Laia Actualitzada, 62,5 kg y Millorar la salut. |
| Campo vacío | Editar → borra el peso → Guardar canvis. | La app sigue abierta y pide un peso positivo. |
| Foto | Selecciona una foto, guarda y vuelve a editar. Elimínala y cancela. | El perfil conserva la foto guardada. |
| Enrere | Modifica un campo y pulsa Enrere. | Se vuelve al perfil sin aplicar ese cambio. |

Con el teclado abierto, el primer gesto de atrás del sistema puede cerrar solo el teclado. El botón Enrere de la pantalla siempre cancela la edición.

Se incluyen tres pruebas instrumentadas en `app/src/androidTest/java/org/insbaixcamp/fitnessapp/PerfilFlowTest.kt`. Comprueban inicio, guardado/cancelación y peso vacío. Para ejecutarlas, abre ese archivo en Android Studio y ejecuta la clase con un emulador o móvil conectado.

## 8. Alcance y verificación

Los datos se guardan únicamente en memoria mediante `remember`, como se trabaja en esta actividad. No hay Firebase ni base de datos. Al recrear la actividad (por ejemplo, al girar el dispositivo) o reiniciar el proceso, pueden volver los datos iniciales. Conservarlos en esos casos sería una ampliación posterior.

En esta revisión se han comprobado los archivos, los paquetes, las referencias internas y el flujo del código. **No se ha compilado ni ejecutado la app ni las pruebas instrumentadas aquí**, porque este entorno no dispone de SDK/emulador Android ni de un entorno Java funcional. La comprobación final se debe realizar en tu Android Studio con la tabla anterior.

Referencias de Android Developers utilizadas: [estado en Compose](https://developer.android.com/develop/ui/compose/state), [selector de fotografías](https://developer.android.com/training/data-storage/shared/photo-picker) y [campos de texto](https://developer.android.com/develop/ui/compose/text/user-input).
