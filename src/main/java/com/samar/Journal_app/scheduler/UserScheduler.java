package com.samar.Journal_app.scheduler;

import com.samar.Journal_app.cache.AppCache;
import com.samar.Journal_app.entity.JournalEntry;
import com.samar.Journal_app.model.SentimentData;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.enums.Sentiment;
import com.samar.Journal_app.repository.UserRepositoryImpl;
import com.samar.Journal_app.service.EmailService;
import com.samar.Journal_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    @Autowired
    private AppCache appCache;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron = "0 0 6 ? * SUN")
    public void fetchUsersAndSendMail(){
        List<User> users = userRepositoryImpl.findUserForSa();
        for( User user: users){
            Sentiment mostFreqSentiment = calculateMostFrequentSentiment(user);
            if(mostFreqSentiment!=null) {
                SentimentData sentiMentData = SentimentData.builder().email(user.getEmail()).sentiment("sentiment for the last 7 days: "+mostFreqSentiment.toString()).build();
                try{
                    kafkaTemplate.send("weekly-sentiments", sentiMentData.getEmail(), sentiMentData);
                } catch (Exception e) {
                    emailService.sendEmail(sentiMentData.getEmail(), "sentiment for previous week. ","sentiment for the last 7 days: "+mostFreqSentiment.toString());
                }

            }
        }
    }

    @Scheduled(cron = "0 0/10 * 1/1 * ?")
    public void refreshCache(){
        appCache.init();
    }

    private Sentiment calculateMostFrequentSentiment(User user ){
        List<JournalEntry> allJournals = user.getJournalEntries();
        List<Sentiment> allSentiments = allJournals.stream()
                .filter(x-> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                .map(x-> x.getSentiment())
                .collect(Collectors.toList());
        Map<Sentiment, Integer> sentimentCount = new HashMap<>();
        for(Sentiment sentiment : allSentiments){
            sentimentCount.put(sentiment, sentimentCount.getOrDefault(sentiment, 0)+1);
        }
        int maxCount = 0;
        Sentiment mostFreqSentiment = null;
        for(Map.Entry<Sentiment, Integer> entry: sentimentCount.entrySet()){
            if(entry.getValue()>maxCount){
                maxCount = entry.getValue();
                mostFreqSentiment = entry.getKey();
            }
        }
        return mostFreqSentiment;
    }
}
