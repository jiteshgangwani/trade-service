package com.tradedemo.application.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.tradedemo.application.model.Trade;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CsvDataProcessor implements DataProcessor {
    
    @Override
    public List<Trade> processTrades(String content) {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        ObjectReader oReader = csvMapper.reader(Trade.class).with(schema);
        List<Trade> trades = new ArrayList<>();

        try (StringReader reader = new StringReader(content)) {
            MappingIterator<Trade> mi = oReader.readValues(reader);
            while (mi.hasNext()) {
                Trade current = mi.next();
                trades.add(current);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing CSV data", e);
        }
        
        // Filter out invalid dates
        return trades.stream()
                    .filter(trade -> isValidDate(trade.getDate()))
                    .collect(Collectors.toList());
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }
    
    private boolean isValidDate(String date) {
        if (date == null || date.length() != 8) {
            return false;
        }
        try {
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4, 6));
            int day = Integer.parseInt(date.substring(6, 8));
            
            return year >= 1900 && year <= 9999 &&
                   month >= 1 && month <= 12 &&
                   day >= 1 && day <= 31;
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return false;
        }
    }
} 