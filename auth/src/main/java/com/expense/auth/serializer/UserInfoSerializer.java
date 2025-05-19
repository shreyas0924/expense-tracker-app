package com.expense.auth.serializer;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.expense.auth.eventProducer.UserInfoEvent;
import com.expense.auth.model.UserInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserInfoSerializer implements Serializer<UserInfoEvent> {

     @Override
     public void configure(Map<String, ?> configs, boolean isKey) {
     }

     @Override
     public byte[] serialize(String topic, UserInfoEvent data) {
          byte[] returnValue = null;
          ObjectMapper objectMapper = new ObjectMapper();

          try {
               returnValue = objectMapper.writeValueAsString(data).getBytes();
          } catch (Exception e) {
               e.printStackTrace();
          }

          return returnValue;
     }

     @Override
     public void close() {
     }
}
