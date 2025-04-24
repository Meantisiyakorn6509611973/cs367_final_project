package com.example.demo;

import com.example.demo.model.Box;
import com.example.demo.model.Item;
import com.example.demo.repository.BoxRepository;
import com.example.demo.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(BoxRepository boxRepository, ItemRepository itemRepository) {
        return args -> {
            // Create the box
            Box matchaBox = new Box();
            matchaBox.setBoxName("Matcha Lover Box");
            matchaBox.setBoxPrice(299);

            // Save the box first so it gets an ID
            matchaBox = boxRepository.save(matchaBox);

            // Create items and associate them with the box
            Item item1 = new Item("Matcha Macarons", 90);
            item1.setItemAmount(5);
            item1.setBox(matchaBox); // ✅ assign box

            Item item2 = new Item("Matcha Brownie", 60);
            item2.setItemAmount(4);
            item2.setBox(matchaBox);

            Item item3 = new Item("White Chocolate Matcha Cookies", 55);
            item3.setItemAmount(3);
            item3.setBox(matchaBox);

            Item item4 = new Item("Red Bean Matcha Roll Cake", 75);
            item4.setItemAmount(2);
            item4.setBox(matchaBox);

            Item item5 = new Item("Matcha Warabi Mochi", 65);
            item5.setItemAmount(6);
            item5.setBox(matchaBox);

            // Save all items
            itemRepository.saveAll(Arrays.asList(item1, item2, item3, item4, item5));

            log.info("Box and items initialized successfully");
        };
    }
}
