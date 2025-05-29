package sonar.fluxnetworks.common.data.dto;

import net.minecraft.nbt.NBTTagCompound;
import sonar.fluxnetworks.common.data.TagUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_COLOR;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_ENERGY;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_ID;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_NAME;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_PASSWORD;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_SECURITY;
import static sonar.fluxnetworks.common.data.TagConstants.OWNER_UUID;
import static sonar.fluxnetworks.common.data.TagConstants.PLAYER_LIST;
import static sonar.fluxnetworks.common.data.TagConstants.UNLOADED;
import static sonar.fluxnetworks.common.data.TagConstants.WIRELESS_MODE;

public class FluxNetworkDTO extends NBTCompatibleDTO<FluxNetworkDTO> {
    private static final FluxNetworkDTO EMPTY = new FluxNetworkDTO();

    Long networkID;
    String networkName;
    UUID ownerUUID;
    Integer networkSecurity;
    String networkPassword;
    Integer networkColor;
    Integer networkEnergy;
    Integer wirelessMode;
    List<NetworkMemberDTO> playerList;
    List<FluxConnectorDTO> unloaded;

    public static FluxNetworkDTO fromNBT(NBTTagCompound tag) {
        FluxNetworkDTO dto = new FluxNetworkDTO();
        dto.networkID = TagUtils.longOrNull(tag, NETWORK_ID);
        dto.networkName = TagUtils.stringOrNull(tag, NETWORK_NAME);
        dto.ownerUUID = TagUtils.uuidOrNull(tag, OWNER_UUID);
        dto.networkSecurity = TagUtils.intOrNull(tag, NETWORK_SECURITY);
        dto.networkPassword = TagUtils.stringOrNull(tag, NETWORK_PASSWORD);
        dto.networkColor = TagUtils.intOrNull(tag, NETWORK_COLOR);
        dto.networkEnergy =TagUtils.intOrNull(tag, NETWORK_ENERGY);
        dto.wirelessMode = TagUtils.intOrNull(tag, WIRELESS_MODE);
        dto.playerList = TagUtils.playerListOrNull(tag, PLAYER_LIST);
        dto.unloaded = TagUtils.fluxConnectorListOrNull(tag, UNLOADED);
        return dto.isEmpty() ? null : dto;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        TagUtils.fillNBT(tag, NETWORK_ID, this.networkID);
        TagUtils.fillNBT(tag, NETWORK_NAME, this.networkName);
        TagUtils.fillNBT(tag, OWNER_UUID, this.ownerUUID);
        TagUtils.fillNBT(tag, NETWORK_SECURITY, this.networkSecurity);
        TagUtils.fillNBT(tag, NETWORK_PASSWORD, this.networkPassword);
        TagUtils.fillNBT(tag, NETWORK_COLOR, this.networkColor);
        TagUtils.fillNBT(tag, NETWORK_ENERGY, this.networkEnergy);
        TagUtils.fillNBT(tag, WIRELESS_MODE, this.wirelessMode);
        TagUtils.fillNBT(tag, PLAYER_LIST, this.playerList);
        TagUtils.fillNBT(tag, UNLOADED, this.unloaded);
        return tag;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    public Long getNetworkID() {
        return networkID;
    }

    public void setNetworkID(Long networkID) {
        this.networkID = networkID;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public Integer getNetworkSecurity() {
        return networkSecurity;
    }

    public void setNetworkSecurity(Integer networkSecurity) {
        this.networkSecurity = networkSecurity;
    }

    public String getNetworkPassword() {
        return networkPassword;
    }

    public void setNetworkPassword(String networkPassword) {
        this.networkPassword = networkPassword;
    }

    public Integer getNetworkColor() {
        return networkColor;
    }

    public void setNetworkColor(Integer networkColor) {
        this.networkColor = networkColor;
    }

    public Integer getNetworkEnergy() {
        return networkEnergy;
    }

    public void setNetworkEnergy(Integer networkEnergy) {
        this.networkEnergy = networkEnergy;
    }

    public Integer getWirelessMode() {
        return wirelessMode;
    }

    public void setWirelessMode(Integer wirelessMode) {
        this.wirelessMode = wirelessMode;
    }

    public List<NetworkMemberDTO> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<NetworkMemberDTO> playerList) {
        this.playerList = playerList;
    }

    public List<FluxConnectorDTO> getUnloaded() {
        return unloaded;
    }

    public void setUnloaded(List<FluxConnectorDTO> unloaded) {
        this.unloaded = unloaded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FluxNetworkDTO)) {
            return false;
        }
        FluxNetworkDTO that = (FluxNetworkDTO) o;
        return Objects.equals(networkID, that.networkID)
                       && Objects.equals(networkName, that.networkName)
                       && Objects.equals(ownerUUID, that.ownerUUID)
                       && Objects.equals(networkSecurity, that.networkSecurity)
                       && Objects.equals(networkPassword, that.networkPassword)
                       && Objects.equals(networkColor, that.networkColor)
                       && Objects.equals(networkEnergy, that.networkEnergy)
                       && Objects.equals(wirelessMode, that.wirelessMode)
                       && Objects.equals(playerList, that.playerList)
                       && Objects.equals(unloaded, that.unloaded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkID, networkName, ownerUUID, networkSecurity, networkPassword, networkColor,
                networkEnergy, wirelessMode, playerList, unloaded);
    }
}
