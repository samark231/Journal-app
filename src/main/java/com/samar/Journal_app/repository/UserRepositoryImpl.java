package com.samar.Journal_app.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.samar.Journal_app.entity.User;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> findUserForSa(){
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }

    public Long deleteAllUsers(){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").ne("admin"));
        DeleteResult remove = mongoTemplate.remove(query, User.class);
        return remove.getDeletedCount();
    }
    public Boolean updateEmailAndSentiment(String username, String email, Boolean sentimentAnalysis){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        Update update = new Update();
        if (email!=null) update.set("email", email);
        if (sentimentAnalysis!=null) update.set("sentimentAnalysis", sentimentAnalysis);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, User.class);
        return updateResult.wasAcknowledged();

    }
}
