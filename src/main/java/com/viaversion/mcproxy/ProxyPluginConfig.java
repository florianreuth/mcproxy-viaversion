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

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.util.Config;
import dev.outfluencer.mcproxy.networking.protocol.registry.MinecraftVersion;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public final class ProxyPluginConfig extends Config {

    private Map<String, Integer> proxyServerProtocols;

    ProxyPluginConfig(final File configFile, final Logger logger) {
        super(configFile, logger);
    }

    @Override
    public void reload() {
        super.reload();

        proxyServerProtocols = get("proxy-servers", new HashMap<>());
    }

    @Override
    protected void handleConfig(final Map<String, Object> config) {
        final Map<String, Object> servers;
        if (config.get("proxy-servers") instanceof Map map) {
            servers = map;
        } else {
            servers = new HashMap<>();
        }

        for (Map.Entry<String, Object> entry : new HashSet<>(servers.entrySet())) {
            if (!(entry.getValue() instanceof Integer)) {
                if (entry.getValue() instanceof String protocol) {
                    ProtocolVersion found = ProtocolVersion.getClosest(protocol);
                    if (found != null) {
                        servers.put(entry.getKey(), found.getVersion());
                    } else {
                        servers.remove(entry.getKey()); // Remove!
                    }
                } else {
                    servers.remove(entry.getKey()); // Remove!
                }
            }
        }

        if (!servers.containsKey("default")) {
            servers.put("default", MinecraftVersion.SUPPORTED_VERSION.getFirst());
        }
        config.put("proxy-servers", servers);
    }

    @Override
    public Set<String> getSectionsWithModifiableKeys() {
        return Set.of("proxy-servers");
    }

    @Override
    public URL getDefaultConfigURL() {
        return getClass().getClassLoader().getResource("assets/mcproxy/config.yml");
    }

    @Override
    public InputStream getDefaultConfigInputStream() {
        return getClass().getClassLoader().getResourceAsStream("assets/mcproxy/config.yml");
    }

    /**
     * Get the listed server protocols in the config.
     * default will be listed as default.
     *
     * @return Map of String, Integer
     */
    public Map<String, Integer> getProxyServerProtocols() {
        return proxyServerProtocols;
    }

}
