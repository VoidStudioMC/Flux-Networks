package sonar.fluxnetworks.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import sonar.fluxnetworks.api.network.IFluxNetwork;
import sonar.fluxnetworks.api.network.NetworkMember;
import sonar.fluxnetworks.common.capabilities.DefaultSuperAdmin;
import sonar.fluxnetworks.common.connection.FluxNetworkCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Client -> Server request to compute which networks the requesting player is a member of.
 * This avoids sending full member lists to the client.
 */
public class PacketNetworkMembershipRequest implements IMessageHandler<PacketNetworkMembershipRequest.MembershipRequestMessage, IMessage> {

    @Override
    public IMessage onMessage(MembershipRequestMessage message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().player;
        UUID uuid = player.getUniqueID();

        if (DefaultSuperAdmin.canActivateSuperAdmin(player)) {
            return new PacketNetworkMembershipUpdate.MembershipUpdateMessage(new ArrayList<>(message.networkIds));
        }

        List<Integer> memberNetworkIds = new ArrayList<>();
        for (int networkId : message.networkIds) {
            IFluxNetwork network = FluxNetworkCache.instance.getNetwork(networkId);
            if (network.isInvalid()) {
                continue;
            }
            Optional<NetworkMember> member = network.getValidMember(uuid);
            if (member.isPresent() && member.get().getAccessPermission().canAccess()) {
                memberNetworkIds.add(networkId);
            }
        }
        return new PacketNetworkMembershipUpdate.MembershipUpdateMessage(memberNetworkIds);
    }

    public static class MembershipRequestMessage implements IMessage {

        public List<Integer> networkIds = new ArrayList<>();

        public MembershipRequestMessage() {
        }

        public MembershipRequestMessage(List<IFluxNetwork> networks) {
            networks.forEach(n -> networkIds.add(n.getNetworkID()));
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                networkIds.add(buf.readInt());
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(networkIds.size());
            networkIds.forEach(buf::writeInt);
        }
    }
}




