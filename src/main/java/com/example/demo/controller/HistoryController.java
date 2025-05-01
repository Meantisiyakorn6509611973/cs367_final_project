package com.example.demo.controller;

import com.example.demo.model.History;
import com.example.demo.service.HistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<History> getAllHistory() {
        return historyService.getAllHistory();
    }
}
