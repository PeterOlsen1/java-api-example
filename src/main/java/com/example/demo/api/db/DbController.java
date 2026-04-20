package com.example.demo.api.db;

import java.util.Map;
import java.util.HashMap;
import java.time.Instant;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, String>> handleDbDel(@RequestParam(defaultValue = "") String key) {
        // 400 for invalid request
        if (key.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Missing 'key' parameter"));
        }
        if (!this.internalDb.containsKey(key)){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "No such key exists"));
        }


        this.internalDb.remove(key);
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "timestamp", Instant.now().toString()
        ));
    }

    @GetMapping("/db/getAll")
    public Map<String, String> handleDbGetAll(){
        return this.internalDb;
    }

    @GetMapping("db/getDbSize")
    public ResponseEntity<Map<String, Object>> getDbSize(){
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "timestamp", Instant.now().toString(),
                "message", String.format("Size of the Internal DB: %d", this.internalDb.size())
        ));
    }
}