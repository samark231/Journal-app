package com.samar.Journal_app.repository;

import com.samar.Journal_app.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

public interface UserRepository extends MongoRepository<User, String> {
    public User findByUsername(String username);

    @Query("{'username':?0}")
    @Update("{$set:{'password':?1}}")
    public Long updatePassword(String username, String newPassword);

    @Query("{'username':?0}")
    @Update("{'$push':{'journalEntries':?1}}")
    public void addJournalId(String username, ObjectId journalId);

    @Query("{'username':?0}")
    @Update("{$pull:{'journalEntries':{'$id':?1}}}")
    public Long removeJournalId(String username, ObjectId journalId);

    @Query(value = "{'roles':?0}", exists = true)
    public boolean checkAdmin(String role);



}