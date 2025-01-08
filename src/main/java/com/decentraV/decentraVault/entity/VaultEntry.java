package com.decentraV.decentraVault.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "vault_entries")
@Data
@NoArgsConstructor
public class VaultEntry {
    @Id
    private ObjectId id;

    @NonNull
    private String title;

    private String content;

    private LocalDateTime date;

    // New field to store the file path of the uploaded PDF
    private String filePath;

    // Optional: A field for tags or categories (if needed)
    private String[] tags;

    // Uncomment this if sentiment analysis is needed in the future
    // private Sentiment sentiment;
}
