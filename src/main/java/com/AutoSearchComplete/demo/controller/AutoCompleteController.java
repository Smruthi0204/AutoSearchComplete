package com.AutoSearchComplete.demo.controller;

import com.AutoSearchComplete.demo.service.AutoCompleteService;
import com.AutoSearchComplete.demo.dto.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class AutoCompleteController {

    private final AutoCompleteService service;

    @GetMapping("/autocomplete")
    public List<String> autocomplete(@RequestParam String query) {
        return service.autocomplete(query);
    }

    
    @PostMapping
    public ResponseEntity<Void> saveSearch(@RequestBody SearchRequest request) {
        service.recordSearch(request.getQuery());
        return ResponseEntity.ok().build();
    }
}
