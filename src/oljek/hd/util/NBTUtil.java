package oljek.hd.util;

import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTUtil {

    public ItemStack set(ItemStack stack, String path, NBTBase compound) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound stackCompound = (nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound());

        stackCompound.set(path, compound);
        nmsStack.setTag(stackCompound);

        stack = CraftItemStack.asBukkitCopy(nmsStack);
        return stack;
    }

    public NBTBase get(ItemStack stack, String path, NBTBase defaultValue) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound stackCompound = (nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound());

        if (!stackCompound.hasKey(path))
            return defaultValue;

        return stackCompound.get(path);
    }

}
