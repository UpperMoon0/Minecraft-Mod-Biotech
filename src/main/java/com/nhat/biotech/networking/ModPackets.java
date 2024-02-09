package com.nhat.biotech.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation("biotech", "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(FluidHatchPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(FluidHatchPacket::new)
                .encoder(FluidHatchPacket::toBytes)
                .consumerMainThread(FluidHatchPacket::handle)
                .add();

        net.messageBuilder(EnergyPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergyPacket::new)
                .encoder(EnergyPacket::toBytes)
                .consumerMainThread(EnergyPacket::handle)
                .add();
        net.messageBuilder(BreederPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BreederPacket::new)
                .encoder(BreederPacket::toBytes)
                .consumerMainThread(BreederPacket::handle)
                .add();
    }
    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
