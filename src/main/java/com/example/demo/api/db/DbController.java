package com.example.demo.api.db;

import java.util.Map;
import java.util.HashMap;
import java.time.Instant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
public class DbController {
    private Map<String, String> internalDb;
    
    public DbController() {
        this.internalDb = new HashMap<>();
    }

    @GetMapping("/db/get")
    public Map<String, String> handleDbGet(@RequestParam(defaultValue = "") String key) {
        // 400 for invalid request
        if (key.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing 'key' parameter");
        }

        // 404 for non-existent key
        if (!this.internalDb.containsKey(key)) {
            String errString = String.format("Key %s not found", key);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errString);
        }

        String result = this.internalDb.get(key);
        return Map.of(
            "value", result,
            "timestamp", Instant.now().toString()
        );
    }

    @PutMapping("/db/set")
    public Map<String, String> handleDbSet(@RequestBody Map<String, String> body) {
        if (!body.containsKey("key") || !body.containsKey("value")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter");
        }

        String key = body.get("key");
        String value = body.get("value");

        if (key.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Key is blank");
        }

        this.internalDb.put(key, value);
        return Map.of(
            "status", "success",
            "timestamp", Instant.now().toString()
        );
    }

    @DeleteMapping("/db/del")
    public Map<String, String> handleDbDel(@RequestParam(defaultValue = "") String key) {
        // 400 for invalid request
        if (key.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing 'key' parameter");
        }

        this.internalDb.remove(key);
        return Map.of(
            "status", "success",
            "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/db/getAll")
    public Map<String, String> handleDbGetAll() {
        return this.internalDb;
    }
}