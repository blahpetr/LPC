package com.infiniteplugins.lpc;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

final class PaperChatListener implements Listener {

    private final LPC plugin;

    PaperChatListener(final LPC plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(final AsyncChatEvent event) {
        final Player player = event.getPlayer();
        final String format = plugin.buildFormat(player);
        final String processedMessage = plugin.processMessage(player, LegacyComponentSerializer.legacyAmpersand().serialize(event.message()));
        final Component messageComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(processedMessage);

        Component formatComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(format)
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{message}")
                        .replacement(messageComponent)
                        .build()
                );

        // DO NOT call event.message(...). Only set the renderer.
        event.renderer(new ChatRenderer() {
            @Override
            public Component render(final Player source, final Component sourceDisplayName, final Component message, final Audience viewer) {
                return formatComponent;
            }
        });
    }
}
