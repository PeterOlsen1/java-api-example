package com.example.demo.api;

import java.util.Map;
import java.time.Instant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class IndexController {
    @GetMapping("/")
    public String handleIndex() {
        return "Hello from my spring application!";
    }

    @GetMapping("/returnMyNumber")
    public Map<String, String> returnMyNumber(@RequestParam(defaultValue = "4") String number) {
        return Map.of(
            "number", number,
            "timestamp", Instant.now().toString()
        );
    } 
}