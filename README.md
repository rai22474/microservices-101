## Ari

Ari significa hormiga en japones. Por la fabula de Esopo, la cigarra y la hormiga.

Este sistema es un monólito, va a servir como base para los ejercicios del [curso microservicios 101](https://github.com/rai22474/ari/wiki).

Sus funcionalidades principales son:

* Gestión usuarios en el sistema.
* Bloqueo de la tarjeta virtual anónima.
* Petición de dinero a otro usuario.
* Envio de dinero a otro usuario.
 
## Instrucciones para ejecutar el proyecto

### Prerequisitos

 * Java 8
 * Maven 3.0

#### Construcción

mvn clean package

#### Test

mvn clean test

#### Ejecución

java -jar target/ari-0.1.0.jar

#### Acceptance Test

* cd acceptance-test
* mvn test
