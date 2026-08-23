package sonar.fluxnetworks.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import sonar.fluxnetworks.api.tiles.IFluxConnector;

public enum PacketTileType {
    SET_NETWORK(PacketTileHandler::handleSetNetworkPacket),
    CHUNK_LOADING(PacketTileHandler::handleChunkLoadPacket);

    public IPacketTileHandler handler;

    PacketTileType(PacketTileType.IPacketTileHandler handler) {
        this.handler = handler;
    }

    public interface IPacketTileHandler {
        IMessage handlePacket(IFluxConnector tile, EntityPlayer player, NBTTagCompound nbtTag);
    }
}
