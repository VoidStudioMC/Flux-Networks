package sonar.fluxnetworks.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sonar.fluxnetworks.api.network.IFluxNetwork;
import sonar.fluxnetworks.api.network.NetworkSettings;
import sonar.fluxnetworks.common.connection.FluxNetworkCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketColorRequest implements IMessageHandler<PacketColorRequest.ColorRequestMessage, IMessage> {

    @Override
    public IMessage onMessage(ColorRequestMessage message, MessageContext ctx) {
        Map<Long, Tuple<Integer, String>> cache = new HashMap<>();
        if (!message.requests.isEmpty()) {
            for (long id : message.requests) {
                IFluxNetwork network = FluxNetworkCache.instance.getNetwork(id);
                cache.put(id, new Tuple<>(network.getSetting(NetworkSettings.NETWORK_COLOR) | 0xff000000, network.isInvalid() ? "NONE" : network.getSetting(NetworkSettings.NETWORK_NAME)));
            } // More than one
            return new PacketColorCache.ColorCacheMessage(cache);
        }
        return null;
    }

    public static class ColorRequestMessage implements IMessage {

        List<Long> requests;

        public ColorRequestMessage() {
        }

        public ColorRequestMessage(List<Long> requests) {
            this.requests = requests;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            requests = new ArrayList<>();
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                requests.add(buf.readLong());
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(requests.size());
            requests.forEach(buf::writeLong);
        }
    }
}
