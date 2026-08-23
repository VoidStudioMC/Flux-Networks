package sonar.fluxnetworks.api.utils;

import net.minecraft.nbt.NBTTagCompound;
import sonar.fluxnetworks.api.network.IFluxNetwork;
import sonar.fluxnetworks.api.tiles.IFluxConnector;
import sonar.fluxnetworks.common.connection.FluxNetworkCache;

///TODO remove common references
public class FluxConfigurationType {
    public static FluxConfigurationType NETWORK = new FluxConfigurationType(0, "network", FluxConfigurationType::copyNetwork, FluxConfigurationType::pasteNetwork);
    public static FluxConfigurationType PRIORITY = new FluxConfigurationType(2, "priority", FluxConfigurationType::copyPriority, FluxConfigurationType::pastePriority);
    public static FluxConfigurationType PRIORITY_SETTING = new FluxConfigurationType(3, "p_setting", FluxConfigurationType::copyPrioritySetting, FluxConfigurationType::pastePrioritySetting);
    public static FluxConfigurationType TRANSFER = new FluxConfigurationType(4, "transfer", FluxConfigurationType::copyTransfer, FluxConfigurationType::pasteTransfer);
    public static FluxConfigurationType TRANSFER_SETTING = new FluxConfigurationType(5, "t_setting", FluxConfigurationType::copyTransferSetting, FluxConfigurationType::pasteTransferSetting);

    public static FluxConfigurationType[] VALUES = new FluxConfigurationType[]{NETWORK, PRIORITY, PRIORITY_SETTING, TRANSFER, TRANSFER_SETTING};

    public int ordinal;
    public String key;
    public ICopyMethod copy;
    public IPasteMethod paste;

    public FluxConfigurationType(int ordinal, String key, ICopyMethod copy, IPasteMethod paste) {
        this.ordinal = ordinal;
        this.key = key;
        this.copy = copy;
        this.paste = paste;
    }

    public String getNBTName() {
        return key;
    }

    //// NETWORK \\\\

    public static void copyNetwork(NBTTagCompound nbt, String key, IFluxConnector tile) {
        if (!tile.getNetwork().isInvalid() && tile.getNetworkID() != -1) {
            nbt.setInteger(key, tile.getNetworkID());
        }
    }

    public static void pasteNetwork(NBTTagCompound nbt, String key, IFluxConnector tile) {
        int storedID = nbt.getInteger(key);
        if (storedID != -1) {
            IFluxNetwork newNetwork = FluxNetworkCache.instance.getNetwork(storedID);
            tile.getNetwork().queueConnectionRemoval(tile, false);
            newNetwork.queueConnectionAddition(tile);
        }
    }

    //// PRIORITY \\\\

    public static void copyPriority(NBTTagCompound nbt, String key, IFluxConnector tile) {
        nbt.setInteger(key, tile.getRawPriority());
    }

    public static void pastePriority(NBTTagCompound nbt, String key, IFluxConnector tile) {
        tile.setRawPriority(nbt.getInteger(key));
    }

    //// PRIORITY SETTING \\\\

    public static void copyPrioritySetting(NBTTagCompound nbt, String key, IFluxConnector tile) {
        nbt.setBoolean(key, tile.getSurgeMode());
    }

    public static void pastePrioritySetting(NBTTagCompound nbt, String key, IFluxConnector tile) {
        tile.setSurgeMode(nbt.getBoolean(key));
    }

    //// TRANSFER LIMIT \\\\

    public static void copyTransfer(NBTTagCompound nbt, String key, IFluxConnector tile) {
        nbt.setLong(key, tile.getRawLimit());
    }

    public static void pasteTransfer(NBTTagCompound nbt, String key, IFluxConnector tile) {
        tile.setRawLimit(nbt.getLong(key));
    }

    //// TRANSFER SETTING \\\\

    public static void copyTransferSetting(NBTTagCompound nbt, String key, IFluxConnector tile) {
        nbt.setBoolean(key, tile.getDisableLimit());
    }

    public static void pasteTransferSetting(NBTTagCompound nbt, String key, IFluxConnector tile) {
        tile.setDisableLimit(nbt.getBoolean(key));
    }

    public interface ICopyMethod {
        void copyFromTile(NBTTagCompound tag, String key, IFluxConnector tile);
    }

    public interface IPasteMethod {
        void pasteToTile(NBTTagCompound tag, String key, IFluxConnector tile);
    }

}