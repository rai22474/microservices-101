## Microservices 101

El objetivo de este learning path es recorrer de una forma práctica algunos los aspectos de los aspectos más relevantes que influyen tener éxito a la hora de desarrollar una arquitectura de microservicios.

Este curso amplía [Herding microservices](https://www.youtube.com/playlist?list=PLfAoTEAPazb4eJflljcW8_Ld2Eclx7hOh), que fue impartido en [Campus Madrid](https://www.campus.co/madrid/es).

## Ari

Ari significa hormiga en japones. Por la fabula de Esopo, la cigarra y la hormiga.

Es un sistema monólitico, va a servir como base para los ejercicios del [curso](https://github.com/rai22474/microservices-101/wiki).

Sus funcionalidades principales son:

* Gestión usuarios en el sistema.
* Bloqueo de la tarjeta virtual anónima.
* Petición de dinero a otro usuario.
* Envio de dinero a otro usuario.
 
## Instrucciones para ejecutar el proyecto

El código fuente de este proyecto es java. Se usa [Spring boot](https://projects.spring.io/spring-boot/) cómo framework.

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
