package gg.matt.generic.join;

import gg.matt.util.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.message.ChatPosition;
import net.minestom.server.message.Messenger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class Join {
    public static void setSkin(Player player) {
        String uuid = Utils.returnStream("https://api.mojang.com/users/profiles/minecraft/"
                + player.getUsername()).get("id").toString();
        JSONObject skin = Utils.returnStream("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid
                + "?unsigned=false");
        JSONArray properties = (JSONArray) skin.get("properties");
        String textureValue = null;
        String signature = null;
        for (Object property : properties) {
            JSONObject propertyObject = (JSONObject) property;
            textureValue = propertyObject.get("value").toString();
            signature = propertyObject.get("signature").toString();
        }
        if (textureValue != null && signature != null) {
            PlayerSkin completeSkin = new PlayerSkin(textureValue, signature);
            player.setSkin(completeSkin);
        }
    }

    public static void sendWelcomeMessage(Player player, InstanceContainer instanceContainer) {
        Component component = Component.text("Player ").color(TextColor.color(210, 210, 233))
                .append(Component.text(player.getUsername())
                        .decorate(TextDecoration.BOLD).color(TextColor.color(210, 220, 213)))
                .append(Component.text(" has joined the server!").color(TextColor.color(210, 210, 233)));
        Set<Player> playersToSend = new HashSet<>(instanceContainer.getPlayers());
        playersToSend.add(player);
        Messenger.sendMessage(playersToSend, component, ChatPosition.CHAT, null);
    }
}
