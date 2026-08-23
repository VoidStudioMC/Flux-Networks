package sonar.fluxnetworks.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import sonar.fluxnetworks.FluxConfig;
import sonar.fluxnetworks.api.gui.EnumFeedbackInfo;
import sonar.fluxnetworks.api.network.FluxLogicType;
import sonar.fluxnetworks.api.network.IFluxNetwork;
import sonar.fluxnetworks.api.network.NetworkSettings;
import sonar.fluxnetworks.api.tiles.IFluxConnector;
import sonar.fluxnetworks.common.connection.FluxNetworkCache;
import sonar.fluxnetworks.common.data.FluxChunkManager;
import sonar.fluxnetworks.common.data.FluxNetworkData;

public class PacketTileHandler {

    public static NBTTagCompound getSetNetworkPacket(int id, String password) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(FluxNetworkData.NETWORK_ID, id);
        tag.setString(FluxNetworkData.NETWORK_PASSWORD, password);
        return tag;
    }

    public static IMessage handleSetNetworkPacket(IFluxConnector flux, EntityPlayer player, NBTTagCompound tag) {
        int id = tag.getInteger(FluxNetworkData.NETWORK_ID);
        String pass = tag.getString(FluxNetworkData.NETWORK_PASSWORD);
        if (flux.getNetworkID() == id) {
            return null;
        }
        IFluxNetwork network = FluxNetworkCache.instance.getNetwork(id);
        if (!network.isInvalid()) {
            if (flux.getConnectionType().isController() && network.getConnections(FluxLogicType.CONTROLLER).size() > 0) {
                return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.HAS_CONTROLLER);
            }
            //Sanctum old applyFN05Patch
            if (!network.getMemberPermission(player).canEdit()) {
                return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.REJECT);
            }
            //end
            if (!network.getMemberPermission(player).canAccess()) {
                if (pass.isEmpty()) {
                    return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.PASSWORD_REQUIRE);
                }
                if (!pass.equals(network.getSetting(NetworkSettings.NETWORK_PASSWORD))) {
                    return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.REJECT);
                }
            }
            if (flux.getNetwork() != null && !flux.getNetwork().isInvalid()) {
                //Sanctum old applyFN01Patch
                if (!network.getMemberPermission(player).canAccess()) {
                    return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.REJECT);
                }
                //end
                flux.getNetwork().queueConnectionRemoval(flux, false);
            }
            flux.setConnectionOwner(EntityPlayer.getUUID(player.getGameProfile()));
            network.queueConnectionAddition(flux);
            return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.SUCCESS);
        }
        return null;
    }

    public static NBTTagCompound getChunkLoadPacket(boolean chunkLoading) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("c", chunkLoading);
        return tag;
    }

    public static IMessage handleChunkLoadPacket(IFluxConnector flux, EntityPlayer player, NBTTagCompound tag) {
        boolean load = tag.getBoolean("c");
        if (FluxConfig.enableChunkLoading) {
            if (load) {
                boolean p = FluxChunkManager.forceChunk(flux.getFluxWorld(), new ChunkPos(flux.getFluxPos()));
                flux.setForcedLoading(p);
                if (!p) {
                    return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.HAS_LOADER);
                }
                return null;
            } else {
                FluxChunkManager.releaseChunk(flux.getFluxWorld(), new ChunkPos(flux.getFluxPos()));
                flux.setForcedLoading(false);
                return null;
            }
        } else {
            flux.setForcedLoading(false);
        }
        return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.BANNED_LOADING);
    }
}
