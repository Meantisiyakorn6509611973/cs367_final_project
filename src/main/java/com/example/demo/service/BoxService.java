package com.example.demo.service;

import com.example.demo.model.Box;
import com.example.demo.model.Item;
import com.example.demo.repository.BoxRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.dto.BoxPurchaseRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoxService {

    private final BoxRepository boxRepository;
    private final ItemRepository itemRepository;

    public BoxService(BoxRepository boxRepository, ItemRepository itemRepository) {
        this.boxRepository = boxRepository;
        this.itemRepository = itemRepository;
    }

    public List<Box> getAllBoxes() {
        return boxRepository.findAll();
    }

    public Box saveBox(Box box) {
        return boxRepository.save(box);
    }

    public List<Box> getBoxesByBoxName(String boxName) {
        return boxRepository.findByBoxName(boxName);
    }

    // Randomly select 1 or more items from a box and update stock
    public List<Item> getRandomItemsFromBoxName(String boxName, int numberOfItems) {
        List<Box> boxes = boxRepository.findByBoxName(boxName);
        if (boxes.isEmpty()) {
            throw new RuntimeException("No box found with boxName: " + boxName);
        }

        Box selectedBox = boxes.get(0);
        List<Item> items = selectedBox.getItems();

        List<Item> availableItems = items.stream()
            .filter(item -> item.getItemAmount() > 0)
            .collect(Collectors.toList());

        if (availableItems.size() < numberOfItems) {
            throw new RuntimeException("Not enough stock for box: " + boxName);
        }

        Collections.shuffle(availableItems);
        List<Item> selectedItems = availableItems.stream()
            .limit(numberOfItems)
            .collect(Collectors.toList());

        selectedItems.forEach(item -> {
            item.setItemAmount(item.getItemAmount() - 1);
            itemRepository.save(item);
        });

        return selectedItems;
    }

    // Purchase multiple boxes of one boxName (each box = 1 item)
    public List<Item> purchaseBoxesByBoxName(String boxName, int quantity) {
        List<Item> result = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            List<Item> randomOne = getRandomItemsFromBoxName(boxName, 1);
            if (!randomOne.isEmpty()) {
                result.addAll(randomOne);
            } else {
                throw new RuntimeException("Insufficient stock for box: " + boxName);
            }
        }
        return result;
    }

    // Purchase boxes from multiple themes
    public List<Item> purchaseMultipleBoxes(List<BoxPurchaseRequest> boxRequests) {
        List<Item> allItems = new ArrayList<>();
        for (BoxPurchaseRequest request : boxRequests) {
            allItems.addAll(purchaseBoxesByBoxName(request.getBoxName(), request.getQuantity()));
        }
        return allItems;
    }
}
