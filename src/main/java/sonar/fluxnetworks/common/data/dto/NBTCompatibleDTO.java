package sonar.fluxnetworks.common.data.dto;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

public abstract class NBTCompatibleDTO<T> {
    public static <T> T fromNBT(NBTTagCompound tag) {
        return null;
    }

    public abstract NBTTagCompound toNBT();

    static <T> List<T> fromNBTList(NBTTagCompound tag, String key) {
        List<T> dtoList = new ArrayList<>();
        if (tag.hasKey(key)) {
            NBTTagList nbtTagList = tag.getTagList(key, 10);
            for (int i = 0; i < nbtTagList.tagCount(); i++) {
                dtoList.add(fromNBT(nbtTagList.getCompoundTagAt(i)));
            }
        }
        return dtoList.isEmpty() ? null : dtoList;
    }

    static void fillNBT(NBTTagCompound tag, String key, List<NBTCompatibleDTO<?>> value) {
        if (value != null) {
            NBTTagList nbtTagList = new NBTTagList();
            for (NBTCompatibleDTO<?> dto : value) {
                nbtTagList.appendTag(dto.toNBT());
            }
        }
    }
}
