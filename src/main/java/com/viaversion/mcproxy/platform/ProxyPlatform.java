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

package com.viaversion.mcproxy.platform;

import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.ProtocolDetectorService;
import com.viaversion.viaversion.api.platform.ViaServerProxyPlatform;
import dev.outfluencer.mcproxy.api.ProxyServer;
import dev.outfluencer.mcproxy.api.connection.Player;
import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

public final class ProxyPlatform implements ViaServerProxyPlatform<Player> {

    private final ProxyViaAPI api = new ProxyViaAPI();

    private final File dataFolder;
    private final Logger logger;
    private final ProtocolDetectorService protocolDetectorService;
    private final ProxyViaConfig config;

    public ProxyPlatform(final File dataFolder, final Logger logger, final ProtocolDetectorService protocolDetectorService) {
        this.dataFolder = dataFolder;
        this.logger = logger;
        this.protocolDetectorService = protocolDetectorService;
        this.config = new ProxyViaConfig(getDataFolder(), getLogger());
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public String getPlatformName() {
        return ProxyServer.getInstance().getName();
    }

    @Override
    public String getPlatformVersion() {
        return ProxyServer.getInstance().getVersion();
    }

    @Override
    public boolean isProxy() {
        return true;
    }

    @Override
    public boolean kickPlayer(final UserConnection connection, final String message) {
        final UUID uuid = connection.getProtocolInfo().getUuid();
        if (uuid == null) {
            return false;
        }

        final Player player = ProxyServer.getInstance().getPlayer(uuid);
        if (player != null) {
            player.disconnect(message);
        }
        return true;
    }

    @Override
    public ViaAPI<Player> getApi() {
        return api;
    }

    @Override
    public ViaVersionConfig getConf() {
        return config;
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public ProtocolDetectorService protocolDetectorService() {
        return protocolDetectorService;
    }

}
