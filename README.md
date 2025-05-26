# trade-service

A Spring Boot microservice that processes trade data in both CSV and XML formats.

## Features
- Supports both CSV and XML input formats
- Validates trade dates
- Enriches trade data with product information
- Returns processed data in CSV format

## Getting Started

### Prerequisites
- Java 8 or higher
- Maven 3.x

### Building the Service
```bash
mvn clean install
```

### Running the Service
```bash
java -jar target/trade-service-0.0.1-SNAPSHOT.jar
```

The service will start on port 8081 by default.

## API Usage

### Endpoint
POST http://localhost:8081/api/v1/enrich

### Input Formats

#### CSV Format
```bash
curl -v --request POST \
  --data-binary "@trade.csv" \
  --header 'Content-Type: text/plain' \
  --header 'Accept: text/plain' \
  http://localhost:8081/api/v1/enrich
```

Example CSV content:
```csv
date,product_id,currency,price
20160101,1,EUR,10.0
```

#### XML Format
```bash
curl -v --request POST \
  --data-binary "@trade.xml" \
  --header 'Content-Type: application/xml' \
  --header 'Accept: text/plain' \
  http://localhost:8081/api/v1/enrich
```

Example XML content:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<trades>
    <trade>
        <date>20160101</date>
        <product_id>1</product_id>
        <currency>EUR</currency>
        <price>10.0</price>
    </trade>
</trades>
```

### Response Format
The response is always in CSV format:
```csv
date,product_name,currency,price
20160101,Treasury Bills Domestic,EUR,10.0
```

## Data Validation
- Dates must be in YYYYMMDD format
- Invalid dates will be filtered out from the response
- All fields (date, product_id, currency, price) are required

## Error Handling
- Invalid dates are silently filtered out
- Unsupported content types return 415 Unsupported Media Type
- Malformed input returns appropriate error messages

## Development

### Running Tests
```bash
mvn test
```

The test suite includes:
- CSV format validation
- XML format validation
- Date validation
- Error handling
- Content type validation

## Future Improvements
1. Add support for more input/output formats (JSON, etc.)
2. Implement bulk file processing optimization
3. Add more comprehensive error reporting
4. Add input validation for currency and price formats
5. Implement caching for product data
6. Add API documentation using Swagger/OpenAPI
