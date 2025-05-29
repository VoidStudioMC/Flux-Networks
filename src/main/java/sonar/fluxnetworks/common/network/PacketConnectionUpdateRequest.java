package sonar.fluxnetworks.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sonar.fluxnetworks.api.network.FluxLogicType;
import sonar.fluxnetworks.api.network.IFluxNetwork;
import sonar.fluxnetworks.api.tiles.IFluxConnector;
import sonar.fluxnetworks.api.utils.Coord4D;
import sonar.fluxnetworks.common.connection.FluxLiteConnector;
import sonar.fluxnetworks.common.connection.FluxNetworkCache;

import java.util.ArrayList;
import java.util.List;

public class PacketConnectionUpdateRequest implements IMessageHandler<PacketConnectionUpdateRequest.ConnectionRequestMessage, IMessage> {

    @Override
    public IMessage onMessage(ConnectionRequestMessage message, MessageContext ctx) {
        if (message.coords.isEmpty()) {
            return null;
        }
        IFluxNetwork network = FluxNetworkCache.instance.getNetwork(message.networkID);
        if (!network.isInvalid()) {
            List<NBTTagCompound> tags = new ArrayList<>();
            //noinspection unchecked
            List<IFluxConnector> onlineConnectors = network.getConnections(FluxLogicType.ANY);
            message.coords.forEach(c ->
                    onlineConnectors.stream().filter(f -> f.getCoords().equals(c)).findFirst()
                            .ifPresent(f -> tags.add(FluxLiteConnector.writeCustomNBT(f, new NBTTagCompound()))));
            return new PacketConnectionUpdate.NetworkConnectionMessage(message.networkID, tags);
        }
        return null;
    }

    public static class ConnectionRequestMessage implements IMessage {

        public long networkID;
        public List<Coord4D> coords = new ArrayList<>();

        public ConnectionRequestMessage() {

        }

        public ConnectionRequestMessage(long networkID, List<Coord4D> coords) {
            this.networkID = networkID;
            this.coords = coords;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            networkID = buf.readLong();
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                coords.add(new Coord4D(buf));
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeLong(networkID);
            buf.writeInt(coords.size());
            coords.forEach(c -> c.write(buf));
        }
    }
}
