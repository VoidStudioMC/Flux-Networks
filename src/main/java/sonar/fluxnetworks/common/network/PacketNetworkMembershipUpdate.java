package sonar.fluxnetworks.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import sonar.fluxnetworks.common.connection.FluxNetworkCache;
import sonar.fluxnetworks.common.handler.PacketHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Server -> Client update with the set of network IDs that the client player is a member of.
 */
public class PacketNetworkMembershipUpdate implements IMessageHandler<PacketNetworkMembershipUpdate.MembershipUpdateMessage, IMessage> {

    @Override
    public IMessage onMessage(MembershipUpdateMessage message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            Set<Integer> memberIds = new HashSet<>(message.memberNetworkIds);
            PacketHandler.handlePacket(() -> FluxNetworkCache.instance.updateClientMemberNetworks(memberIds), ctx.netHandler);
        }
        return null;
    }

    public static class MembershipUpdateMessage implements IMessage {

        public List<Integer> memberNetworkIds = new ArrayList<>();

        public MembershipUpdateMessage() {
        }

        public MembershipUpdateMessage(List<Integer> memberNetworkIds) {
            this.memberNetworkIds = memberNetworkIds;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                memberNetworkIds.add(buf.readInt());
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(memberNetworkIds.size());
            memberNetworkIds.forEach(buf::writeInt);
        }
    }
}




