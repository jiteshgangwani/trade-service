package com.tradedemo.application.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.tradedemo.application.model.Trade;
import com.tradedemo.application.model.TradeList;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class XmlDataProcessor implements DataProcessor {
    
    @Override
    public List<Trade> processTrades(String content) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            TradeList tradeList = xmlMapper.readValue(content, TradeList.class);
            return tradeList.getTrades().stream()
                          .filter(trade -> isValidDate(trade.getDate()))
                          .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error processing XML data", e);
        }
    }

    @Override
    public String getContentType() {
        return "application/xml";
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