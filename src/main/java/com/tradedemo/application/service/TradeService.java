package com.tradedemo.application.service;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.tradedemo.application.model.Trade;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TradeService {

    public List<Trade> getTrade(String tradeString) {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        ObjectReader oReader = csvMapper.reader(Trade.class).with(schema);
        List<Trade> trades = new ArrayList<>();

        try (Reader reader = new StringReader(tradeString)) {
            MappingIterator<Trade> mi = oReader.readValues(reader);
            while (mi.hasNext()) {
                Trade current = mi.next();
                if(dateValidator(current.getDate())) {
                    trades.add(current);
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return trades;
    }

    private boolean dateValidator(String dateStr)
    {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

}
