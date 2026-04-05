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
package com.viaversion.mcproxy.service;

import com.viaversion.mcproxy.ProxyPluginConfig;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.platform.AbstractProtocolDetectorService;
import dev.outfluencer.mcproxy.networking.protocol.registry.MinecraftVersion;
import java.util.Map;

public final class ProtocolDetectorService extends AbstractProtocolDetectorService {

    private final ProxyPluginConfig proxyConfig;

    public ProtocolDetectorService(final ProxyPluginConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    @Override
    public void probeAllServers() {
        Via.getPlatform().getLogger().severe("Probing servers for supported protocol versions is not supported. Please configure the server versions in the config.yml file.");
    }

    @Override
    protected Map<String, Integer> configuredServers() {
        return this.proxyConfig.getProxyServerProtocols();
    }

    @Override
    protected ProtocolVersion lowestSupportedProtocolVersion() {
        return ProtocolVersion.getProtocol(MinecraftVersion.SUPPORTED_VERSION.getFirst());
    }

}
