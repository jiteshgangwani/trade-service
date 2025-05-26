package com.tradedemo.application.service;

import com.tradedemo.application.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TradeService {

    private final Map<String, DataProcessor> processors;

    @Autowired
    public TradeService(List<DataProcessor> dataProcessors) {
        this.processors = dataProcessors.stream()
                .collect(Collectors.toMap(DataProcessor::getContentType, Function.identity()));
    }

    public List<Trade> processTrades(String content, String contentType) {
        // Handle both text/plain and text/xml as valid content types
        if (contentType.equals(MediaType.TEXT_XML_VALUE)) {
            contentType = MediaType.APPLICATION_XML_VALUE;
        }
        
        DataProcessor processor = processors.get(contentType);
        if (processor == null) {
            throw new UnsupportedOperationException("Unsupported content type: " + contentType);
        }
        return processor.processTrades(content);
    }
}
