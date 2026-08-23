package sonar.fluxnetworks.api.tiles;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import sonar.fluxnetworks.api.network.ConnectionType;
import sonar.fluxnetworks.api.network.IFluxNetwork;
import sonar.fluxnetworks.api.network.INetworkConnector;
import sonar.fluxnetworks.api.network.ITransferHandler;
import sonar.fluxnetworks.api.utils.Coord4D;
import sonar.fluxnetworks.api.utils.NBTType;
import sonar.fluxnetworks.common.connection.FluxNetworkServer;
import sonar.fluxnetworks.common.core.FluxGuiStack;

import java.util.UUID;

/**
 * Extended by IFluxPoint and IFluxPlug
 */
public interface IFluxConnector extends INetworkConnector, ITileByteBuf {

    NBTTagCompound writeCustomNBT(NBTTagCompound tag, NBTType type);

    void readCustomNBT(NBTTagCompound tag, NBTType type);

    int getLogicPriority();

    int getRawPriority(); // ignore surge

    UUID getConnectionOwner();

    ConnectionType getConnectionType();

    boolean canAccess(EntityPlayer player);

    long getLogicLimit();

    long getRawLimit(); // ignore disable limit

    /**
     * If this device is storage, this method returns the max energy storage of it,
     * or Long.MAX_VALUE otherwise
     *
     * @return max transfer limit
     */
    long getMaxTransferLimit();

    boolean isActive();

    boolean isChunkLoaded();

    boolean isForcedLoading();

    void connect(IFluxNetwork network);

    void disconnect(IFluxNetwork network);

    ITransferHandler getTransferHandler();

    World getFluxWorld();

    Coord4D getCoords();

    BlockPos getFluxPos();

    int getFolderID();

    String getCustomName();

    boolean getDisableLimit();

    boolean getSurgeMode();

    /**
     * Transfer handler is unavailable on client, this method is mainly used for gui display on client
     * If this device is storage, this method returns the energy stored of it
     *
     * @return internal buffer or energy stored
     */
    long getTransferBuffer();

    long getTransferChange();

    void setRawPriority(int priority);

    void setSurgeMode(boolean surgeMode);

    void setRawLimit(long limit);

    void setDisableLimit(boolean disableLimit);

    void setCustomName(String customName);

    void setForcedLoading(boolean chunkLoading);

    void setConnectionOwner(UUID owner);

    default void setChunkLoaded(boolean chunkLoaded) {
    }

    default ItemStack getDisplayStack() {
        switch (getConnectionType()) {
            case POINT:
                return FluxGuiStack.FLUX_POINT;
            case PLUG:
                return FluxGuiStack.FLUX_PLUG;
            case CONTROLLER:
                return FluxGuiStack.FLUX_CONTROLLER;
            default:
                return ItemStack.EMPTY;
        }
    }

    default void notifyFluxUpdate() {
    }

    @Override
    default void writePacket(ByteBuf buf, int id) {
        switch (id) {
            case 1:
                ByteBufUtils.writeUTF8String(buf, getCustomName());
                break;
            case 2:
                buf.writeInt(getRawPriority());
                break;
            case 3:
                buf.writeLong(getRawLimit());
                break;
            case 4:
                buf.writeBoolean(getSurgeMode());
                break;
            case 5:
                buf.writeBoolean(getDisableLimit());
                break;
        }
    }

    @Override
    default void readPacket(ByteBuf buf, int id) {
        switch (id) {
            case 1:
                setCustomName(ByteBufUtils.readUTF8String(buf));
                markLiteSettingChanged();
                break;
            case 2:
                setRawPriority(buf.readInt());
                sortNetworkConnections();
                break;
            case 3:
                setRawLimit(buf.readLong());
                markLiteSettingChanged();
                break;
            case 4:
                setSurgeMode(buf.readBoolean());
                sortNetworkConnections();
                break;
            case 5:
                setDisableLimit(buf.readBoolean());
                markLiteSettingChanged();
                break;
        }
    }

    default void sortNetworkConnections() {
        IFluxNetwork network = getNetwork();
        if (network instanceof FluxNetworkServer) {
            FluxNetworkServer fluxNetworkServer = (FluxNetworkServer) network;
            fluxNetworkServer.sortConnections = true;
            markLiteSettingChanged();
        }
    }

    default void markLiteSettingChanged() {
        IFluxNetwork network = getNetwork();
        if (network instanceof FluxNetworkServer) {
            FluxNetworkServer fluxNetworkServer = (FluxNetworkServer) network;
            fluxNetworkServer.markLiteSettingChanged(this);
        }
    }
}
