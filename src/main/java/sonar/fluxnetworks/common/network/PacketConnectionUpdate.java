package sonar.fluxnetworks.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import sonar.fluxnetworks.common.connection.FluxNetworkCache;
import sonar.fluxnetworks.common.handler.PacketHandler;

import java.util.ArrayList;
import java.util.List;

public class PacketConnectionUpdate implements IMessageHandler<PacketConnectionUpdate.NetworkConnectionMessage, IMessage> {

    @Override
    public IMessage onMessage(NetworkConnectionMessage message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            PacketHandler.handlePacket(() -> FluxNetworkCache.instance.updateClientConnections(message.networkID, message.tags), ctx.netHandler);
        }
        return null;
    }

    public static class NetworkConnectionMessage implements IMessage {

        public long networkID;
        public List<NBTTagCompound> tags = new ArrayList<>();

        public NetworkConnectionMessage() {
        }

        public NetworkConnectionMessage(long networkID, List<NBTTagCompound> tags) {
            this.networkID = networkID;
            this.tags = tags;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            networkID = buf.readLong();
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                tags.add(ByteBufUtils.readTag(buf));
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeLong(networkID);
            buf.writeInt(tags.size());
            tags.forEach(t -> ByteBufUtils.writeTag(buf, t));
        }
    }
}
