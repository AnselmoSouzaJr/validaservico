FROM ubuntu:latest
LABEL authors="Anselmo"

ENTRYPOINT ["top", "-b"]
# Use uma imagem base do OpenJDK para aplicativos Java
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR gerado para o diretório de trabalho
COPY target/validaservico-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta padrão usada pelo Spring Boot
EXPOSE 8080

# Define o comando para executar o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]