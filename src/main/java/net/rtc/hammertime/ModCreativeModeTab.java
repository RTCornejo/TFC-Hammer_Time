package net.rtc.hammertime;


import net.dries007.tfc.common.TFCCreativeTabs;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.RegistryObject;
import net.rtc.hammertime.common.items.ModItems;

public class ModCreativeModeTab {
    public static void AddCreative(BuildCreativeModeTabContentsEvent event){
        if (event.getTabKey() == TFCCreativeTabs.METAL.tab().getKey()){
            for (Metal.Default metal : Metal.Default.values()){
                RegistryObject<Item> item = ModItems.SLEDGEHAMMERS.get(metal);
                if (item != null && item.isPresent()){
                    event.accept(item);
                }
            }
        }
    }
}
