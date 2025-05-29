package sonar.fluxnetworks.common.data.dto;

import net.minecraft.nbt.NBTTagCompound;
import sonar.fluxnetworks.common.data.TagUtils;

import java.util.Objects;
import java.util.UUID;

import static sonar.fluxnetworks.common.data.TagConstants.CACHED_NAME;
import static sonar.fluxnetworks.common.data.TagConstants.PLAYER_ACCESS;
import static sonar.fluxnetworks.common.data.TagConstants.PLAYER_UUID;

public class NetworkMemberDTO extends NBTCompatibleDTO<NetworkMemberDTO> {
    private static final NetworkMemberDTO EMPTY = new NetworkMemberDTO();

    UUID playerUUID;
    String cachedName;
    Byte playerAccess;

    public static NetworkMemberDTO fromNBT(NBTTagCompound tag) {
        NetworkMemberDTO dto = new NetworkMemberDTO();
        dto.playerUUID = tag.getUniqueId(PLAYER_UUID);
        dto.cachedName = tag.getString(CACHED_NAME);
        dto.playerAccess = tag.getByte(PLAYER_ACCESS);
        return dto.isEmpty() ? null : dto;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        TagUtils.fillNBT(tag, PLAYER_UUID, playerUUID);
        TagUtils.fillNBT(tag, CACHED_NAME, cachedName);
        TagUtils.fillNBT(tag, PLAYER_ACCESS, playerAccess);
        return tag;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getCachedName() {
        return cachedName;
    }

    public void setCachedName(String cachedName) {
        this.cachedName = cachedName;
    }

    public Byte getPlayerAccess() {
        return playerAccess;
    }

    public void setPlayerAccess(Byte playerAccess) {
        this.playerAccess = playerAccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NetworkMemberDTO)) {
            return false;
        }
        NetworkMemberDTO that = (NetworkMemberDTO) o;
        return Objects.equals(playerUUID, that.playerUUID)
                       && Objects.equals(cachedName, that.cachedName)
                       && Objects.equals(playerAccess, that.playerAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerUUID, cachedName, playerAccess);
    }
}
