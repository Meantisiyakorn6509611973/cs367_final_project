package com.example.demo.controller;

import com.example.demo.model.Box;
import com.example.demo.model.Item;
import com.example.demo.service.BoxService;
import com.example.demo.dto.BoxPurchaseRequest;
import com.example.demo.dto.PurchaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/boxname/{boxName}")
    public List<Box> getBoxesByBoxName(@PathVariable String boxName) {
        return boxService.getBoxesByBoxName(boxName);
    }

    @PostMapping("/purchase/{boxName}")
    public PurchaseResponse purchaseBoxes(
            @PathVariable String boxName,
            @RequestParam(defaultValue = "1") int quantity) {
        List<Item> purchasedItems = boxService.purchaseBoxesByBoxName(boxName, quantity);
        List<String> itemNames = purchasedItems.stream()
                .map(Item::getItemName)
                .collect(Collectors.toList());
        return new PurchaseResponse(itemNames);
    }

    @PostMapping("/purchase")
    public PurchaseResponse purchaseMultipleThemes(@RequestBody List<BoxPurchaseRequest> boxRequests) {
        List<Item> allItems = boxService.purchaseMultipleBoxes(boxRequests);
        List<String> itemNames = allItems.stream()
                .map(Item::getItemName)
                .collect(Collectors.toList());
        return new PurchaseResponse(itemNames);
    }
}
