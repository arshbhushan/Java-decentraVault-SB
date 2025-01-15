package com.decentraV.decentraVault.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "config_vault_app")
@Data
@NoArgsConstructor
public class ConfigVaultAppEntity {
    private String key;
    private String value;
}
