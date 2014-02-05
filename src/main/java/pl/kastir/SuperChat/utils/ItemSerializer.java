package pl.kastir.SuperChat.utils;

import org.bukkit.inventory.ItemStack;

import pl.kastir.SuperChat.utils.reflection.ReflectionUtil;
import pl.kastir.SuperChat.utils.reflection.ReflectionUtil.DynamicPackage;

public class ItemSerializer {

    private static Class<?> nbtClass;
    private static Class<?> craftItemStackClass;
    private static Class<?> itemStackClass;
    private static Class<?> itemClass;

    static {
        try {
            nbtClass = ReflectionUtil.getClass("NBTTagCompound", DynamicPackage.MINECRAFT_SERVER);
            craftItemStackClass = ReflectionUtil.getClass("inventory.CraftItemStack", DynamicPackage.CRAFTBUKKIT);
            itemStackClass = ReflectionUtil.getClass("ItemStack", DynamicPackage.MINECRAFT_SERVER);
            itemClass = ReflectionUtil.getClass("Item", DynamicPackage.MINECRAFT_SERVER);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public static String getJson(ItemStack i) {
        String json = "";
        try {
            Object nbt = nbtClass.newInstance();
            Object item = ReflectionUtil.getMethod("asNMSCopy", craftItemStackClass, ItemStack.class).invoke(null, i);
            if (item == null) item = itemStackClass.getConstructor(itemClass, Integer.class, Integer.class).newInstance(itemClass.getMethod("d", Integer.class).invoke(null, i.getTypeId()), 1, i.getData().getData());
            nbt = ReflectionUtil.getMethod("save", itemStackClass, nbtClass).invoke(item, nbt);
            json = nbt.toString();
//            NBTTagCompound nbt = new NBTTagCompound();
//            net.minecraft.server.v1_7_R1.ItemStack item = CraftItemStack.asNMSCopy(i);
//            if (item == null) item = new net.minecraft.server.v1_7_R1.ItemStack(Item.d(i.getTypeId()), 1, i.getData().getData());
//            nbt = item.save(nbt);
//            json = nbt.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

}
