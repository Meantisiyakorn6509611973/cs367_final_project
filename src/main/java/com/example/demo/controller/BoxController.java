package com.example.demo.controller;

import com.example.demo.model.Box;
import com.example.demo.service.BoxService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boxes")
public class BoxController {

    private final BoxService boxService;

    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping
    public List<Box> getAllBoxes() {
        return boxService.getAllBoxes();
    }

    @PostMapping
    public Box createBox(@RequestBody Box box) {
        return boxService.saveBox(box);
    }

    @GetMapping("/theme/{theme}")
    public List<Box> getBoxesByTheme(@PathVariable String theme) {
        return boxService.getBoxesByTheme(theme);
    }
}
