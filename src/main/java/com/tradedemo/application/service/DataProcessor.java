package com.tradedemo.application.service;

import com.tradedemo.application.model.Trade;
import java.util.List;

public interface DataProcessor {
    List<Trade> processTrades(String content);
    String getContentType();
} 