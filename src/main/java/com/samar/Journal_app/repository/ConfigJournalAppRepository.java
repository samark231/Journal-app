package com.samar.Journal_app.repository;

import com.samar.Journal_app.entity.ConfigJournalEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalEntity, ObjectId> {

}
