package com.decentraV.decentraVault.service;

import com.decentraV.decentraVault.entity.User;
import com.decentraV.decentraVault.entity.VaultEntry;
import com.decentraV.decentraVault.repository.VaultEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VaultEntryService {

    @Autowired
    private VaultEntryRepository vaultEntryRepository;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(VaultEntryService.class);

    /**
     * Saves a new VaultEntry associated with a user.
     *
     * @param vaultEntry the VaultEntry to save
     * @param username   the username of the associated user
     */
    @Transactional
    public void saveEntry(VaultEntry vaultEntry, String username) {
        try {
            User user = userService.findByUsername(username);
            vaultEntry.setDate(LocalDateTime.now());
            VaultEntry savedEntry = vaultEntryRepository.save(vaultEntry);
            user.getVaultEntries().add(savedEntry);
            userService.saveNewUser(user);

            logger.info("Vault entry successfully saved for user: {}", username);
        } catch (Exception e) {
            logger.error("Failed to save vault entry for user: {}", username, e);
            throw new RuntimeException("Error saving vault entry", e);
        }
    }

    public void saveEntry(VaultEntry vaultEntry) {
        try {
            vaultEntryRepository.save(vaultEntry);
            logger.info("Vault entry successfully saved/updated with ID: {}", vaultEntry.getId());
        } catch (Exception e) {
            logger.error("Failed to save/update vault entry with ID: {}", vaultEntry.getId(), e);
            throw new RuntimeException("Error saving/updating vault entry", e);
        }
    }

    public List<VaultEntry> getAll() {
        return vaultEntryRepository.findAll();
    }

    public Optional<VaultEntry> findById(ObjectId id) {
        return vaultEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username) {
        try {
            User user = userService.findByUsername(username);
            boolean removed = user.getVaultEntries().removeIf(entry -> entry.getId().equals(id));

            if (removed) {
                userService.saveNewUser(user);
                vaultEntryRepository.deleteById(id);
                logger.info("Vault entry with ID: {} successfully deleted for user: {}", id, username);
                return true;
            } else {
                logger.warn("No Vault entry found with ID: {} for user: {}", id, username);
            }
        } catch (Exception e) {
            logger.error("Error deleting Vault entry with ID: {} for user: {}", id, username, e);
            throw new RuntimeException("Error deleting vault entry", e);
        }

        return false;
    }

    public Optional<VaultEntry> findByIdAndUser(ObjectId id, String username) {
        try {
            User user = userService.findByUsername(username);
            Optional<VaultEntry> vaultEntry = vaultEntryRepository.findById(id);

            if (vaultEntry.isPresent() && user.getVaultEntries().contains(vaultEntry.get())) {
                logger.info("Vault entry with ID: {} found for user: {}", id, username);
                return vaultEntry;
            } else {
                logger.warn("No Vault entry with ID: {} found for user: {}", id, username);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error finding Vault entry with ID: {} for user: {}", id, username, e);
            throw new RuntimeException("Error finding vault entry", e);
        }
    }
}