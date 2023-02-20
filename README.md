# Instrucciones de uso

java-letters-api es una API HTTP REST para listar, crear y actualizar clientes la cual se utiliza como aplicación demo para el laboratorio Infraestructura como Servicio de los entrenamientos AWS de arquitecto java.

El objetivo del laboratorio es el poner en práctica los conocimientos en AWS IAM, AWS EC2 y AWS RDS.

Tecnologías usadas por java-letters-api (no se requiere previo conocimiento en ellas para poder desarrollar el laboratorio):
* Java
* Spring Boot
* Gradle
* MySQL
* Docker

Requisitos
* Contar con acceso a la cuenta AWS awsserverless2023 (todos los estudiantes tienen acceso a la cuenta).
En caso de no ser estudiante, contar con una cuenta AWS donde desarrollar el laboratorio.

## Compilación

La compilación es un paso que solo es necesaria si se ha modificado el código fuente, o si no se tiene acceso al repositorio AWS ECR.

Compilar y generar jar

`./gradlew --no-daemon clean build -x test`

Ejecutar tests unitarios

`./gradlew --no-daemon test`

Generar imagen Docker

`./gradlew --no-daemon dockerBuildImage` 
 
## Ejecutar en localhost

1. Crear una base de datos MySQL.
En caso de no contar con una instalación MySQL, como alternativa, usar Docker para obtener un contenedor a partir de la imagen oficial de MySQL.  

```
docker network create proyectoiaas
docker run --rm -p 3306:3306 --network proyectoiaas --name mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=lettersdb mysql:5.7.33
```
 
2. Ejecutar el script de creación de la tabla clientes:
El siguiente comando permite acceder al contenedor `mysql` iniciado con el comando del paso anterior. 

`docker exec -it mysql /bin/bash`

Una vez dentro del contenedor `mysql`, el siguiente comando permite conectarse a MySQL para ejecutar instrucciones en la base de datos: 
`mysql -p`

El siguiente es el script de la tabla clientes:

```
CREATE TABLE `clientes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fname` varchar(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `doc_id` varchar(12) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `instagram` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);
```

3. Ejecutar un contenedor con la imagen `java-letters-api`.
En caso de no contar con la imagen `java-letters-api`, esta se puede obtener al compilar (ver [Compilación](#compilacion))

`docker run --rm -p 8080:8080 --network proyectoiaas --name java-letters-api -e spring.datasource.password=password -e spring.datasource.username=root -e spring.datasource.url=jdbc:mysql://mysql:3306/lettersdb -e spring.profiles.active=local java-letters-api`

4. Realizar una petición HTTP GET al recuerso clientes.

`http://localhost:8080/clientes`

## Publicar imagen Docker en AWS ECR

La publicación solo es necesaria si se quiere utilizar otro repositorio AWS ECR.

1. Hacer login en el repositorio AWS ECR (cambiar `428571511138.dkr.ecr.us-east-1.amazonaws.com` por la url de la cuenta y región)

`aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 428571511138.dkr.ecr.us-east-1.amazonaws.com`

2. Marcar la imagen java-letters-api como la imagen latest a publicar en el repositorio (cambiar `428571511138.dkr.ecr.us-east-1.amazonaws.com/java-letters-api` por la url de la cuenta, región y repositorio a usar)

`docker tag java-letters-api:latest 428571511138.dkr.ecr.us-east-1.amazonaws.com/java-letters-api:latest`

3. Copiar la imagen java-letters-api en el repositorio AWS ECR (cambiar `428571511138.dkr.ecr.us-east-1.amazonaws.com/java-letters-api` por la url de la cuenta, región y repositorio a usar)

`docker push 428571511138.dkr.ecr.us-east-1.amazonaws.com/java-letters-api:latest`

## Ejecutar en instancia AWS EC2

1. Solicitar la instancia EC2

2. Instalar Docker en la instancia EC2 (https://www.cyberciti.biz/faq/how-to-install-docker-on-amazon-linux-2/)

```
sudo yum install -y docker
sudo usermod -a -G docker ec2-user
id ec2-user
newgrp docker
sudo systemctl enable docker.service
sudo systemctl start docker.service
```

3. Asumir el rol IAM autorizado para obtener la imagen Docker java-letters-api desde el repositorio `428571511138.dkr.ecr.us-east-1.amazonaws.com/java-letters-api`


```
export ASSUME_ROLE_RESPONSE=$(aws sts assume-role --role-arn "arn:aws:iam::428571511138:role/r-awsserverless2023" --role-session-name "Deploy")`

export AWS_ACCESS_KEY_ID=$(echo $ASSUME_ROLE_RESPONSE | jq -r '.Credentials.AccessKeyId')
export AWS_SECRET_ACCESS_KEY=$(echo $ASSUME_ROLE_RESPONSE | jq -r '.Credentials.SecretAccessKey')
export AWS_SESSION_TOKEN=$(echo $ASSUME_ROLE_RESPONSE | jq -r '.Credentials.SessionToken')
```

4. Autenticarse ante el repositorio AWS ECR

`aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 428571511138.dkr.ecr.us-east-1.amazonaws.com`

5. Ejecutar de la misma forma explicada en [Ejecutar en localhost](#ejecutar-en-localhost)
