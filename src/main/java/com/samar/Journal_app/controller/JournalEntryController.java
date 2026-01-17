package com.samar.Journal_app.controller;

import com.samar.Journal_app.dto.CreateJournalEntryRequest;
import com.samar.Journal_app.dto.HeatMapDto;
import com.samar.Journal_app.dto.JournalEntryResponse;
import com.samar.Journal_app.entity.JournalEntry;
import com.samar.Journal_app.repository.JournalEntryRepositoryImpl;
import com.samar.Journal_app.response.ApiResponse;
import com.samar.Journal_app.service.JournalEntryService;
import com.samar.Journal_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.shaded.com.google.protobuf.Api;
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


    @PostMapping
    public ResponseEntity<ApiResponse<JournalEntryResponse>> createEntry(@RequestBody @Valid CreateJournalEntryRequest newEntry, Authentication authentication){
        JournalEntryResponse createdEntry = journalEntryService.saveNewEntry(newEntry, authentication.getName());
        ApiResponse<JournalEntryResponse> response = new ApiResponse<>(true, "New Entry Created", createdEntry);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all-entries")
    public ResponseEntity<ApiResponse<List<JournalEntry>>> getAllEntriesOfUser(Authentication authentication){
        List<JournalEntry> allJournalEntriesOfUser = journalEntryService.getAllJournalEntriesOfUser(authentication.getName());
        String msg = "Fetched all entries of user Successfully";
        return new ResponseEntity<>(new ApiResponse<>(true, msg, allJournalEntriesOfUser), HttpStatus.OK);
    }

    @GetMapping("/id/{journalId}")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> getJournalById(Authentication authentication, @PathVariable ObjectId journalId){
        ApiResponse<JournalEntryResponse> response = new ApiResponse<>(true, "entry found", journalEntryService.getJournalById(journalId, authentication.getName()));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping("/id/{journalId}")
    public ResponseEntity<ApiResponse<Boolean>> DeleteJournalById(@PathVariable ObjectId journalId, Authentication authentication){
        journalEntryService.deleteEntry(authentication.getName(), journalId);
        return new ResponseEntity<>(new ApiResponse<>(true, "entry deleted Successfully.", true), HttpStatus.OK);
    }

    @PutMapping("/id/{journalId}")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> updateJournalById(@PathVariable ObjectId journalId, @RequestBody @Valid CreateJournalEntryRequest newEntry, Authentication authentication){
        JournalEntryResponse journalEntryResponse = journalEntryService.updateJournalById(journalId, authentication.getName(), newEntry);
        return  new ResponseEntity<>(new ApiResponse<>(true, "Entry updated successfully", journalEntryResponse), HttpStatus.OK);

    }
    @GetMapping("/heat-map")
    public ResponseEntity<ApiResponse<List<HeatMapDto>>> fetchHeatMapData(Authentication authentication){
        List<HeatMapDto> heatMapDtos = journalEntryService.fetchHeatMapData(authentication.getName());
        return new ResponseEntity<>(new ApiResponse<>(true, "HeatMap data fetched successfully",heatMapDtos ), HttpStatus.OK);
    }

}
