# Traductor 
# Turco ↔ Español

Aplicación móvil Android que traduce entre idiomas turco y español.

## Integrante
- Rahyzza Novoa Vasquinzay

## Asignatura
DSY1105 - Desarrollo de Aplicaciones Móviles

## Funcionalidades
- Registro e inicio de sesión con validaciones visuales por campo
- Traducción turco ↔ español con ML Kit (funciona sin internet tras descargar el modelo)
- Dictado por voz con el micrófono (permiso en tiempo de ejecución)
- Retroalimentación háptica con vibración
- Historial de traducciones con favoritos y eliminación (persistencia local con Room)
- Animaciones de retroalimentación en formularios y botones

## Tecnologías
- Kotlin 
- Jetpack Compose 
- Material 3
- MVVM 
- Room 
- KSP
- ML Kit Translate 
- Navigation Compose

## Estructura
- `data/`: Room (entidades, DAOs) y repositorio
- `domain/`: validaciones y motor de traducción
- `native/`: recursos nativos (micrófono y vibración)
- `ui/`: pantallas, ViewModels y navegación

## Cómo ejecutar
1. Clonar el repositorio: `git clone https://github.com/RayFullstack/App_TraductorTurco.git`
2. Abrir la carpeta en Android Studio y sincronizar Gradle
3. Ejecutar en un emulador o dispositivo con Android 8.0 (API 26) o superior
4. La primera traducción requiere internet para descargar el modelo


## Planificación
Tablero Trello: https://trello.com/b/rmLgjb0e/app-tuco

