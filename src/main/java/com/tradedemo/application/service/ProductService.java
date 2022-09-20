package com.tradedemo.application.service;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.tradedemo.application.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    Map<String, Product> products = new HashMap<>();


    public ProductService() {

        try {
            File file = new ClassPathResource("product.csv").getFile();
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(',');
            ObjectReader oReader = csvMapper.reader(Product.class).with(schema);

            try (Reader reader = new FileReader(file)) {
                MappingIterator<Product> mi = oReader.readValues(reader);
                while (mi.hasNext()) {
                    Product current = mi.next();
                    products.put(current.getProductId(), current);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public Map<String, Product> getAllProducts() {
        return products;
    }
}
