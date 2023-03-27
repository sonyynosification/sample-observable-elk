# Getting started
1. Start docker
2. Run
```sh
docker-compose up -d
```
3. Navigate to elastic search `http://localhost:9200`
4. Start `observable-elk`, `account-service`, `product-service`
5. Execute request:
```sh
curl -X GET --location "http://localhost:8080/account/1"
```
6. Navigate to kibana `http://localhost:5601`
7. Create index patterns before using kibana to view logs
