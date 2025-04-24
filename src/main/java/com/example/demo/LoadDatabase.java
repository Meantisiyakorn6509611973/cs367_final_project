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
import java.util.List;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(BoxRepository boxRepository, ItemRepository itemRepository) {
        return args -> {
            createBoxWithItems(boxRepository, itemRepository, "International Snack Box", 59, List.of(
                    new Item("KitKat Japan Green Tea", 75, 6),
                    new Item("Pepero Almond", 60, 5),
                    new Item("Milka Alpine Milk Chocolate", 85, 4),
                    new Item("Hi-Chew Mix Fruit", 50, 7),
                    new Item("Glico Collon Matcha", 65, 3)
            ));

            createBoxWithItems(boxRepository, itemRepository, "Bakery Delight Box", 59, List.of(
                    new Item("Butter Croissant", 55, 4),
                    new Item("Red Velvet Cupcake", 60, 5),
                    new Item("Mini Macaron Box", 90, 3),
                    new Item("Chocolate Fudge Brownie", 65, 6),
                    new Item("Fresh Milk Cream Bun", 50, 5)
            ));

            createBoxWithItems(boxRepository, itemRepository, "Healthy Snack Box", 59, List.of(
                    new Item("Granola Honey Crunch Bar", 45, 6),
                    new Item("Roasted Almond Mix", 55, 4),
                    new Item("Dried Mango Slices", 50, 5),
                    new Item("Chia Seed Oat Cookie", 40, 6),
                    new Item("Low Sugar Multigrain Biscuit", 45, 4)
            ));

            createBoxWithItems(boxRepository, itemRepository, "Traditional Thai Snack Box", 59, List.of(
                    new Item("ขนมเปี๊ยะไส้ถั่วไข่เค็ม", 60, 5),
                    new Item("ขนมหม้อแกงเผือก", 55, 4),
                    new Item("คุกกี้ข้าวแต๋นกรอบ", 50, 6),
                    new Item("ขนมฝอยทองม้วน", 45, 5),
                    new Item("ขนมเปี๊ยะไส้คัสตาร์ดไข่เค็ม", 65, 3)
            ));

            createBoxWithItems(boxRepository, itemRepository, "Chocolate Lover Box", 79, List.of(
                    new Item("Dark Chocolate with Sea Salt", 85, 4),
                    new Item("Belgian Chocolate Truffles", 95, 3),
                    new Item("Milk Chocolate with Hazelnuts", 75, 5),
                    new Item("Yuzu Citrus Chocolate", 90, 2),
                    new Item("Chocolate Raisin Bar", 60, 6)
            ));

            createBoxWithItems(boxRepository, itemRepository, "Matcha Lover Box", 79, Arrays.asList(
                    new Item("Matcha Macarons", 90, 5),
                    new Item("Matcha Brownie", 60, 4),
                    new Item("White Chocolate Matcha Cookies", 55, 3),
                    new Item("Red Bean Matcha Roll Cake", 75, 2),
                    new Item("Matcha Warabi Mochi", 65, 6)
            ));

            log.info("All themed boxes initialized.");
        };
    }

    private void createBoxWithItems(BoxRepository boxRepo, ItemRepository itemRepo, String boxName, int boxPrice, List<Item> items) {
        Box box = new Box();
        box.setBoxName(boxName);
        box.setBoxPrice(boxPrice);
        box = boxRepo.save(box);

        for (Item item : items) {
            item.setBox(box);
        }

        itemRepo.saveAll(items);
    }
}
