package com.example.cashie.cashie.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ItemController {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Item> ITEMS = new ArrayList<Item>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Item> ITEM_MAP = new HashMap<String, Item>();

    private static final int COUNT = 10;
    private static final String TAG = "Men Content";

    public static void addItem(Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Item {
        public String id;
        public String name;
        public String size;
        public String maker;
        public String fabric;
        public double price;
        public String color;
        public String cut;
        public String seller;
        public String tag;

        public Item() {
        }

        public Item(String id, String name, String size, String maker, String fabric, double price, String color, String cut, String seller, String tag) {
            this.id = id;
            this.name = name;
            this.size = size;
            this.maker = maker;
            this.fabric = fabric;
            this.price = price;
            this.color = color;
            this.cut = cut;
            this.seller = seller;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
