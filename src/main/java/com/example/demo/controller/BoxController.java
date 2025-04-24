package com.example.demo.controller;

import com.example.demo.model.Box;
import com.example.demo.model.Item;
import com.example.demo.service.BoxService;
import com.example.demo.dto.BoxPurchaseRequest;
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

    @GetMapping("/boxname/{boxname}")
    public List<Box> getBoxesByBoxName(@PathVariable String boxname) {
        return boxService.getBoxesByBoxName(boxname);
    }

    @GetMapping("/boxname/{boxname}/random")
    public List<Item> getRandomItems(
            @PathVariable String boxname,
            @RequestParam(defaultValue = "1") int count) {
        return boxService.getRandomItemsFromBoxName(boxname, count);
    }

    @PostMapping("/purchase/{boxname}")
    public List<Item> purchaseBoxes(
            @PathVariable String boxname,
            @RequestParam(defaultValue = "1") int quantity) {
        return boxService.purchaseBoxesByBoxName(boxname, quantity);
    }

    @PostMapping("/purchase")
    public List<Item> purchaseMultipleThemes(@RequestBody List<BoxPurchaseRequest> boxRequests) {
        return boxService.purchaseMultipleBoxes(boxRequests);
    }
}
