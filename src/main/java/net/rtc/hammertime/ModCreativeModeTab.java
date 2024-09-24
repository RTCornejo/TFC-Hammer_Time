package net.rtc.hammertime;


import net.dries007.tfc.common.TFCCreativeTabs;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.RegistryObject;
import net.rtc.hammertime.common.items.ModItems;

public class ModCreativeModeTab {
    public static void AddCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == TFCCreativeTabs.METAL.tab().getKey()) {
            for (Metal.Default metal : Metal.Default.values()) {
                RegistryObject<Item> sledgehammer = ModItems.SLEDGEHAMMERS.get(metal);
                RegistryObject<Item> excavator = ModItems.EXCAVATORS.get(metal);
                RegistryObject<Item> sledgehammer_heads = ModItems.SLEDGEHAMMER_HEADS.get(metal);
                RegistryObject<Item> excavator_heads = ModItems.EXCAVATOR_HEADS.get(metal);
                if (sledgehammer != null && sledgehammer.isPresent()) {
                    event.accept(sledgehammer);
                    event.accept(sledgehammer_heads);
                    event.accept(excavator);
                    event.accept(excavator_heads);
                }
            }
        }
        if (event.getTabKey() == TFCCreativeTabs.MISC.tab().getKey()){
            event.accept(ModItems.UNFIRED_EXCAVATOR_HEAD_MOLD.get());
            event.accept(ModItems.EXCAVATOR_HEAD_MOLD.get());
            event.accept(ModItems.UNFIRED_SLEDGEHAMMER_HEAD_MOLD.get());
            event.accept(ModItems.SLEDGEHAMMER_HEAD_MOLD.get());
        }
    }
}
