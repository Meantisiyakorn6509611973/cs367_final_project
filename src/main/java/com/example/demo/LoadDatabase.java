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
            Box matchaBox = new Box();
            matchaBox.setBoxName("Matcha Lover Box");
            matchaBox.setBoxPrice(60);

            Item item1 = new Item("Matcha Macarons", 90);
            Item item2 = new Item("Matcha Brownie", 60);
            Item item3 = new Item("White Chocolate Matcha Cookies", 55);
            Item item4 = new Item("Red Bean Matcha Roll Cake", 75);
            Item item5 = new Item("Matcha Warabi Mochi", 65);

            // Persist items first (if using a separate table for @OneToMany)
            itemRepository.saveAll(Arrays.asList(item1, item2, item3, item4, item5));
        

            // Associate items with box
            matchaBox.setItems(Arrays.asList(item1, item2, item3, item4, item5));

            log.info("Loading " + boxRepository.save(matchaBox));
        };
    }
}
