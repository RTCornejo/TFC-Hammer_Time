package net.rtc.hammertime.common.items;


import net.dries007.tfc.common.items.ToolItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rtc.hammertime.HammerTime;

import java.util.Map;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HammerTime.MOD_ID);

    public static final Map<Metal.Default, RegistryObject<Item>> SLEDGEHAMMERS = Helpers.mapOfKeys(Metal.Default.class,
            Metal.ItemType.PICKAXE::has,
            metal -> ITEMS.register("metal/sledgehammer/" + metal.getSerializedName(),
                    () -> new SledgeItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(1.0f, metal.toolTier()), -3.1f, BlockTags.MINEABLE_WITH_PICKAXE, new Item.Properties().rarity(metal.getRarity()))
                )
            );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }}

