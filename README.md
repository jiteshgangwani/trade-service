# trade-service


Run microservice from command prompt

mvn install ;
java  -jar target/trade-service-0.0.1-SNAPSHOT.jar


curl command to test
====================

curl -v  --request POST   --data-binary "@trade.csv" --header 'Content-Type: text/csv' --header 'Accept: text/csv' http://localhost:8081/api/v1/enrich

--data-binary have to be used to maintain formatting of csv file as part of request


TODO
1. test case creation
2. code refactoring
3. optimize code for bulk files
