package com.decentraV.decentraVault.repository;

import com.decentraV.decentraVault.entity.VaultEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VaultEntryRepository extends MongoRepository<VaultEntry, ObjectId> {
}
