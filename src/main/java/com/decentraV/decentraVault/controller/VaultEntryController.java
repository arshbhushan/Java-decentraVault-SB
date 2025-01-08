package com.decentraV.decentraVault.controller;

import com.decentraV.decentraVault.entity.User;
import com.decentraV.decentraVault.entity.VaultEntry;
import com.decentraV.decentraVault.service.UserService;
import com.decentraV.decentraVault.service.VaultEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vault")
public class VaultEntryController {

    @Autowired
    private VaultEntryService vaultEntryService;

    @Autowired
    private UserService userService;

    /**
     * Get all vault entries of the authenticated user.
     */
    @GetMapping
    public ResponseEntity<List<VaultEntry>> getAllVaultEntries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUsername(userName);

        if (user != null && user.getVaultEntries() != null && !user.getVaultEntries().isEmpty()) {
            return new ResponseEntity<>(user.getVaultEntries(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Create a new vault entry for the authenticated user.
     */
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<VaultEntry> createVaultEntry(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("tags") String tags,
            @RequestParam("file") MultipartFile file) {
        try {
            // Get the authenticated user's name
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            // Create a new VaultEntry object
            VaultEntry vaultEntry = new VaultEntry();
            vaultEntry.setTitle(title);
            vaultEntry.setContent(content);
            vaultEntry.setTags(tags.split(",")); // Assuming tags are passed as a comma-separated string

            // Save the file to the server
            if (file != null && !file.isEmpty()) {
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);

                // Create directory if it doesn't exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Save the file with a unique name
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath);

                // Set the file path in the VaultEntry
                vaultEntry.setFilePath(filePath.toString());
            }

            // Save the VaultEntry
            vaultEntryService.saveEntry(vaultEntry, userName);

            // Return the created VaultEntry in the response
            return new ResponseEntity<>(vaultEntry, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Get a specific vault entry by ID for the authenticated user.
     */
    @GetMapping("/{entryId}")
    public ResponseEntity<VaultEntry> getVaultEntryById(@PathVariable ObjectId entryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<VaultEntry> vaultEntry = vaultEntryService.findByIdAndUser(entryId, userName);

        if (vaultEntry.isPresent()) {
            return new ResponseEntity<>(vaultEntry.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Update a specific vault entry by ID for the authenticated user.
     */
    @PutMapping("/{entryId}")
    public ResponseEntity<VaultEntry> updateVaultEntry(@PathVariable ObjectId entryId, @RequestBody VaultEntry updatedEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Optional<VaultEntry> existingEntry = vaultEntryService.findByIdAndUser(entryId, userName);
        if (existingEntry.isPresent()) {
            VaultEntry vaultEntry = existingEntry.get();
            vaultEntry.setTitle(updatedEntry.getTitle() != null ? updatedEntry.getTitle() : vaultEntry.getTitle());
            vaultEntry.setContent(updatedEntry.getContent() != null ? updatedEntry.getContent() : vaultEntry.getContent());
            vaultEntryService.saveEntry(vaultEntry);
            return new ResponseEntity<>(vaultEntry, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Delete a specific vault entry by ID for the authenticated user.
     */
    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> deleteVaultEntry(@PathVariable ObjectId entryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        boolean deleted = vaultEntryService.deleteById(entryId, userName);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

