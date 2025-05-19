package com.expense.auth.eventProducer;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.expense.auth.model.UserInfoDto;

@Service
public class UserInfoProducer {
     private final KafkaTemplate<String, UserInfoDto> kafkaTemplate;

     @Value("${spring.kafka.topic-json.name}")
     private String TOPIC_NAME;

     @Autowired
     UserInfoProducer(KafkaTemplate<String, UserInfoDto> kafkaTemplate) {
          this.kafkaTemplate = kafkaTemplate;
     }

     public void sendEventToKafka(UserInfoEvent userInfoDto) throws InterruptedException, ExecutionException {
          Message<UserInfoEvent> message = MessageBuilder.withPayload(userInfoDto)
                    .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME).build();
          kafkaTemplate.send(message).get();
     }

}
