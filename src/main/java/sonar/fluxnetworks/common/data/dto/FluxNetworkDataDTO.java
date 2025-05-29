package sonar.fluxnetworks.common.data.dto;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.api.network.IFluxNetwork;
import sonar.fluxnetworks.api.network.NetworkMember;
import sonar.fluxnetworks.api.network.NetworkSettings;
import sonar.fluxnetworks.api.network.SecurityType;
import sonar.fluxnetworks.api.tiles.IFluxConnector;
import sonar.fluxnetworks.api.utils.EnergyType;
import sonar.fluxnetworks.common.connection.FluxLiteConnector;
import sonar.fluxnetworks.common.connection.FluxNetworkServer;
import sonar.fluxnetworks.common.data.FluxNetworkData;
import sonar.fluxnetworks.common.data.TagUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static sonar.fluxnetworks.FluxConfig.MONGO_SERVER_ID;
import static sonar.fluxnetworks.common.data.TagConstants.LOADED_CHUNKS;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORKS;
import static sonar.fluxnetworks.common.data.TagConstants.UNIQUE_ID;

public class FluxNetworkDataDTO {
    private static final FluxNetworkDataDTO EMPTY = new FluxNetworkDataDTO();

    List<FluxNetworkDTO> networks;
    List<ChunkPosDTO> loadedChunks;
    UUID serverID;
    Long uniqueId;

    public FluxNetworkDataDTO() {}

    public FluxNetworkDataDTO(UUID serverID) {
        this.serverID = serverID;
    }

    public static FluxNetworkDataDTO fromNBT(NBTTagCompound tag) {
        FluxNetworkDataDTO dto = new FluxNetworkDataDTO();
        dto.uniqueId = TagUtils.longOrNull(tag, UNIQUE_ID);
        dto.networks = TagUtils.fluxNetworkListOrNull(tag, NETWORKS);
        dto.loadedChunks = TagUtils.chunkPosListOrNull(tag, LOADED_CHUNKS);
        dto.serverID = MONGO_SERVER_ID;
        return dto.isEmpty() ? null : dto;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong(UNIQUE_ID, uniqueId);
        TagUtils.fillNBT(tag, NETWORKS, networks);
        TagUtils.fillNBT(tag, LOADED_CHUNKS, loadedChunks);
        return tag;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    public FluxNetworkData getFluxNetworkData() {
        FluxNetworkData data = new FluxNetworkData();
        data.networks = getNetworkMap();
        data.loadedChunks = getLoadedChunksMap();
        data.uniqueID = getUniqueId();
        return data;
    }

    public Map<Long, IFluxNetwork> getNetworkMap() {
        Map<Long, IFluxNetwork> map = new HashMap<>();
        if (networks != null) {
            for (FluxNetworkDTO dto : networks) {
                FluxNetworkServer fluxNetwork = new FluxNetworkServer(
                        dto.networkID,
                        dto.networkName,
                        SecurityType.values()[dto.networkSecurity],
                        dto.networkColor,
                        dto.ownerUUID,
                        EnergyType.values()[dto.networkEnergy],
                        dto.networkPassword
                );
                fluxNetwork.network_wireless.setValue(dto.wirelessMode);
                if (dto.playerList != null && !dto.playerList.isEmpty()) {
                    List<NetworkMember> playerList = new ArrayList<>();
                    for (NetworkMemberDTO networkMemberDTO : dto.playerList) {
                        playerList.add(new NetworkMember(networkMemberDTO));
                    }
                    fluxNetwork.setSetting(NetworkSettings.NETWORK_PLAYERS, playerList);
                }

                if (dto.unloaded != null && !dto.unloaded.isEmpty()) {
                    List<IFluxConnector> unloaded = new ArrayList<>();
                    for (FluxConnectorDTO fluxConnectorDTO : dto.unloaded) {
                        unloaded.add(new FluxLiteConnector(fluxConnectorDTO));
                    }
                    fluxNetwork.setSetting(NetworkSettings.ALL_CONNECTORS, unloaded);
                }

                map.put(dto.networkID, fluxNetwork);
            }
        }
        return map;
    }

    public Map<Integer, List<ChunkPos>> getLoadedChunksMap() {
        Map<Integer, List<ChunkPos>> map = new HashMap<>();
        if (loadedChunks != null) {
            for (ChunkPosDTO chunkPosDTO : loadedChunks) {
                if (!map.containsKey(chunkPosDTO.dimension)) {
                    map.put(chunkPosDTO.dimension, new ArrayList<>());
                }
                map.get(chunkPosDTO.dimension).add(new ChunkPos(chunkPosDTO.x, chunkPosDTO.z));
            }
        }
        return map;
    }

    public List<FluxNetworkDTO> getNetworks() {
        return networks;
    }

    public void setNetworks(List<FluxNetworkDTO> networks) {
        this.networks = networks;
    }

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public List<ChunkPosDTO> getLoadedChunks() {
        return loadedChunks;
    }

    public void setLoadedChunks(List<ChunkPosDTO> loadedChunks) {
        this.loadedChunks = loadedChunks;
    }

    public UUID getServerID() {
        return serverID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FluxNetworkDataDTO)) {
            return false;
        }
        FluxNetworkDataDTO that = (FluxNetworkDataDTO) o;
        return Objects.equals(networks, that.networks)
                       && Objects.equals(loadedChunks, that.loadedChunks)
                       && Objects.equals(uniqueId, that.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(networks, loadedChunks, uniqueId);
    }
}
