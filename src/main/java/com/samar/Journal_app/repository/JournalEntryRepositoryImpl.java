package com.samar.Journal_app.repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.samar.Journal_app.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class JournalEntryRepositoryImpl{
    @Autowired
    private MongoTemplate mongoTemplate;
    public Boolean updateJournalById(ObjectId journalId, JournalEntry newEntry){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(journalId));
        Update update = new Update();
        update.set("title",newEntry.getTitle());
        update.set("content", newEntry.getContent());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, JournalEntry.class);
        return updateResult.wasAcknowledged();
    }
    public Boolean deleteJournalById(ObjectId journalId){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(journalId));
        DeleteResult remove = mongoTemplate.remove(query, JournalEntry.class);
        return remove.wasAcknowledged();
    }


}
