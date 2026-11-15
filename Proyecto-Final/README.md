# Monedero Virtual

Proyecto académico desarrollado con Java 17, JavaFX, Maven y JUnit 5 (arquitectura MVC).

## Abrir y ejecutar en IntelliJ IDEA
1. Descomprime la carpeta.
2. En IntelliJ, selecciona **Open** y elige la carpeta que contiene `pom.xml`.
3. Espera a que Maven descargue JavaFX y JUnit.
4. Ejecuta `Plugins > javafx > javafx:run`, o abre `view/Aplicacion.java` y ejecútalo.
5. Para las pruebas: `Lifecycle > test`.

Desde terminal: `mvn clean javafx:run` y `mvn test`.

## Reglas de negocio (según el enunciado)
- Puntos: depósitos 1 punto por cada 100 unidades, retiros 2 y transferencias 3 (solo el monto, sin comisión).
- Rangos: Bronce 0-500, Plata 501-1000, Oro 1001-5000, Platino más de 5000.
- Comisiones: transferencias 2% y retiros 1% del monto.
- Tarifas por rango: descuento sobre la comisión de transferencias (Plata 5%, Oro 10%, Platino 20%).
- Beneficios canjeables:
  - 100 puntos: 10% menos en la comisión de transferencias (se suma al descuento del rango).
  - 500 puntos: 30 días sin comisión en retiros.
  - 1000 puntos: bono de 50 unidades en el monedero.
- Al revertir una transacción se restauran saldos, comisión y puntos; el historial de puntos registra el ajuste.
- Alertas: saldo bajo (menos de $20.000), recordatorios de transacciones programadas (hoy o mañana),
  movimientos atípicos y canjes. Cada alerta se notifica por correo, WhatsApp y SMS (envío simulado).
- Detección de atípicas: un gasto (retiro o transferencia no reversado) es atípico si supera
  promedio + 2 x desviación estándar (poblacional). Queda marcado como SOSPECHOSA y genera una alerta.
  Se necesitan al menos 6 gastos para que un valor pueda superar ese límite. Complejidad O(n).

## Estructura
- `view`, `viewcontrollers`: interfaz JavaFX (FXML).
- `controllers`: `MonederoController`.
- `model`: dominio (`SistemaMonedero` como fachada, transacciones, puntos, alertas, notificadores, detector).
- `exceptions`: `MonederoException` y sus tres subclases de negocio.

## Alcance
Los datos se mantienen en memoria durante la ejecución. Las notificaciones son simuladas: se muestran en la
consola de la aplicación con el formato de cada canal; gracias a la interfaz `Notificador`, un canal real
se puede integrar sin tocar el resto del sistema.
