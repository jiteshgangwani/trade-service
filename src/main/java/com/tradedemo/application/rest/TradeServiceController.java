package com.tradedemo.application.rest;

import com.tradedemo.application.model.Product;
import com.tradedemo.application.model.Trade;
import com.tradedemo.application.service.ProductService;
import com.tradedemo.application.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class TradeServiceController {

    private static final Logger logger = LoggerFactory.getLogger(TradeServiceController.class);
    private static final String CSV_SEPARATOR = ",";

    @Autowired
    ProductService productService;

    @Autowired
    TradeService tradeService;

    @PostMapping(value = "/v1/enrich", 
                consumes = {MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> processTrade(
            @RequestBody String content,
            @RequestHeader("Content-Type") String contentType) {
        
        logger.debug("Processing trade data with content type: {}", contentType);
        List<Trade> tradeList = tradeService.processTrades(content, contentType);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        return new ResponseEntity<>(
                writeToCSV(tradeList, productService.getAllProducts()),
                headers,
                HttpStatus.OK
        );
    }

    private String writeToCSV(List<Trade> trades, Map<String, Product> productMap) {
        StringBuilder output = new StringBuilder();
        output.append("date,product_name,currency,price\n");
        
        for (Trade trade : trades) {
            output.append(trade.getDate())
                  .append(CSV_SEPARATOR)
                  .append(productMap.containsKey(trade.getProductId()) 
                         ? productMap.get(trade.getProductId()).getProductName() 
                         : "Missing Product Name")
                  .append(CSV_SEPARATOR)
                  .append(trade.getCurrency())
                  .append(CSV_SEPARATOR)
                  .append(trade.getPrice())
                  .append('\n');
        }
        return output.toString();
    }
}


