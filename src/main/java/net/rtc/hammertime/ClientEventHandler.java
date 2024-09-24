package net.rtc.hammertime;

import net.dries007.tfc.client.model.ContainedFluidModel;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.rtc.hammertime.common.items.ModItems;

public class ClientEventHandler {
    public static void init(){
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ClientEventHandler::registerColorHandlerItems);
    }

    private static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event) {
        event.register(new ContainedFluidModel.Colors(), ModItems.EXCAVATOR_HEAD_MOLD.get(), ModItems.SLEDGEHAMMER_HEAD_MOLD.get());
    }
}
