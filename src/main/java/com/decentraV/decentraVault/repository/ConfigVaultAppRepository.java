package com.decentraV.decentraVault.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.bson.types.ObjectId;
import com.decentraV.decentraVault.entity.ConfigVaultAppEntity;

public interface ConfigVaultAppRepository extends MongoRepository<ConfigVaultAppEntity,ObjectId> {
}
