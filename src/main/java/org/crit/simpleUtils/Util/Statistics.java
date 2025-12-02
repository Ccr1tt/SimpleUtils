package org.crit.simpleUtils.Util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class Statistics {

    public static List<Long> GetMemory(){

        // i dont fucking know what im doing 🤣🤣🤣
        long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long usedMemory = freeMemory - maxMemory / (1024 * 1024);

        return List.of(maxMemory, freeMemory, usedMemory);
    }
    public static Number GetPlayers(){
        int count = Bukkit.getOnlinePlayers().size();
        return  count;
    }

}
