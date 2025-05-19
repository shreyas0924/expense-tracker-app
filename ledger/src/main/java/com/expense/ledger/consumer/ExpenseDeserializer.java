package com.expense.ledger.consumer;

import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import com.expense.ledger.DTO.ExpenseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExpenseDeserializer implements Deserializer<ExpenseDto> {

     @Override
     public void close() {
     }

     @Override
     public void configure(Map<String, ?> arg0, boolean arg1) {
     }

     @Override
     public ExpenseDto deserialize(String arg0, byte[] arg1) {
          ObjectMapper mapper = new ObjectMapper();
          ExpenseDto expenseDto= null;
          try {
               expenseDto = mapper.readValue(arg1, ExpenseDto.class);
          } catch (Exception e) {
               e.printStackTrace();
          }
          return expenseDto;
     }
}
