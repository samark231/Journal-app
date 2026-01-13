package com.samar.Journal_app.service;

import com.samar.Journal_app.dto.CreateJournalEntryRequest;
import com.samar.Journal_app.dto.JournalEntryResponse;
import com.samar.Journal_app.entity.JournalEntry;
import com.samar.Journal_app.exception.JournalNotFoundException;
import com.samar.Journal_app.repository.JournalEntryRepository;
import com.samar.Journal_app.repository.JournalEntryRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class JournalEntryService {
    private final UserService userService;
    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryRepositoryImpl journalEntryRepositoryImpl;

    @Transactional
    public JournalEntryResponse saveNewEntry(CreateJournalEntryRequest newEntry, String username){
        JournalEntry entryToSave = new JournalEntry();
        entryToSave.setTitle(newEntry.getTitle());
        entryToSave.setUsername(username);
        if(newEntry.getContent()!=null){// allowing user to set only whitespaces as content as well.
            entryToSave.setContent(newEntry.getContent());
        }
        entryToSave.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(entryToSave);
        userService.addJournalEntryToUser(username, saved.getId());
        return  JournalEntryResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .date(saved.getDate())
                .build();

    }
    public List<JournalEntry> getAllJournalEntriesOfUser(String username){
        return journalEntryRepositoryImpl.fetchAllEntriesOfUser(username);
    }
    public JournalEntryResponse getJournalById(ObjectId journalId, String username){
        List<JournalEntry> entries = journalEntryRepositoryImpl.getJournalById(journalId, username);
        if(entries.isEmpty()){
            throw new JournalNotFoundException("No Journal found with Id: "+journalId);
        }
        JournalEntry entryToReturn = entries.get(0);
        return JournalEntryResponse.builder()
                .id(entryToReturn.getId())
                .title(entryToReturn.getTitle())
                .content(entryToReturn.getContent())
                .date(entryToReturn.getDate())
                .build();
    }

    @Transactional
    public void deleteEntry(String username, ObjectId journalId){
        Long delCount = userService.deleteJournalEntryFromUser(username, journalId);
        if(!delCount.equals(0L)){
            Long count = journalEntryRepositoryImpl.deleteJournalById(journalId, username);
            if(count==0){
                throw new JournalNotFoundException("Journal not found is journal's entry collection with id: "+journalId);
            }
        }else{
            throw new JournalNotFoundException("Journal Not found in user's collection with id: "+journalId);
        }
    }
    public JournalEntryResponse updateJournalById(ObjectId journalId,String username, CreateJournalEntryRequest newEntry){
        JournalEntry entryToUpdate = new JournalEntry();
        entryToUpdate.setTitle(newEntry.getTitle());
        entryToUpdate.setContent(newEntry.getContent());
        entryToUpdate.setDate(LocalDateTime.now());
        Boolean updated = journalEntryRepositoryImpl.updateJournalById(journalId, username, entryToUpdate);
        if(updated){
            return JournalEntryResponse.builder()
                    .id(journalId)
                    .title(entryToUpdate.getTitle())
                    .content(entryToUpdate.getContent())
                    .date(entryToUpdate.getDate())
                    .build();
        }else{
            throw new JournalNotFoundException("Could not update entry with id: "+journalId);
        }
    }
    public JournalEntry saveEntry(JournalEntry journalEntry){
       return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllJournals() {
        return journalEntryRepository.findAll();
    }


    public void deleteAll(){
        journalEntryRepository.deleteAll();
    }


}
