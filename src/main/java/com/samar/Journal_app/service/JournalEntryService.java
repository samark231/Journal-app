package com.samar.Journal_app.service;

import com.samar.Journal_app.entity.JournalEntry;
import com.samar.Journal_app.repository.JournalEntryRepository;
import com.samar.Journal_app.repository.JournalEntryRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class JournalEntryService {
    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private JournalEntryRepositoryImpl journalEntryRepositoryImpl;

    @Transactional
    public JournalEntry saveNewEntry(JournalEntry journalEntry, String username){
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
        userService.addJournalEntryToUser(username, savedEntry.getId());
        return savedEntry;
    }
    public JournalEntry saveEntry(JournalEntry journalEntry){
       return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllJournals() {
        return journalEntryRepository.findAll();
    }


    @Transactional
    public boolean deleteEntry(String username, ObjectId journalId){
        try{
            log.info("trying to delete journalEntry form user collection irst...");
            Long delCount = userService.deleteJournalEntryFromUser(username, journalId);
            log.info("journal entry deleted from user collection.");
            if(!delCount.equals(0L)){
                log.info("Now trying to delete journalEntry form JournalEntries collection first...");
                journalEntryRepositoryImpl.deleteJournalById(journalId);
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            log.error("Error occured while deleting a journal",e);
            return false;
        }

    }

    public void deleteAll(){
        journalEntryRepository.deleteAll();
    }


}
