package com.example.securitybasicpract;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class TestController {


    @GetMapping("/test")
    public ResponseEntity<Collection<String>> getTestData() {
        List<String> names = List.of("data 1", "data 2", "data 3");
        return ok(names);
    }
}
