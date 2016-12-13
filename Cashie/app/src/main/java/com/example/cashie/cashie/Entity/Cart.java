package com.example.cashie.cashie.Entity;

import com.example.cashie.cashie.Controller.ItemController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 12/12/2016.
 */

public class Cart {
    private static int numItems = 0;
    private static double totalPrice = 0;
    private static List<ItemController.Item> itemList = new ArrayList<ItemController.Item>();

    public static void addToCart(ItemController.Item item) {
        ++numItems;
        totalPrice += item.price;
        itemList.add(item);
    }

    public static void removeFromCart(ItemController.Item item) {
        --numItems;
        totalPrice -= item.price;
        itemList.remove(item);
    }

    public static int getNumItems() {
        return numItems;
    }

    public static List<ItemController.Item> getItemList() {
        return itemList;
    }

    public static double getTotalPrice() {
        return totalPrice;
    }
}
