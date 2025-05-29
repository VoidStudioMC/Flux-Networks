package sonar.fluxnetworks.api.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import sonar.fluxnetworks.common.data.dto.NetworkMemberDTO;

import java.util.UUID;

import static sonar.fluxnetworks.common.data.TagConstants.CACHED_NAME;
import static sonar.fluxnetworks.common.data.TagConstants.PLAYER_ACCESS;
import static sonar.fluxnetworks.common.data.TagConstants.PLAYER_UUID;

public class NetworkMember {

    private UUID playerUUID;
    private String cachedName;
    private AccessLevel accessPermission;

    NetworkMember() {
    }

    public NetworkMember(NBTTagCompound nbt) {
        readNetworkNBT(nbt);
    }

    public NetworkMember(NetworkMemberDTO networkMemberDTO) {
        this.playerUUID = networkMemberDTO.getPlayerUUID();
        this.cachedName = networkMemberDTO.getCachedName();
        this.accessPermission = AccessLevel.values()[networkMemberDTO.getPlayerAccess()];
    }

    public static NetworkMember createNetworkMember(EntityPlayer player, AccessLevel permissionLevel) {
        NetworkMember t = new NetworkMember();
        GameProfile profile = player.getGameProfile();

        t.playerUUID = EntityPlayer.getUUID(profile);
        t.cachedName = profile.getName();
        t.accessPermission = permissionLevel;

        return t;
    }

    public static NetworkMember createMemberByUsername(String username) {
        NetworkMember t = new NetworkMember();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        boolean isOffline = !server.isServerInOnlineMode();

        if (!isOffline) {
            PlayerProfileCache cache = server.getPlayerProfileCache();
            GameProfile profile = cache.getGameProfileForUsername(username);
            if (profile != null) {
                t.playerUUID = profile.getId();
            } else {
                isOffline = true;
            }
        }
        if (isOffline) {
            t.playerUUID = EntityPlayer.getOfflineUUID(username);
        }
        t.cachedName = username;
        t.accessPermission = AccessLevel.USER;
        return t;
    }

    public String getCachedName() {
        return cachedName;
    }

    public AccessLevel getAccessPermission() {
        return accessPermission;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setAccessPermission(AccessLevel accessPermission) {
        this.accessPermission = accessPermission;
    }

    public void readNetworkNBT(NBTTagCompound nbt) {
        playerUUID = nbt.getUniqueId(PLAYER_UUID);
        cachedName = nbt.getString(CACHED_NAME);
        accessPermission = AccessLevel.values()[nbt.getByte(PLAYER_ACCESS)];
    }

    public NBTTagCompound writeNetworkNBT(NBTTagCompound nbt) {

        nbt.setUniqueId(PLAYER_UUID, playerUUID);
        nbt.setString(CACHED_NAME, cachedName);
        nbt.setByte(PLAYER_ACCESS, (byte) accessPermission.ordinal());
        return nbt;
    }
}
