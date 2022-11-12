// Copyright 2022 - 2022, ooxi and the log-player-health contributors
// SPDX-License-Identifier: libpng-2.0
package com.github.ooxi.logplayerhealth;

import com.mojang.logging.LogUtils;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import javax.annotation.CheckForNull;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;

/**
 * @warning The value here should match an entry in the
 *     {@code META-INF/mods.toml} file
 */
@Mod(LogPlayerHealth.MODID)
public class LogPlayerHealth {

	public static final String MODID = "logplayerhealth";

	private static final Logger LOGGER = LogUtils.getLogger();
//	private static final Map<EntityPlayer, Health> PLAYER_HEALTH = Collections.synchronizedMap(new WeakHashMap<>());
	private static final Map<Player, Health> PLAYER_HEALTH = Collections.<Player, Health>synchronizedMap(new WeakHashMap<Player, Health>());

	public LogPlayerHealth() {
		Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(EventHandler.class);
	}

	public static class EventHandler {

		@SubscribeEvent
		public static void onPlayerEvent(final PlayerEvent event) {
			final @CheckForNull Player player = event.getEntity();

			if (null == player) {
				return;
			}

			final @CheckForNull Health oldHealth = PLAYER_HEALTH.get(player);
			final Health newHealth = new Health(player.getHealth(), player.MAX_HEALTH);

			/* Player's health has not changed, no further action
			 * necessary
			 */
			if ((oldHealth != null) && (newHealth.equals(oldHealth))) {
				return;
			}

			/* Log and update new player health
			 */
			PLAYER_HEALTH.put(player, newHealth);
			LOGGER.info("{} player health changed {} / {} ({})", MODID, newHealth.current(), newHealth.max(), player.getName());
		}
	}
}
