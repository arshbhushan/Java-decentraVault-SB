package com.decentraV.decentraVault.repository;

import com.decentraV.decentraVault.entity.User;
import com.decentraV.decentraVault.entity.VaultEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VaultEntryRepository extends MongoRepository<VaultEntry, ObjectId> {
}
