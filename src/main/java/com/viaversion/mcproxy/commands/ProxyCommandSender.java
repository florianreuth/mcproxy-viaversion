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
package com.viaversion.mcproxy.commands;

import com.viaversion.viaversion.api.command.ViaCommandSender;
import dev.outfluencer.mcproxy.api.command.CommandSource;
import dev.outfluencer.mcproxy.api.connection.Player;
import java.util.UUID;

public record ProxyCommandSender(CommandSource source) implements ViaCommandSender {

    @Override
    public boolean hasPermission(String permission) {
        return source.hasPermission(permission);
    }

    @Override
    public void sendMessage(String msg) {
        source.sendMessage(msg);
    }

    @Override
    public UUID getUUID() {
        if (source instanceof Player player) {
            return player.getUuid();
        } else {
            return new UUID(0, 0);
        }
    }

    @Override
    public String getName() {
        return source.getName();
    }

}
