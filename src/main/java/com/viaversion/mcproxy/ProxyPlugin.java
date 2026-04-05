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

import dev.outfluencer.mcproxy.api.plugin.Plugin;
import java.io.File;
import java.net.MalformedURLException;
import net.lenni0451.reflect.ClassLoaders;

public final class ProxyPlugin extends Plugin {

    @Override
    public void onEnable() {
        final File pluginFolder = getPluginFolder().toFile();
        pluginFolder.mkdirs();
        loadImplementation(pluginFolder);

        ProxyPluginLoader.onEnable(this);
    }

    private void loadImplementation(final File pluginFolder) {
        final File[] files = pluginFolder.listFiles();
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("You need to place the main ViaVersion jar in plugins/ViaVersion/");
        }

        boolean found = false;
        try {
            for (final File file : files) {
                if (file.getName().endsWith(".jar")) {
                    ClassLoaders.loadToFront(file.toURI().toURL());
                    found = true;
                }
            }
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }

        if (!found) {
            throw new IllegalArgumentException("You need to place the main ViaVersion jar in plugins/ViaVersion/");
        }
    }

}
