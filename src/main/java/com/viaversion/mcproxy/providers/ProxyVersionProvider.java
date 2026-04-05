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

package com.viaversion.mcproxy.providers;

import com.viaversion.mcproxy.storage.CurrentServer;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.version.BaseVersionProvider;
import dev.outfluencer.mcproxy.networking.protocol.registry.MinecraftVersion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public final class ProxyVersionProvider extends BaseVersionProvider {

    @Override
    public ProtocolVersion getClosestServerProtocol(UserConnection user) throws Exception {
        if (user.isClientSide()) {
            return getBackProtocol(user);
        } else {
            return getFrontProtocol(user);
        }
    }

    private ProtocolVersion getBackProtocol(UserConnection user) {
        final String serverName = user.get(CurrentServer.class).server();
        return Via.proxyPlatform().protocolDetectorService().serverProtocolVersion(serverName);
    }

    private ProtocolVersion getFrontProtocol(UserConnection user) throws Exception {
        List<Integer> sorted = new ArrayList<>(MinecraftVersion.SUPPORTED_VERSION);
        Collections.sort(sorted);

        ProtocolInfo info = user.getProtocolInfo();

        // Proxy supports it
        final ProtocolVersion clientProtocolVersion = info.protocolVersion();
        if (new HashSet<>(sorted).contains(clientProtocolVersion.getVersion())) {
            return clientProtocolVersion;
        }

        // Older than proxy supports, get the lowest version
        if (clientProtocolVersion.getVersion() < sorted.getFirst()) {
            return Via.getManager().getInjector().getServerProtocolVersion();
        }

        // Loop through all protocols to get the closest protocol id that proxy supports (and that viaversion does too)
        for (Integer protocol : sorted.reversed()) {
            if (clientProtocolVersion.getVersion() > protocol && ProtocolVersion.isRegistered(protocol)) {
                return ProtocolVersion.getProtocol(protocol);
            }
        }

        Via.getPlatform().getLogger().severe("Panic, no protocol id found for " + clientProtocolVersion);
        return clientProtocolVersion;
    }

}
