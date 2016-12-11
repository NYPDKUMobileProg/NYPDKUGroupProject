package com.example.cashie.cashie.Entity;

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
public class MenContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Item> ITEMS = new ArrayList<Item>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Item> ITEM_MAP = new HashMap<String, Item>();

    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Item createDummyItem(int position) {
        return new Item(String.valueOf(position), "Shirt " + position, "M", "Calvin Klein", 49.90);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Item {
        public final String id;
        public final byte[] photo = null;
        public final String name;
        public final String size;
        public final String maker;
        public final double price;

        public Item(String id, String name, String size, String maker, double price) {
            this.id = id;
            this.name = name;
            this.size = size;
            this.maker = maker;
            this.price = price;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
