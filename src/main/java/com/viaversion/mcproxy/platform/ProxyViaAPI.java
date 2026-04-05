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

import com.viaversion.viaversion.ViaAPIBase;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import dev.outfluencer.mcproxy.api.connection.Player;
import io.netty.buffer.ByteBuf;

public final class ProxyViaAPI extends ViaAPIBase<Player> {

    @Override
    public ProtocolVersion getPlayerProtocolVersion(final Player player) {
        return getPlayerProtocolVersion(player.getUuid());
    }

    @Override
    public void sendRawPacket(final Player player, final ByteBuf packet) {
        sendRawPacket(player.getUuid(), packet);
    }

}
