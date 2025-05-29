package sonar.fluxnetworks.common.data.codecs;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import sonar.fluxnetworks.common.data.dto.ChunkPosDTO;

import static sonar.fluxnetworks.common.data.TagConstants.DIMENSION;
import static sonar.fluxnetworks.common.data.TagConstants.X;
import static sonar.fluxnetworks.common.data.TagConstants.Z;

public class ChunkPosCodec implements Codec<ChunkPosDTO> {

    @Override
    public ChunkPosDTO decode(BsonReader reader, DecoderContext decoderContext) {
        ChunkPosDTO chunkPosDTO = new ChunkPosDTO();
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String s = reader.readName();
            if (s.equals(X)) {
                chunkPosDTO.setX(reader.readInt32());
            } else if (s.equals(Z)) {
                chunkPosDTO.setZ(reader.readInt32());
            } else if (s.equals(DIMENSION)) {
                chunkPosDTO.setDimension(reader.readInt32());
            }
        }
        reader.readEndDocument();
        return chunkPosDTO;
    }

    @Override
    public void encode(BsonWriter writer, ChunkPosDTO value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeInt32(X, value.getX());
        writer.writeInt32(Z, value.getZ());
        writer.writeInt32(DIMENSION, value.getDimension());
        writer.writeEndDocument();
    }

    @Override
    public Class<ChunkPosDTO> getEncoderClass() {
        return ChunkPosDTO.class;
    }
}
