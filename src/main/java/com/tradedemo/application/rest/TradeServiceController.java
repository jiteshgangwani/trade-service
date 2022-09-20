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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class TradeServiceController {

    private static final Logger logger = LoggerFactory.getLogger(TradeServiceController.class);

    @Autowired
    ProductService productService;

    @Autowired
    TradeService tradeService;

    @PostMapping(value = "/v1/enrich", consumes = "text/csv")
    public ResponseEntity fileUpload(@RequestBody String file) {
        logger.info("message received " + file);

        List<Trade> tradeList = tradeService.getTrade(file);


        // setting HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "text/cvs");

        return new ResponseEntity<>(
                writeToCSV(tradeList, productService.getAllProducts()).toString(),
                headers,
                HttpStatus.OK
        );

    }


    private static final String CSV_SEPARATOR = ",";

    private static StringBuffer writeToCSV(List<Trade> trades, Map<String, Product> productMap) {
        StringBuffer oneLine = new StringBuffer();
        oneLine.append("date,product_name,currency,price");
        oneLine.append(System.lineSeparator());
        for (Trade trade : trades) {
            oneLine.append(trade.getDate());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(productMap.containsKey(trade.getProductId()) ? productMap.get(trade.getProductId()).getProductName() : "Missing Product Name");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(trade.getCurrency());
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(trade.getPrice());
            oneLine.append(System.lineSeparator());
        }
        return oneLine;
    }

}


