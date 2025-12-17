package com.samar.Journal_app.service;

import com.samar.Journal_app.entity.JournalEntry;
import com.samar.Journal_app.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class JournalEntryService {
    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

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

    public JournalEntry getEntryById(ObjectId myId) {
        return journalEntryRepository.findById(myId).orElse(null);
    }

    @Transactional
    public boolean deleteEntry(String username, ObjectId journalId){
        Long delCount = userService.deleteJournalEntryFromUser(username, journalId);
        if(!delCount.equals(0L)){
            journalEntryRepository.deleteJournalById(journalId);
            return true;
        }else{
            return false;
        }
    }


}
