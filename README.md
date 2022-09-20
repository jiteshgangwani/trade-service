# trade-service


Run microservice

java -jar 

curl command to test
====================
curl -v  --request POST   --data-binary "@trade.csv" --header 'Content-Type: text/csv' --header 'Accept: text/csv' http://localhost:8081/api/v1/enrich



TODO
1. test case creation
2. code refactoring
3. optimize code for bulk files
