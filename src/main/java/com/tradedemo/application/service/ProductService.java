package com.tradedemo.application.service;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.tradedemo.application.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    Map<String, Product> products = new HashMap<>();

    @Value(value = "classpath:companies.xml")
    private Resource product;

    public ProductService() {

        try {

            Resource resource = new ClassPathResource("product.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(',');
            ObjectReader oReader = csvMapper.reader(Product.class).with(schema);

                MappingIterator<Product> mi = oReader.readValues(reader);
                while (mi.hasNext()) {
                    Product current = mi.next();
                    products.put(current.getProductId(), current);
                }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public Map<String, Product> getAllProducts() {
        return products;
    }
}



