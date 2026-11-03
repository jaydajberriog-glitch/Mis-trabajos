# Monedero Virtual

Proyecto académico desarrollado con Java 17, JavaFX, Maven y JUnit 5.

## Abrir y ejecutar en IntelliJ IDEA
1. Descomprime la carpeta.
2. En IntelliJ, selecciona **Open** y elige la carpeta `MonederoVirtual` (la que contiene `pom.xml`).
3. Espera a que Maven descargue JavaFX y JUnit.
4. Abre la ventana Maven y ejecuta `Plugins > javafx > javafx:run`, o abre `MainApp.java` y ejecútalo si IntelliJ reconoce la configuración.
5. Para ejecutar las pruebas, usa Maven `Lifecycle > test`.

También puedes ejecutar desde una terminal ubicada en la carpeta del proyecto:
- Windows: `mvn clean javafx:run`
- Pruebas: `mvn test`

## Reglas de negocio iniciales
- Cada $10.000 completos en depósitos y transferencias enviadas generan 1 punto.
- Los retiros no generan puntos.
- Rangos: Bronce (0-499), Plata (500-1499), Oro (1500-2999), Platino (3000 o más).
- Se alerta saldo bajo cuando un monedero queda por debajo de $20.000.
- Un movimiento se considera atípico cuando supera la media más dos desviaciones estándar.
- Beneficios de ejemplo: bono de $10.000 por 1000 puntos y bono de $25.000 por 2200 puntos.

## Alcance de esta versión
Los datos se mantienen en memoria durante la ejecución. Al cerrar la aplicación se reinician. La persistencia en archivos o base de datos no fue solicitada expresamente y se puede agregar después.
