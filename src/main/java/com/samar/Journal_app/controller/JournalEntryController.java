package com.samar.Journal_app.controller;

import com.samar.Journal_app.entity.JournalEntry;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.service.JournalEntryService;
import com.samar.Journal_app.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping("/all-entries")
    public ResponseEntity<?> getAllEntriesOfUser( Authentication authentication){
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        List<JournalEntry> all = user.getJournalEntries();
        if(all!=null && (!all.isEmpty())){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("No entry created yet",HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry newEntry, Authentication authentication){
        try {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);

            if(newEntry.getTitle().trim().isEmpty() || newEntry.getContent().trim().isEmpty()){
                return new ResponseEntity<>("Need both title and description",HttpStatus.BAD_REQUEST);
            }else{
                System.out.println("user is"+user);
                JournalEntry entry = journalEntryService.saveNewEntry(newEntry, username);
                return new ResponseEntity<>(entry, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/id/{journalId}")
    public ResponseEntity<?> getJournalById(Authentication authentication, @PathVariable ObjectId journalId){
        String username = authentication.getName();
        List<JournalEntry> allEntries = userService.getUserByUsername(username).getJournalEntries();
        List<JournalEntry> collect = allEntries.stream()
                .filter(entry->entry.getId().equals(journalId))
                .toList();
        if(collect.isEmpty()) return new ResponseEntity<>("journal not found.", HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(collect.get(0), HttpStatus.OK);

    }

    @DeleteMapping("/id/{journalId}")
    public ResponseEntity<?> DeleteJournalById(@PathVariable ObjectId journalId, Authentication authentication){
//        System.out.println("control reached in delete controller");
        String username = authentication.getName();
        boolean isRemoved = journalEntryService.deleteEntry(username, journalId);
        if(isRemoved) return new ResponseEntity<>("journal found and removed", HttpStatus.NO_CONTENT);
        return new ResponseEntity<>("journal not found", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/id/{journalId}")
    public ResponseEntity<?> UpdateJournalById(@PathVariable ObjectId journalId, @RequestBody JournalEntry newEntry){
        System.out.println("control reached in the update controller ");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JournalEntry> allEntries = userService.getUserByUsername(username).getJournalEntries();
        List<JournalEntry> collect = allEntries.stream()
                .filter(entry->entry.getId().equals(journalId))
                .toList();
        if(collect.isEmpty()) return new ResponseEntity<>("journal not found", HttpStatus.NOT_FOUND);
        JournalEntry oldEntry = collect.get(0);
        oldEntry.setTitle(newEntry.getTitle().trim().isEmpty()? oldEntry.getTitle() : newEntry.getTitle());
        oldEntry.setContent(newEntry.getContent()!=null && newEntry.getContent().trim().isEmpty()?oldEntry.getContent():newEntry.getContent());
        return new ResponseEntity<>(journalEntryService.saveEntry(oldEntry), HttpStatus.OK);
    }
}
