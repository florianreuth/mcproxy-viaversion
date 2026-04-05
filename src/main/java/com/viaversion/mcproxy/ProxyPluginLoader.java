/*
 * This file is part of mcproxy-viaversion - https://github.com/florianreuth/mcproxy-viaversion
 * Copyright (C) 2026 Florian Reuth <git@florianreuth.de> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.viaversion.mcproxy;

import com.viaversion.mcproxy.platform.ProxyPlatform;
import com.viaversion.mcproxy.platform.ProxyPlatformLoader;
import com.viaversion.mcproxy.platform.ProxyViaInjector;
import com.viaversion.mcproxy.service.ProtocolDetectorService;
import com.viaversion.viabackwards.ViaBackwardsPlatformImpl;
import com.viaversion.viarewind.ViaRewindPlatformImpl;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.commands.ViaCommandHandler;
import dev.outfluencer.mcproxy.api.plugin.Plugin;
import java.nio.file.Path;
import java.util.logging.Logger;

public final class ProxyPluginLoader {

    public static void onEnable(final Plugin plugin) {
        final Path pluginFolder = plugin.getPluginFolder();
        final Logger logger = plugin.getLogger();

        final ProxyPluginConfig proxyConfig = new ProxyPluginConfig(pluginFolder.resolve("config.yml").toFile(), logger);
        final ProxyPlatform platform = new ProxyPlatform(pluginFolder.toFile(), logger, new ProtocolDetectorService(proxyConfig));
        ViaManagerImpl.initAndLoad(platform, new ProxyViaInjector(), new ViaCommandHandler(false), new ProxyPlatformLoader(), () -> {
            proxyConfig.reload();

            if (hasClass("com.viaversion.viabackwards.api.ViaBackwardsPlatform")) {
                logger.info("Found ViaBackwards, loading it");
                new ViaBackwardsPlatformImpl();
            }
            if (hasClass("com.viaversion.viarewind.api.ViaRewindPlatform")) {
                logger.info("Found ViaRewind, loading it");
                new ViaRewindPlatformImpl();
            }
        });
    }

    private static boolean hasClass(final String name) {
        try {
            Class.forName(name);
            return true;
        } catch (final ClassNotFoundException ignored) {
            return false;
        }
    }

}
