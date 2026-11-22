# Laboratorio 10

Rama: `feature/room-lab10`. No se realizó push.

## Implementación

- `data/local/CharacterEntity.kt`, `LocationEntity.kt`: tablas independientes con IDs Int originales.
- `CharacterDao.kt`, `LocationDao.kt`: consultas Flow, inserción sin duplicados, actualización y eliminación; favoritos por ID.
- `AppDatabase.kt`, `Mappers.kt`: base Room compartida y conversión bidireccional sin perder campos.
- `data/Repositories.kt`: repositorios de modelos, contenedor singleton y coordinador de sesión.
- `login/SessionViewModel.kt`: estados de carga, error y finalización para login y logout.
- Modificados: Gradle de app (Room 2.8.4 y KSP 2.3.9), `Character` (isFavorite), `UserPreferences` (indicador persistente), cuatro ViewModels existentes, login, splash, perfil, lista/detalle de personajes, lista de ubicaciones y `AppNavHost`.
- Pruebas: `MapperTest` y `RoomSessionTest`.

Las únicas lecturas de `CharacterDb` y `LocationDb` desde la aplicación están en la sincronización. La UI recibe los modelos originales a través de repositorios y ViewModels. Se conserva la navegación tipada existente.

El login guarda el nombre y ejecuta una suspensión de exactamente 4000 ms antes de insertar ambos conjuntos dentro de una transacción. El tiempo total incluye además las operaciones de almacenamiento y navegación. DataStore y Room son almacenamientos distintos: primero se confirma la transacción de Room y después el indicador. Si se interrumpe ese último paso, el siguiente intento es idempotente y conserva los favoritos gracias a INSERT IGNORE.

Logout invalida la generación de sincronización dentro de un Mutex compartido, borra la sesión y el indicador, y elimina ambas tablas en una transacción. Una sincronización que estaba esperando no puede insertar después. Si el proceso termina durante el borrado, una nueva sesión limpia los residuos antes de sincronizar. Se limpia el historial protegido y los estados de navegación guardados.

## Verificación manual

1. Abrir la app sin sesión: debe aparecer el login. El botón permanece deshabilitado con nombre vacío.
2. Escribir un nombre y pulsar Iniciar sesión. Debe aparecer el progreso, deshabilitarse el botón y transcurrir la espera de 4 segundos antes de mostrar los datos.
3. Comprobar los 20 personajes y las 20 ubicaciones; abrir sus detalles y volver.
4. Marcar un personaje como favorito en la lista, abrir el detalle y comprobar el corazón. Desmarcar en detalle y comprobar la lista.
5. Activar Solo favoritos. Sin favoritos debe aparecer el mensaje vacío. Marcar varios y comprobar el filtro.
6. Cerrar y volver a abrir la app con sesión activa: debe ir a personajes sin repetir la sincronización y conservar favoritos.
7. Ir al perfil, comprobar el nombre y cerrar sesión. Debe regresar al login. Pulsar Atrás no debe mostrar las pantallas anteriores.
8. Iniciar sesión otra vez: debe repetirse la espera de 4 segundos, recuperar ambos conjuntos y comenzar sin favoritos anteriores.
9. Girar el dispositivo durante el login: el trabajo debe continuar en el ViewModel y el botón seguir bloqueado.

## Comandos

PowerShell, desde `C:\proyectos\lab07_moviles`:

```powershell
$env:JAVA_HOME = 'C:/Program Files/Android/Android Studio/jbr'
./gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest --console=plain
./gradlew.bat :app:connectedDebugAndroidTest --console=plain
```

APK esperado tras una compilación correcta: `C:\proyectos\lab07_moviles\app\build\outputs\apk\debug\app-debug.apk`.

Las pruebas instrumentadas usan datos de prueba y limpian las preferencias de la aplicación instalada en el dispositivo de prueba.

## Resultados ejecutados

- `:app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest --console=plain --max-workers=2`: BUILD SUCCESSFUL. Tres pruebas unitarias, cero fallos (dos mappers y la prueba existente).
- `:app:connectedDebugAndroidTest --console=plain --max-workers=2`: BUILD SUCCESSFUL. Cinco pruebas instrumentadas, cero fallos, en Pixel_6 con Android 17 (API 37). Cuatro nuevas cubren DAO, favoritos, idempotencia, reintento y cierre durante sincronizacion; una ya existia.
- `git diff --check`: sin errores.
- APK generado: `C:\proyectos\lab07_moviles\app\build\outputs\apk\debug\app-debug.apk` (20,532,226 bytes).
- Comprobacion mediante ADB/UI Automator: arranque al login, login hasta lista Room, favorito en lista reflejado en detalle, detalle de Rick Sanchez y perfil con nombre guardado.
- El primer intento de compilacion encontro un icono fuera de su alcance Compose; se corrigio antes de la compilacion exitosa. Queda una advertencia preexistente de obsolescencia de ArrowBack.
- Logs de compilacion y pruebas: `build/reports/lab10/`. Informes de pruebas: `app/build/reports/tests/testDebugUnitTest/` y `app/build/reports/androidTests/connected/debug/`.
- Compatibilidad de Room/KSP consultada en las fuentes oficiales: https://developer.android.com/jetpack/androidx/releases/room y https://developer.android.com/build/migrate-to-built-in-kotlin . No se cambiaron SDK 37, minSdk 24, AGP 9.3.2 ni Gradle 9.5.0.

Verificacion adicional ADB: cerrar sesion regreso al login; despues de pulsar Atras y abrir la actividad nuevamente se mantuvo el login.
