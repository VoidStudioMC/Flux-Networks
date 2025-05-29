package sonar.fluxnetworks.common.data.dto;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import sonar.fluxnetworks.common.data.TagUtils;

import java.util.Objects;

import static sonar.fluxnetworks.common.data.TagConstants.BUFFER;
import static sonar.fluxnetworks.common.data.TagConstants.CHANGE;
import static sonar.fluxnetworks.common.data.TagConstants.COUNT;
import static sonar.fluxnetworks.common.data.TagConstants.DAMAGE;
import static sonar.fluxnetworks.common.data.TagConstants.DIMENSION;
import static sonar.fluxnetworks.common.data.TagConstants.D_LIMIT;
import static sonar.fluxnetworks.common.data.TagConstants.FOLDER_ID;
import static sonar.fluxnetworks.common.data.TagConstants.FORCED_CHUNK;
import static sonar.fluxnetworks.common.data.TagConstants.ID;
import static sonar.fluxnetworks.common.data.TagConstants.IS_CHUNK_LOADED;
import static sonar.fluxnetworks.common.data.TagConstants.LIMIT;
import static sonar.fluxnetworks.common.data.TagConstants.NAME;
import static sonar.fluxnetworks.common.data.TagConstants.N_ID;
import static sonar.fluxnetworks.common.data.TagConstants.PRIORITY;
import static sonar.fluxnetworks.common.data.TagConstants.SURGE;
import static sonar.fluxnetworks.common.data.TagConstants.TAG;
import static sonar.fluxnetworks.common.data.TagConstants.TYPE;
import static sonar.fluxnetworks.common.data.TagConstants.X;
import static sonar.fluxnetworks.common.data.TagConstants.Y;
import static sonar.fluxnetworks.common.data.TagConstants.Z;

public class FluxConnectorDTO extends NBTCompatibleDTO<FluxConnectorDTO> {
    private static final FluxConnectorDTO EMPTY = new FluxConnectorDTO();

    Integer type;
    Long n_id;
    Integer priority;
    Integer folder_id;
    Long limit;
    String name;
    Boolean surge;
    Boolean disableLimit;
    Boolean isChunkLoaded;
    Long buffer;
    Long change;
    Boolean forcedChunk;

    Integer x;
    Integer y;
    Integer z;
    Integer dimension;

    String id;
    Byte Count;
    Short Damage;
    String tag;

    public void fillWithItemStackNBT(ItemStack stack) {
        NBTTagCompound tag = new NBTTagCompound();
        stack.writeToNBT(tag);
    }

    public static FluxConnectorDTO fromNBT(NBTTagCompound tag) {
        FluxConnectorDTO dto = new FluxConnectorDTO();
        dto.type = TagUtils.intOrNull(tag, TYPE);
        dto.n_id = TagUtils.longOrNull(tag, N_ID);
        dto.priority = TagUtils.intOrNull(tag, PRIORITY);
        dto.folder_id = TagUtils.intOrNull(tag, FOLDER_ID);
        dto.limit = TagUtils.longOrNull(tag, LIMIT);
        dto.name = TagUtils.stringOrNull(tag, NAME);
        dto.disableLimit = TagUtils.booleanOrNull(tag, D_LIMIT);
        dto.surge = TagUtils.booleanOrNull(tag, SURGE);
        dto.isChunkLoaded = TagUtils.booleanOrNull(tag, IS_CHUNK_LOADED);
        dto.buffer = TagUtils.longOrNull(tag, BUFFER);
        dto.change = TagUtils.longOrNull(tag, CHANGE);
        dto.forcedChunk = TagUtils.booleanOrNull(tag, FORCED_CHUNK);

        dto.x = TagUtils.intOrNull(tag, X);
        dto.y = TagUtils.intOrNull(tag, Y);
        dto.z = TagUtils.intOrNull(tag, Z);
        dto.dimension = TagUtils.intOrNull(tag, DIMENSION);

        dto.id = TagUtils.stringOrNull(tag, ID);
        dto.Count = TagUtils.byteOrNull(tag, COUNT);
        dto.Damage = TagUtils.shortOrNull(tag, DAMAGE);
        dto.tag = tag.getCompoundTag(TAG).toString();
        return dto.isEmpty() ? null : dto;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        TagUtils.fillNBT(tag, TYPE, type);
        TagUtils.fillNBT(tag, N_ID, n_id);
        TagUtils.fillNBT(tag, PRIORITY, priority);
        TagUtils.fillNBT(tag, FOLDER_ID, folder_id);
        TagUtils.fillNBT(tag, LIMIT, limit);
        TagUtils.fillNBT(tag, NAME, name);
        TagUtils.fillNBT(tag, SURGE, surge);
        TagUtils.fillNBT(tag, D_LIMIT, disableLimit);
        TagUtils.fillNBT(tag, IS_CHUNK_LOADED, isChunkLoaded);
        TagUtils.fillNBT(tag, BUFFER, buffer);
        TagUtils.fillNBT(tag, CHANGE, change);
        TagUtils.fillNBT(tag, FORCED_CHUNK, forcedChunk);

        TagUtils.fillNBT(tag, X, x);
        TagUtils.fillNBT(tag, Y, y);
        TagUtils.fillNBT(tag, Z, z);
        TagUtils.fillNBT(tag, DIMENSION, dimension);

        TagUtils.fillNBT(tag, ID, id);
        TagUtils.fillNBT(tag, COUNT, Count);
        TagUtils.fillNBT(tag, DAMAGE, Damage);
        TagUtils.parseNBTFromString(tag, TAG, this.tag);
        return tag;


    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getN_id() {
        return n_id;
    }

    public void setN_id(Long n_id) {
        this.n_id = n_id;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(Integer folder_id) {
        this.folder_id = folder_id;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSurge() {
        return surge;
    }

    public void setSurge(Boolean surge) {
        this.surge = surge;
    }

    public Boolean getChunkLoaded() {
        return isChunkLoaded;
    }

    public void setChunkLoaded(Boolean chunkLoaded) {
        isChunkLoaded = chunkLoaded;
    }

    public Long getBuffer() {
        return buffer;
    }

    public void setBuffer(Long buffer) {
        this.buffer = buffer;
    }

    public Long getChange() {
        return change;
    }

    public void setChange(Long change) {
        this.change = change;
    }

    public Boolean getForcedChunk() {
        return forcedChunk;
    }

    public void setForcedChunk(Boolean forcedChunk) {
        this.forcedChunk = forcedChunk;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Byte getCount() {
        return Count;
    }

    public void setCount(Byte count) {
        this.Count = count;
    }

    public Short getDamage() {
        return Damage;
    }

    public void setDamage(Short damage) {
        this.Damage = damage;
    }

    public Boolean getDisableLimit() {
        return disableLimit;
    }

    public void setDisableLimit(Boolean disableLimit) {
        this.disableLimit = disableLimit;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FluxConnectorDTO)) {
            return false;
        }
        FluxConnectorDTO that = (FluxConnectorDTO) o;
        return Objects.equals(type, that.type)
                       && Objects.equals(n_id, that.n_id)
                       && Objects.equals(priority, that.priority)
                       && Objects.equals(folder_id, that.folder_id)
                       && Objects.equals(limit, that.limit)
                       && Objects.equals(name, that.name)
                       && Objects.equals(surge, that.surge)
                       && Objects.equals(isChunkLoaded, that.isChunkLoaded)
                       && Objects.equals(buffer, that.buffer)
                       && Objects.equals(change, that.change)
                       && Objects.equals(forcedChunk, that.forcedChunk)
                       && Objects.equals(x, that.x)
                       && Objects.equals(y, that.y)
                       && Objects.equals(z, that.z)
                       && Objects.equals(dimension, that.dimension)
                       && Objects.equals(id, that.id)
                       && Objects.equals(Count, that.Count)
                       && Objects.equals(Damage, that.Damage)
                       && Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, n_id, priority, folder_id, limit, name, surge, isChunkLoaded, buffer, change,
                forcedChunk, x, y, z, dimension, id, Count, Damage, tag);
    }
}
