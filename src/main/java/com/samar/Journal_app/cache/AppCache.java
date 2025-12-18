package com.samar.Journal_app.cache;

import com.samar.Journal_app.entity.ConfigJournalEntity;
import com.samar.Journal_app.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {
    public Map<String , String> appCache;
    public enum keys{
        WEATHER_API
    }

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    @PostConstruct
    public void init(){
        appCache = new HashMap<>();
        List<ConfigJournalEntity> all = configJournalAppRepository.findAll();
        for(ConfigJournalEntity configJournalEntity: all){
            appCache.put(configJournalEntity.getKey(), configJournalEntity.getValue());
        }
    }
}
