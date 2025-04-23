package com.example.demo.service;

import com.example.demo.model.Box;
import com.example.demo.repository.BoxRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BoxService {

    private final BoxRepository boxRepository;

    public BoxService(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }

    public List<Box> getAllBoxes() {
        return boxRepository.findAll();
    }

    public Box saveBox(Box box) {
        return boxRepository.save(box);
    }

    public List<Box> getBoxesByTheme(String theme) {
        return boxRepository.findByTheme(theme);
    }
}
