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

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectLinkedOpenHashSet;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.mcproxy.storage.CurrentServer;
import com.viaversion.viaversion.platform.ViaChannelInitializer;
import com.viaversion.viaversion.platform.ViaDecodeHandler;
import com.viaversion.viaversion.platform.ViaEncodeHandler;
import dev.outfluencer.mcproxy.api.ProxyServer;
import dev.outfluencer.mcproxy.api.events.CompressionChangeEvent;
import dev.outfluencer.mcproxy.api.events.unsafe.ChannelInitializedEvent;
import dev.outfluencer.mcproxy.networking.netty.HandlerNames;
import dev.outfluencer.mcproxy.networking.netty.handler.PacketHandler;
import dev.outfluencer.mcproxy.networking.protocol.registry.MinecraftVersion;
import dev.outfluencer.mcproxy.proxy.connection.ServerImpl;
import dev.outfluencer.mcproxy.proxy.connection.handler.login.ServerLoginPacketListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.lenni0451.lambdaevents.EventHandler;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.SortedSet;

public final class ProxyViaInjector implements ViaInjector {

    private static final MethodHandle GET_SERVER_INFO;

    static {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();

        try {
            final Class<?> serverLoginPacketListenerClass = Class.forName("dev.outfluencer.mcproxy.proxy.connection.handler.login.ServerLoginPacketListener");

            GET_SERVER_INFO = MethodHandles.privateLookupIn(serverLoginPacketListenerClass, lookup).findGetter(serverLoginPacketListenerClass, "server", ServerImpl.class);
        } catch (final ReflectiveOperationException e) {
            Via.getPlatform().getLogger().severe("Error initializing ProxyViaInjector, try updating mcproxy or ViaVersion!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void inject() {
        ProxyServer.getInstance().getEventManager().register(this);
    }

    @Override
    public void uninject() {
        ProxyServer.getInstance().getEventManager().unregister(this);
    }

    @EventHandler
    public void onChannelInitialized(final ChannelInitializedEvent event) throws Throwable {
        final Channel channel = event.channel();
        final boolean clientSide = event.type() == ChannelInitializedEvent.Type.BACKEND;
        final UserConnection connection = ViaChannelInitializer.createUserConnection(channel, clientSide);

        final ViaInjector injector = Via.getManager().getInjector();
        channel.pipeline().addBefore(HandlerNames.DECODER, injector.getDecoderName(), new ViaDecodeHandler(connection));
        channel.pipeline().addBefore(HandlerNames.ENCODER, injector.getEncoderName(), new ViaEncodeHandler(connection));

        if (clientSide) {
            final PacketHandler handler = (PacketHandler) channel.pipeline().get(HandlerNames.PACKET_HANDLER);
            final ServerLoginPacketListener listener = (ServerLoginPacketListener) handler.getPacketHandler();
            final ServerImpl serverImpl = (ServerImpl) GET_SERVER_INFO.invoke(listener);
            connection.put(new CurrentServer(serverImpl.getName()));
        }
    }

    @EventHandler
    public void onCompressionChange(final CompressionChangeEvent event) {
        final ChannelPipeline pipeline = event.connection().getUnsafe().getHandle().pipeline();
        ViaChannelInitializer.reorderPipeline(pipeline, HandlerNames.COMPRESS, HandlerNames.DECOMPRESS);
    }

    @Override
    public ProtocolVersion getServerProtocolVersion() {
        return ProtocolVersion.getProtocol(MinecraftVersion.SUPPORTED_VERSION.getFirst());
    }

    @Override
    public SortedSet<ProtocolVersion> getServerProtocolVersions() {
        final SortedSet<ProtocolVersion> versions = new ObjectLinkedOpenHashSet<>();
        for (final Integer version : MinecraftVersion.SUPPORTED_VERSION) {
            versions.add(ProtocolVersion.getProtocol(version));
        }
        return versions;
    }

    @Override
    public JsonObject getDump() {
        return null;
    }

}
