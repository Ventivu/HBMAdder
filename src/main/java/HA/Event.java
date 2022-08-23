package HA;

import HA.Config.Config;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class Event {

    public Event() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        if (Config.first.getBoolean()) {
            event.player.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("Chat.configwarn.1", EnumChatFormatting.YELLOW, event.player.getDisplayName(),EnumChatFormatting.RED)));
            event.player.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("Chat.configwarn.2",EnumChatFormatting.RED)));
        }
    }
}
