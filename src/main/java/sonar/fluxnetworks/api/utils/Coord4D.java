package sonar.fluxnetworks.api.utils;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import sonar.fluxnetworks.common.data.dto.FluxConnectorDTO;

import static sonar.fluxnetworks.common.data.TagConstants.DIMENSION;
import static sonar.fluxnetworks.common.data.TagConstants.X;
import static sonar.fluxnetworks.common.data.TagConstants.Y;
import static sonar.fluxnetworks.common.data.TagConstants.Z;

public class Coord4D {

    private int x, y, z, dimension;

    public Coord4D(int x, int y, int z, int dimension) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    public Coord4D(NBTTagCompound tag) {
        read(tag);
    }

    public Coord4D(TileEntity tile) {
        x = tile.getPos().getX();
        y = tile.getPos().getY();
        z = tile.getPos().getZ();
        dimension = tile.getWorld().provider.getDimension();
    }

    public Coord4D(ByteBuf buf) {
        read(buf);
    }

    public NBTTagCompound write(NBTTagCompound tag) {
        tag.setInteger(X, x);
        tag.setInteger(Y, y);
        tag.setInteger(Z, z);
        tag.setInteger(DIMENSION, dimension);
        return tag;
    }

    public void write(FluxConnectorDTO dto) {
        dto.setX(x);
        dto.setY(y);
        dto.setZ(z);
        dto.setDimension(dimension);
    }

    public void read(NBTTagCompound tag) {
        x = tag.getInteger(X);
        y = tag.getInteger(Y);
        z = tag.getInteger(Z);
        dimension = tag.getInteger(DIMENSION);
    }

    public void read(FluxConnectorDTO dto) {
        x = dto.getX();
        y = dto.getY();
        z = dto.getZ();
        dimension = dto.getDimension();
    }

    public void write(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(dimension);
    }

    public void read(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        dimension = buf.readInt();
    }

    public String getStringInfo() {
        return "X: " + x + " Y: " + y + " Z: " + z + " Dim: " + dimension;
    }

    public BlockPos getPos() {
        return new BlockPos(x, y, z);
    }

    public int getDimension() {
        return dimension;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coord4D)) {
            return false;
        }
        Coord4D c = (Coord4D) obj;
        return x == c.x && y == c.y && z == c.z && dimension == c.dimension;
    }
}
