package sonar.fluxnetworks.common.data;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.common.data.dto.ChunkPosDTO;
import sonar.fluxnetworks.common.data.dto.FluxConnectorDTO;
import sonar.fluxnetworks.common.data.dto.FluxNetworkDTO;
import sonar.fluxnetworks.common.data.dto.NBTCompatibleDTO;
import sonar.fluxnetworks.common.data.dto.NetworkMemberDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TagUtils {
    public static Long longOrNull(NBTTagCompound tag, String key) {
        return tag.hasKey(key) ? tag.getLong(key) : null;
    }

    public static Integer intOrNull(NBTTagCompound tag, String key) {
        return tag.hasKey(key) ? tag.getInteger(key) : null;
    }

    public static Boolean booleanOrNull(NBTTagCompound tag, String key) {
        return tag.hasKey(key) ? tag.getBoolean(key) : null;
    }

    public static String stringOrNull(NBTTagCompound tag, String key) {
        return tag.hasKey(key) ? tag.getString(key) : null;
    }

    public static Byte byteOrNull(NBTTagCompound tag, String key) {
        return tag.hasKey(key) ? tag.getByte(key) : null;
    }

    public static Short shortOrNull(NBTTagCompound tag, String key) {
        return tag.hasKey(key) ? tag.getShort(key) : null;
    }

    public static List<NetworkMemberDTO> playerListOrNull(NBTTagCompound tag, String key) {
        List<NetworkMemberDTO> dtoList = new ArrayList<>();
        if (tag.hasKey(key)) {
            NBTTagList nbtTagList = tag.getTagList(key, 10);
            for (int i = 0; i < nbtTagList.tagCount(); i++) {
                dtoList.add(NetworkMemberDTO.fromNBT(nbtTagList.getCompoundTagAt(i)));
            }
        }
        return dtoList.isEmpty() ? null : dtoList;
    }

    public static List<FluxConnectorDTO> fluxConnectorListOrNull(NBTTagCompound tag, String key) {
        List<FluxConnectorDTO> dtoList = new ArrayList<>();
        if (tag.hasKey(key)) {
            NBTTagList nbtTagList = tag.getTagList(key, 10);
            for (int i = 0; i < nbtTagList.tagCount(); i++) {
                dtoList.add(FluxConnectorDTO.fromNBT(nbtTagList.getCompoundTagAt(i)));
            }
        }
        return dtoList.isEmpty() ? null : dtoList;
    }

    public static List<FluxNetworkDTO> fluxNetworkListOrNull(NBTTagCompound tag, String key) {
        List<FluxNetworkDTO> dtoList = new ArrayList<>();
        if (tag.hasKey(key)) {
            NBTTagList nbtTagList = tag.getTagList(key, 10);
            for (int i = 0; i < nbtTagList.tagCount(); i++) {
                dtoList.add(FluxNetworkDTO.fromNBT(nbtTagList.getCompoundTagAt(i)));
            }
        }
        return dtoList.isEmpty() ? null : dtoList;
    }

    public static List<ChunkPosDTO> chunkPosListOrNull(NBTTagCompound tag, String key) {
        List<ChunkPosDTO> dtoList = new ArrayList<>();
        if (tag.hasKey(key)) {
            NBTTagCompound chunksTag = tag.getCompoundTag(key);
            chunksTag.getKeySet().forEach(dim -> {
                NBTTagList nbtTagList = chunksTag.getTagList(dim, 10);
                for (int i = 0; i < nbtTagList.tagCount(); i++) {
                    dtoList.add(ChunkPosDTO.fromNBT(nbtTagList.getCompoundTagAt(i), Integer.parseInt(dim)));
                }
            });
        }
        return dtoList.isEmpty() ? null : dtoList;
    }


    public static UUID uuidOrNull(NBTTagCompound tag, String key) {
        return tag.hasKey(key + "Least") && tag.hasKey(key + "Most")? tag.getUniqueId(key) : null;
    }

    public static <T> void fillNBT(NBTTagCompound tag, String key, T value) {
        if (value != null) {
            if (value instanceof UUID) {
                tag.setUniqueId(key, (UUID) value);
            } else if (value instanceof Integer) {
                tag.setInteger(key, (Integer) value);
            } else if (value instanceof Long) {
                tag.setLong(key, (Long) value);
            } else if (value instanceof String) {
                tag.setString(key, (String) value);
            } else if (value instanceof Byte) {
                tag.setByte(key, (Byte) value);
            } else if (value instanceof Short) {
                tag.setShort(key, (Short) value);
            } else if (value instanceof Boolean) {
                tag.setBoolean(key, (Boolean) value);
            }
        }
    }

    public static void parseNBTFromString(NBTTagCompound tag, String key, String value) {
        if (value != null && !value.isEmpty()) {
            try {
                tag.setTag(key, JsonToNBT.getTagFromJson(value));
            } catch (NBTException e) {
                FluxNetworks.logger.error(e.getMessage());
            }
        }
    }

    static <T extends NBTCompatibleDTO<T>> void fillNBT(NBTTagCompound tag, String key, List<T> value) {
        if (value != null) {
            NBTTagList nbtTagList = new NBTTagList();
            for (T dto : value) {
                nbtTagList.appendTag(dto.toNBT());
            }
            tag.setTag(key, nbtTagList);
        }
    }

}
