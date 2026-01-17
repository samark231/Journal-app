package com.samar.Journal_app.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.samar.Journal_app.dto.UserDto;
import com.samar.Journal_app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    public User updateUserDetails(String username, UserDto userDetails){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        Update update = new Update();
        if (userDetails.getEmail()!=null) update.set("email", userDetails.getEmail());
        if (userDetails.getFirstName()!=null) update.set("firstName", userDetails.getFirstName());
        if (userDetails.getLastName()!=null) update.set("lastName", userDetails.getLastName());
        if (userDetails.getDob()!=null) update.set("dob", userDetails.getDob());
        if (userDetails.getGender()!=null) update.set("gender", userDetails.getGender());
        if (userDetails.getSentimentAnalysis()!=null) update.set("sentimentAnalysis", userDetails.getSentimentAnalysis());
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
        return mongoTemplate.findAndModify(query, update, options, User.class);


    }
    public List<User> getUserByUsernameOrEmail(String usernameOrEmail){
        Query query = new Query();
        Criteria criteriaUsername = Criteria.where("username").is(usernameOrEmail);
        Criteria criteriaEmail = Criteria.where("email").is(usernameOrEmail);
        query.addCriteria(new Criteria().orOperator(criteriaUsername, criteriaEmail));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;

    }
}
