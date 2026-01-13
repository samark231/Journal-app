package com.samar.Journal_app.controller;

import com.samar.Journal_app.entity.JournalEntry;
import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.repository.JournalEntryRepositoryImpl;
import com.samar.Journal_app.response.ApiResponse;
import com.samar.Journal_app.service.JournalEntryService;
import com.samar.Journal_app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/journal")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    private final UserService userService;
    private final JournalEntryRepositoryImpl journalEntryRepositoryImpl;


    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry newEntry, Authentication authentication){
        try {
            String username = authentication.getName();
            if(newEntry.getTitle().trim().isEmpty() || newEntry.getContent().trim().isEmpty()){
                return new ResponseEntity<>("Need both title and description",HttpStatus.BAD_REQUEST);
            }else{
                JournalEntry entry = journalEntryService.saveNewEntry(newEntry, username);
                return new ResponseEntity<>(entry, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/all-entries")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getAllEntriesOfUser( Authentication authentication){
        log.info("get all journals request received");
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        List<JournalEntry> allEntries = user.getJournalEntries();
        ApiResponse<List<JournalEntry>> response = new ApiResponse<>(true,"", allEntries);
        if(allEntries!=null && (!allEntries.isEmpty())){
            response.setMessage("all entries sent successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            response.setMessage("No entries found");
            return new ResponseEntity<>(response,HttpStatus.OK);
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
        log.info("control reached in delete controller");
        String username = authentication.getName();
        boolean isRemoved = journalEntryService.deleteEntry(username, journalId);
        if(isRemoved) return new ResponseEntity<>("journal found and removed", HttpStatus.NO_CONTENT);
        return new ResponseEntity<>("journal not found", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/id/{journalId}")
    public ResponseEntity<?> UpdateJournalById(@PathVariable ObjectId journalId, @RequestBody JournalEntry newEntry){
        log.info("control reached in the update controller ");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(isJournalBlank(newEntry)){
            log.info("both fields empty");
            return ResponseEntity.badRequest().body("Both title and content can not be blank");
        }
        Boolean updated = journalEntryRepositoryImpl.updateJournalById(journalId, newEntry);
        if(updated){
            log.info("entry updated");
            newEntry.setId(journalId);
            return ResponseEntity.ok().body(newEntry);
        }else{
            log.info("not updated");
            return ResponseEntity.badRequest().body("Entry Not Updated");

        }

    }
    private Boolean isJournalBlank(JournalEntry entry){
        return entry.getTitle().isBlank()&& entry.getContent().isBlank();
    }

}
