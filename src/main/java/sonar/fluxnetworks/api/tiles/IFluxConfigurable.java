package sonar.fluxnetworks.api.tiles;

import net.minecraft.nbt.NBTTagCompound;
import sonar.fluxnetworks.FluxNetworks;

public interface IFluxConfigurable {

    NBTTagCompound copyConfiguration(NBTTagCompound config);

    void pasteConfiguration(NBTTagCompound config);

    default void setRawPriority(int priority) {
        //noinspection LoggingSimilarMessage
        FluxNetworks.logger.warn("Paste configuration not supported! Please check addons!");
    }

    default void setSurgeMode(boolean surgeMode) {
        //noinspection LoggingSimilarMessage
        FluxNetworks.logger.warn("Paste configuration not supported! Please check addons!");
    }

    default void setRawLimit(long limit) {
        //noinspection LoggingSimilarMessage
        FluxNetworks.logger.warn("Paste configuration not supported! Please check addons!");
    }

    default void setDisableLimit(boolean disableLimit) {
        //noinspection LoggingSimilarMessage
        FluxNetworks.logger.warn("Paste configuration not supported! Please check addons!");
    }
}
