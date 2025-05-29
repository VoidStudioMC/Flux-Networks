package sonar.fluxnetworks.common.data.codecs;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import sonar.fluxnetworks.common.data.dto.FluxConnectorDTO;

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

public class FluxConnectorCodec implements Codec <FluxConnectorDTO>{
    @Override
    public FluxConnectorDTO decode(BsonReader reader, DecoderContext decoderContext) {
        FluxConnectorDTO connector = new FluxConnectorDTO();
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (name.equals(TYPE)) {
                connector.setType(reader.readInt32());
            } else if (name.equals(N_ID)) {
                connector.setN_id(reader.readInt64());
            } else if (name.equals(PRIORITY)) {
                connector.setPriority(reader.readInt32());
            } else if (name.equals(FOLDER_ID)) {
                connector.setFolder_id(reader.readInt32());
            } else if (name.equals(LIMIT)) {
                connector.setLimit(reader.readInt64());
            } else if (name.equals(NAME)) {
                connector.setName(reader.readString());
            } else if (name.equals(SURGE)) {
                connector.setSurge(reader.readBoolean());
            } else if (name.equals(D_LIMIT)) {
                connector.setDisableLimit(reader.readBoolean());
            } else if (name.equals(IS_CHUNK_LOADED)) {
                connector.setChunkLoaded(reader.readBoolean());
            } else if (name.equals(BUFFER)) {
                connector.setBuffer(reader.readInt64());
            } else if (name.equals(CHANGE)) {
                connector.setChange(reader.readInt64());
            } else if (name.equals(FORCED_CHUNK)) {
                connector.setForcedChunk(reader.readBoolean());
            }
            else if (name.equals(X)) {
                connector.setX(reader.readInt32());
            } else if (name.equals(Y)) {
                connector.setY(reader.readInt32());
            } else if (name.equals(Z)) {
                connector.setZ(reader.readInt32());
            } else if (name.equals(DIMENSION)) {
                connector.setDimension(reader.readInt32());
            }
            else if (name.equals(ID)) {
                connector.setId(reader.readString());
            } else if (name.equals(COUNT)) {
                connector.setCount((byte) reader.readInt32());
            } else if (name.equals(DAMAGE)) {
                connector.setDamage((short) reader.readInt32());
            } else if (name.equals(TAG)) {
                connector.setTag(reader.readString());
            }
        }
        reader.readEndDocument();
        return connector;
    }

    @Override
    public void encode(BsonWriter writer, FluxConnectorDTO value, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeInt32(TYPE, value.getType());
        writer.writeInt64(N_ID, value.getN_id());
        writer.writeInt32(PRIORITY, value.getPriority());
        writer.writeInt32(FOLDER_ID, value.getFolder_id());
        writer.writeInt64(LIMIT, value.getLimit());
        writer.writeString(NAME, value.getName());
        writer.writeBoolean(SURGE, value.getSurge());
        writer.writeBoolean(D_LIMIT, value.getDisableLimit());
        writer.writeBoolean(IS_CHUNK_LOADED, value.getChunkLoaded());
        writer.writeInt64(BUFFER, value.getBuffer());
        writer.writeInt64(CHANGE, value.getChange());
        writer.writeBoolean(FORCED_CHUNK, value.getForcedChunk());

        writer.writeInt32(X, value.getX());
        writer.writeInt32(Y, value.getY());
        writer.writeInt32(Z, value.getZ());
        writer.writeInt32(DIMENSION, value.getDimension());

        writer.writeString(ID, value.getId());
        writer.writeInt32(COUNT, value.getCount());
        writer.writeInt32(DAMAGE, value.getDamage());
        writer.writeString(TAG, value.getTag());

        writer.writeEndDocument();
    }

    @Override
    public Class<FluxConnectorDTO> getEncoderClass() {
        return FluxConnectorDTO.class;
    }
}
