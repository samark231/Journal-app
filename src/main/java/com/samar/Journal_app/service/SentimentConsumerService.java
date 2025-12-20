package com.samar.Journal_app.service;

import com.samar.Journal_app.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {
    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "weekly-sentiments", groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentiMentData){
        sendEmail(sentiMentData);
        int v = 4;
    }

    private void sendEmail(SentimentData sentiMentData){
        emailService.sendEmail(sentiMentData.getEmail(), "Sentiment Data for previous week", sentiMentData.getSentiment());
        int a = 1;
    }
}
