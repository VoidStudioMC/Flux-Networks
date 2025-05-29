package sonar.fluxnetworks.common.connection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.api.network.ConnectionType;
import sonar.fluxnetworks.api.network.IFluxNetwork;
import sonar.fluxnetworks.api.network.ITransferHandler;
import sonar.fluxnetworks.api.tiles.IFluxConnector;
import sonar.fluxnetworks.api.utils.Coord4D;
import sonar.fluxnetworks.api.utils.NBTType;
import sonar.fluxnetworks.common.data.dto.FluxConnectorDTO;

import java.util.UUID;

import static sonar.fluxnetworks.common.data.TagConstants.BUFFER;
import static sonar.fluxnetworks.common.data.TagConstants.CHANGE;
import static sonar.fluxnetworks.common.data.TagConstants.D_LIMIT;
import static sonar.fluxnetworks.common.data.TagConstants.FOLDER_ID;
import static sonar.fluxnetworks.common.data.TagConstants.FORCED_CHUNK;
import static sonar.fluxnetworks.common.data.TagConstants.IS_CHUNK_LOADED;
import static sonar.fluxnetworks.common.data.TagConstants.LIMIT;
import static sonar.fluxnetworks.common.data.TagConstants.NAME;
import static sonar.fluxnetworks.common.data.TagConstants.N_ID;
import static sonar.fluxnetworks.common.data.TagConstants.PRIORITY;
import static sonar.fluxnetworks.common.data.TagConstants.SURGE;
import static sonar.fluxnetworks.common.data.TagConstants.TYPE;

public class FluxLiteConnector implements IFluxConnector {

    public long networkID;
    public int priority;
    public UUID playerUUID;
    public ConnectionType connectionType;
    public long limit;
    public Coord4D coord4D;
    public int folderID;
    public String customName;
    public boolean surgeMode;
    public boolean disableLimit;
    public boolean isChunkLoaded;
    public boolean chunkLoading;
    public long buffer;
    public long change;
    public ItemStack stack;

    public FluxLiteConnector(IFluxConnector tile) {
        this.networkID = tile.getNetworkID();
        this.priority = tile.getRawPriority();
        this.playerUUID = tile.getConnectionOwner();
        this.connectionType = tile.getConnectionType();
        this.limit = tile.getRawLimit();
        this.coord4D = tile.getCoords();
        this.folderID = tile.getFolderID();
        this.customName = tile.getCustomName();
        this.surgeMode = tile.getSurgeMode();
        this.disableLimit = tile.getDisableLimit();
        this.isChunkLoaded = tile.isChunkLoaded();
        this.buffer = tile.getTransferHandler().getBuffer();
        this.change = tile.getTransferHandler().getChange();
        this.chunkLoading = tile.isForcedLoading();
        this.stack = tile.getDisplayStack();
    }

    public FluxLiteConnector(FluxConnectorDTO dto) {
        this.networkID = dto.getN_id();
        this.priority = dto.getPriority();
        this.connectionType = ConnectionType.values()[dto.getType()];
        this.limit = dto.getLimit();
        this.coord4D = new Coord4D(dto.getX(), dto.getY(), dto.getZ(), dto.getDimension());
        this.folderID = dto.getFolder_id();
        this.customName = dto.getName();
        this.surgeMode = dto.getSurge();
        this.disableLimit = dto.getDisableLimit();
        this.isChunkLoaded = dto.getChunkLoaded();
        this.buffer = dto.getBuffer();
        this.change = dto.getChange();
        this.chunkLoading = dto.getForcedChunk();
        if (dto.getTag() != null &&  !dto.getTag().isEmpty()) {
            try {
                new ItemStack(JsonToNBT.getTagFromJson(dto.getTag()));
            } catch (NBTException e) {
                this.stack = ItemStack.EMPTY;
                FluxNetworks.logger.error(e.getMessage());
            }
        } else {
            this.stack = ItemStack.EMPTY;
        }
    }

    public FluxLiteConnector(NBTTagCompound tag) {
        readCustomNBT(tag, NBTType.ALL_SAVE);
    }

    public static NBTTagCompound writeCustomNBT(IFluxConnector tile, NBTTagCompound tag) {
        tile.getCoords().write(tag);
        tag.setInteger(TYPE, tile.getConnectionType().ordinal());
        tag.setLong(N_ID, tile.getNetworkID());
        tag.setInteger(PRIORITY, tile.getRawPriority());
        tag.setInteger(FOLDER_ID, tile.getFolderID());
        tag.setLong(LIMIT, tile.getRawLimit());
        tag.setString(NAME, tile.getCustomName());
        tag.setBoolean(D_LIMIT, tile.getDisableLimit());
        tag.setBoolean(SURGE, tile.getSurgeMode());
        tag.setBoolean(IS_CHUNK_LOADED, tile.isChunkLoaded());
        tag.setLong(BUFFER, tile.getTransferBuffer());
        tag.setLong(CHANGE, tile.getTransferChange());
        tag.setBoolean(FORCED_CHUNK, tile.isForcedLoading());
        tile.getDisplayStack().writeToNBT(tag);
        return tag;
    }

    @Override
    public NBTTagCompound writeCustomNBT(NBTTagCompound tag, NBTType type) {
        coord4D.write(tag);
        tag.setInteger(TYPE, connectionType.ordinal());
        tag.setLong(N_ID, networkID);
        tag.setInteger(PRIORITY, priority);
        tag.setInteger(FOLDER_ID, folderID);
        tag.setLong(LIMIT, limit);
        tag.setString(NAME, customName);
        tag.setBoolean(D_LIMIT, disableLimit);
        tag.setBoolean(SURGE, surgeMode);
        tag.setBoolean(IS_CHUNK_LOADED, isChunkLoaded);
        tag.setLong(BUFFER, buffer);
        tag.setLong(CHANGE, change);
        tag.setBoolean(FORCED_CHUNK, chunkLoading);
        stack.writeToNBT(tag);
        return tag;
    }

    @Override
    public void readCustomNBT(NBTTagCompound tag, NBTType type) {
        coord4D = new Coord4D(tag);
        connectionType = ConnectionType.values()[tag.getInteger(TYPE)];
        networkID = tag.getLong(N_ID);
        priority = tag.getInteger(PRIORITY);
        folderID = tag.getInteger(FOLDER_ID);
        limit = tag.getLong(LIMIT);
        customName = tag.getString(NAME);
        disableLimit = tag.getBoolean(D_LIMIT);
        surgeMode = tag.getBoolean(SURGE);
        isChunkLoaded = tag.getBoolean(IS_CHUNK_LOADED);
        buffer = tag.getLong(BUFFER);
        change = tag.getLong(CHANGE);
        chunkLoading = tag.getBoolean(FORCED_CHUNK);
        stack = new ItemStack(tag);
    }

    @Override
    public long getNetworkID() {
        return networkID;
    }

    @Override
    public int getLogicPriority() {
        return priority;
    }

    @Override
    public int getRawPriority() {
        return priority;
    }

    @Override
    public IFluxNetwork getNetwork() {
        return FluxNetworkInvalid.instance;
    }

    @Override
    public void open(EntityPlayer player) {
    }

    @Override
    public void close(EntityPlayer player) {
    }

    @Override
    public UUID getConnectionOwner() {
        return playerUUID;
    }

    @Override
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @Override
    public boolean canAccess(EntityPlayer player) {
        return false;
    }

    @Override
    public boolean isChunkLoaded() {
        return isChunkLoaded;
    }

    @Override
    public boolean isForcedLoading() {
        return chunkLoading;
    }

    @Override
    public void connect(IFluxNetwork network) {
    }

    @Override
    public void disconnect(IFluxNetwork network) {
    }

    @Override
    public ITransferHandler getTransferHandler() {
        return null;
    }

    @Override
    public World getFluxWorld() {
        return null;
    }

    @Override
    public long getLogicLimit() {
        return limit;
    }

    @Override
    public long getRawLimit() {
        return limit;
    }

    @Override
    public long getMaxTransferLimit() {
        return 0;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public Coord4D getCoords() {
        return coord4D;
    }

    @Override
    public int getFolderID() {
        return folderID;
    }

    @Override
    public String getCustomName() {
        return customName;
    }

    @Override
    public boolean getDisableLimit() {
        return disableLimit;
    }

    @Override
    public boolean getSurgeMode() {
        return surgeMode;
    }

    @Override
    public ItemStack getDisplayStack() {
        return stack;
    }

    @Override
    public long getTransferBuffer() {
        return buffer;
    }

    @Override
    public long getTransferChange() {
        return change;
    }

    @Override
    public void setChunkLoaded(boolean chunkLoaded) {
        isChunkLoaded = chunkLoaded;
    }
}
