package com.tradedemo.application.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.tradedemo.application.model.Trade;
import com.tradedemo.application.model.TradeList;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class XmlDataProcessor implements DataProcessor {
    
    @Override
    public List<Trade> processTrades(String content) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            TradeList tradeList = xmlMapper.readValue(content, TradeList.class);
            return tradeList.getTrades();
        } catch (IOException e) {
            throw new RuntimeException("Error processing XML data", e);
        }
    }

    @Override
    public String getContentType() {
        return "application/xml";
    }
} 