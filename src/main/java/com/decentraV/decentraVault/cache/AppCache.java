package com.decentraV.decentraVault.cache;

import com.decentraV.decentraVault.entity.ConfigVaultAppEntity;
import com.decentraV.decentraVault.repository.ConfigVaultAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys{
        WEATHER_API;
    }
    @Autowired
    private ConfigVaultAppRepository configJournalAppRepository;

    public Map<String, String> appCache;

    @PostConstruct
    public void init(){
        appCache = new HashMap<>();
        List<ConfigVaultAppEntity> all = configJournalAppRepository.findAll();
        for (ConfigVaultAppEntity configJournalAppEntity : all) {
            appCache.put(configJournalAppEntity.getKey(), configJournalAppEntity.getValue());
        }
    }
}
