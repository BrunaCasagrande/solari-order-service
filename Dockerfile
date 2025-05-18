FROM eclipse-temurin:21-jdk-jammy

# ferramentas de espera
RUN apt-get update \
 && apt-get install -y default-mysql-client netcat \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8082

ENTRYPOINT sh -c "\
  echo '=> Aguardando MySQL...'; \
  until mysqladmin ping -h solari_order_db \
        -u ${DATASOURCE_USER:-user} \
        -p${DATASOURCE_PASS:-senha} --silent; do \
    sleep 3; \
  done; \
  echo '=> MySQL pronto! Aguardando Kafka...'; \
  until nc -z kafka 9092; do \
    sleep 3; \
  done; \
  echo '=> Kafka pronto! Iniciando aplicação.'; \
  exec java -jar app.jar \
"