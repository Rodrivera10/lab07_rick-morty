# Laboratorio 12: Rick and Morty offline first

## Ejecución

- Rama de entrega: `laboratorio12`.
- El login descarga todas las páginas de personajes y locations del API y las persiste en Room.
- Con una sesión activa, las pantallas consultan Room y las operaciones de personajes/favoritos son locales.
- Cerrar sesión limpia las dos tablas; el siguiente login vuelve a sincronizar.
- APK debug: `apk/laboratorio12.apk`.

La descarga necesita conexión durante el primer login. Si falla, el mensaje de error del login permite volver a intentarlo.
