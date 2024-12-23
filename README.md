To launch the application:
1) Launch service dependencies (PostgreSQL, Elasticsearch).
In order to do this, in the project directory launch the docker containers with the help of the above services following the commands:
   
`docker compose build
docker compose up`

2) After the launch of the Elasticsearch container, you need to get Fingerprint, by running the following command within the container:

`openssl x509 -fingerprint -sha256 -in config/certs/http_ca.crt`

3) Give the parameters of the connection to Elasticsearch. After that, the main application can be launched. State the value of the earlier obtained Fingerprint instead of the `<caFingerprint>` placeholder.
   
`mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Delasticsearch.host=localhost:9200 -Delasticsearch.caFingerprint=<caFingerprint> -Delasticsearch.user=elastic -Delasticsearch.password=4SXmyR-w*+p=F6iHfJUY"`

4) For the initial indexation of the application, run the following request:
   
`curl --location 'localhost:8080/api/search/ordersByProductName/reindex'`

After this step, the default order database will be indexed and ready for search using the following request:

`curl --location 'localhost:8080/api/search/ordersByProductName?name_part=name_part'`

The full list of possible requests can be found in Swagger documentation. 