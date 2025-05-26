# trade-service

A Spring Boot microservice that processes trade data in both CSV and XML formats.

## Features
- Supports both CSV and XML input formats
- Validates trade dates (YYYYMMDD format)
- Enriches trade data with product information
- Returns processed data in CSV format
- Filters out invalid dates automatically

## Getting Started

### Prerequisites
- Java 8 or higher
- Maven 3.x
- Spring Boot 2.7.3

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

### Sample Files
The repository includes two sample files for testing:

1. `trade.csv` - Sample CSV trade data:
```csv
date,product_id,currency,price
20160101,1,EUR,10.0
20160102,2,USD,15.5
20160103,3,GBP,20.75
20160104,1,EUR,11.25
20160105,2,USD,16.80
```

2. `trade.xml` - Sample XML trade data:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<trades>
    <trade>
        <date>20160101</date>
        <product_id>1</product_id>
        <currency>EUR</currency>
        <price>10.0</price>
    </trade>
    <!-- More trades... -->
</trades>
```

### Testing in Windows PowerShell

#### Method 1: Using PowerShell Native Commands
```powershell
# For CSV
$headers = @{ "Content-Type" = "text/plain" }
Invoke-WebRequest -Method Post -Headers $headers -InFile "trade.csv" -Uri "http://localhost:8081/api/v1/enrich"

# For XML
$headers = @{ "Content-Type" = "application/xml" }
Invoke-WebRequest -Method Post -Headers $headers -InFile "trade.xml" -Uri "http://localhost:8081/api/v1/enrich"
```

#### Method 2: Using curl.exe (requires Git Bash or curl installed)
```powershell
# For CSV
curl.exe -X POST -H "Content-Type: text/plain" --data-binary "@trade.csv" http://localhost:8081/api/v1/enrich

# For XML
curl.exe -X POST -H "Content-Type: application/xml" --data-binary "@trade.xml" http://localhost:8081/api/v1/enrich
```

### Testing in Git Bash or Linux
```bash
# For CSV
curl -X POST \
  -H "Content-Type: text/plain" \
  --data-binary "@trade.csv" \
  http://localhost:8081/api/v1/enrich

# For XML
curl -X POST \
  -H "Content-Type: application/xml" \
  --data-binary "@trade.xml" \
  http://localhost:8081/api/v1/enrich
```

### Response Format
The response is always in CSV format:
```csv
date,product_name,currency,price
20160101,Treasury Bills Domestic,EUR,10.0
20160102,Corporate Bonds Domestic,USD,15.5
20160103,REPO Domestic,GBP,20.75
```

## Data Validation
- Dates must be in YYYYMMDD format
- Valid dates range from year 1900 to 9999
- Invalid dates are automatically filtered out from the response
- All fields (date, product_id, currency, price) are required

## Error Handling
- Invalid dates are silently filtered out
- Unsupported content types return 415 Unsupported Media Type
- Malformed input returns appropriate error messages
- XML parsing errors return 400 Bad Request

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
7. Add support for custom date formats
8. Implement rate limiting and security measures
