package sonar.fluxnetworks.common.data.dto;

import net.minecraft.nbt.NBTTagCompound;
import sonar.fluxnetworks.common.data.TagUtils;

import java.util.Objects;

import static sonar.fluxnetworks.common.data.TagConstants.X;
import static sonar.fluxnetworks.common.data.TagConstants.Z;

public class ChunkPosDTO extends NBTCompatibleDTO<ChunkPosDTO> {
    private static final ChunkPosDTO EMPTY = new ChunkPosDTO();

    Integer x;
    Integer z;
    Integer dimension;

    public static ChunkPosDTO fromNBT(NBTTagCompound tag, int dimension) {
        ChunkPosDTO dto = new ChunkPosDTO();
        dto.x = TagUtils.intOrNull(tag, X);
        dto.z = TagUtils.intOrNull(tag, Z);
        dto.dimension = dimension;
        return dto.isEmpty() ? null : dto;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        TagUtils.fillNBT(tag, X, this.x);
        TagUtils.fillNBT(tag, Z, this.z);
        return tag;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }

    public Integer getDimension() {
        return dimension;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChunkPosDTO)) {
            return false;
        }
        ChunkPosDTO that = (ChunkPosDTO) o;
        return Objects.equals(x, that.x) && Objects.equals(z, that.z) && Objects.equals(dimension, that.dimension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z, dimension);
    }
}
