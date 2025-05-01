package com.example.demo.service;

import com.example.demo.model.History;
import com.example.demo.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void recordItemPurchase(String itemName, String boxName) {
        History history = new History(itemName, boxName, LocalDateTime.now());
        historyRepository.save(history);
    }

    public List<History> getAllHistory() {
        return historyRepository.findAll();
    }
}
