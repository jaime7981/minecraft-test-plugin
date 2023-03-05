package xyz.developmentcl.commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemRegistry {
    private 
    Gson gson = new Gson();
    private HashMap<String,Integer> items;

    public ItemRegistry() {
        try {
            TypeToken<HashMap<String, Integer>> typeToken = new TypeToken<HashMap<String, Integer>>() {};
            String name = new File(".").getCanonicalPath();
            String pathToJson = "plugins";
            File file = new File(name, "/" + pathToJson + "/"+ "ItemsCost.json");

            BufferedReader br = new BufferedReader(new FileReader(file));
            this.items = gson.fromJson(br, typeToken);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ItemStack getItem(String itemName) {
        if (!items.containsKey(itemName)) {
            return null;
        }
        Material selectedMaterial = Material.getMaterial(itemName);
        ItemStack selectedItem = new ItemStack(selectedMaterial, 1);
        return selectedItem;
    }

    public int getItemCost(ItemStack item) {
        return items.get(item.getType().toString());
    }

    public void showItems(Player player) {
        for (String key : items.keySet()) {
            player.sendMessage(ChatColor.GREEN + key + ChatColor.RED + " -> " + ChatColor.BLUE + items.get(key));
        }
    }
}