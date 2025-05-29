package sonar.fluxnetworks.common.data.codecs;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import sonar.fluxnetworks.common.data.dto.ChunkPosDTO;
import sonar.fluxnetworks.common.data.dto.FluxNetworkDTO;
import sonar.fluxnetworks.common.data.dto.FluxNetworkDataDTO;

import java.util.ArrayList;
import java.util.UUID;

import static sonar.fluxnetworks.common.data.TagConstants.INDEX_FIELD;
import static sonar.fluxnetworks.common.data.TagConstants.LOADED_CHUNKS;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORKS;
import static sonar.fluxnetworks.common.data.TagConstants.UNIQUE_ID;

public class FluxNetworkDataCodec implements Codec<FluxNetworkDataDTO>{

    Codec<FluxNetworkDTO> fluxNetworkCodec;
    Codec<ChunkPosDTO> chunkPosCodec;
    Codec<UUID> uuidCodec;

    public FluxNetworkDataCodec(CodecRegistry registry) {
        this.fluxNetworkCodec = registry.get(FluxNetworkDTO.class);
        this.chunkPosCodec = registry.get(ChunkPosDTO.class);
        this.uuidCodec = registry.get(UUID.class);
    }

    @Override
    public FluxNetworkDataDTO decode(BsonReader reader, DecoderContext decoderContext) {
        FluxNetworkDataDTO fluxNetworkDataDTO = new FluxNetworkDataDTO();
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (name.equals(UNIQUE_ID)) {
                fluxNetworkDataDTO.setUniqueId(reader.readInt64());
            } else if (name.equals(NETWORKS)) {
                reader.readStartArray();
                fluxNetworkDataDTO.setNetworks(new ArrayList<>());
                while (reader.readBsonType() == BsonType.DOCUMENT) {
                    fluxNetworkDataDTO.getNetworks()
                            .add(decoderContext.decodeWithChildContext(fluxNetworkCodec, reader));
                }
                reader.readEndArray();
            } else if (name.equals(LOADED_CHUNKS)) {
                reader.readStartArray();
                fluxNetworkDataDTO.setLoadedChunks(new ArrayList<>());
                while (reader.readBsonType() == BsonType.DOCUMENT) {
                    fluxNetworkDataDTO.getLoadedChunks()
                            .add(decoderContext.decodeWithChildContext(chunkPosCodec, reader));
                }
                reader.readEndArray();
            } else if (name.equals(INDEX_FIELD)){
                uuidCodec.decode(reader, decoderContext);
            }
        }
        reader.readEndDocument();
        return fluxNetworkDataDTO;
    }

    @Override
    public void encode(BsonWriter writer, FluxNetworkDataDTO value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeName(INDEX_FIELD);
        uuidCodec.encode(writer, value.getServerID(), encoderContext);
        if (value.getUniqueId() != null) {
            writer.writeInt64(UNIQUE_ID, value.getUniqueId());
        }
        if (value.getNetworks() != null && !value.getNetworks().isEmpty()) {
            writer.writeName(NETWORKS);
            writer.writeStartArray();
            for (FluxNetworkDTO network : value.getNetworks()) {
                fluxNetworkCodec.encode(writer, network, encoderContext);
            }
            writer.writeEndArray();
        }
        if (value.getLoadedChunks() != null && !value.getLoadedChunks().isEmpty()) {
            writer.writeName(LOADED_CHUNKS);
            writer.writeStartArray();
            for (ChunkPosDTO chunkPos : value.getLoadedChunks()) {
                chunkPosCodec.encode(writer, chunkPos, encoderContext);
            }
            writer.writeEndArray();
        }
        writer.writeEndDocument();
    }

    @Override
    public Class<FluxNetworkDataDTO> getEncoderClass() {
        return FluxNetworkDataDTO.class;
    }
}
