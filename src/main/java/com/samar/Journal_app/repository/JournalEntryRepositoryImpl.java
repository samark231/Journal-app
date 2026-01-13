package com.samar.Journal_app.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.samar.Journal_app.entity.JournalEntry;
import com.samar.Journal_app.entity.User;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JournalEntryRepositoryImpl{

    private final MongoTemplate mongoTemplate;

    public List<JournalEntry> fetchAllEntriesOfUser(String username){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.find(query, JournalEntry.class);
    }
    public List<JournalEntry> getJournalById(ObjectId journalId, String username){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(journalId));
        query.addCriteria(Criteria.where("username").is(username));//for security so that a user can only see their own journal.
        return mongoTemplate.find(query, JournalEntry.class);
    }
    public Long deleteJournalById(ObjectId journalId, String username){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(journalId));
        query.addCriteria(Criteria.where("username").is(username));
        DeleteResult remove = mongoTemplate.remove(query, JournalEntry.class);
        return remove.getDeletedCount();
    }
    public Boolean updateJournalById(ObjectId journalId, String username, JournalEntry entryToUpdate){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(journalId));
        query.addCriteria(Criteria.where("username").is(username));
        Update update = new Update();
        update.set("title",entryToUpdate.getTitle());
        if(entryToUpdate.getContent()!=null){
            update.set("content", entryToUpdate.getContent());
        }
        update.set("date", LocalDateTime.now());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, JournalEntry.class);
        return updateResult.getMatchedCount()>0;
    }
}
