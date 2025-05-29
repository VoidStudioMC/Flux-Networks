package sonar.fluxnetworks.common.data.codecs;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import sonar.fluxnetworks.common.data.dto.NetworkMemberDTO;

import java.util.UUID;

import static sonar.fluxnetworks.common.data.TagConstants.CACHED_NAME;
import static sonar.fluxnetworks.common.data.TagConstants.PLAYER_ACCESS;
import static sonar.fluxnetworks.common.data.TagConstants.PLAYER_UUID;

public class NetworkMemberCodec implements Codec<NetworkMemberDTO> {

    Codec<UUID> uuidCodec;

    public NetworkMemberCodec(CodecRegistry registry) {
        this.uuidCodec = registry.get(UUID.class);
    }

    @Override
    public NetworkMemberDTO decode(BsonReader reader, DecoderContext decoderContext) {
        NetworkMemberDTO networkMember = new NetworkMemberDTO();
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (name.equals(PLAYER_UUID)) {
                networkMember.setPlayerUUID(uuidCodec.decode(reader, decoderContext));
            } else if (name.equals(CACHED_NAME)) {
                networkMember.setCachedName(reader.readString());
            } else if (name.equals(PLAYER_ACCESS)) {
                networkMember.setPlayerAccess((byte) reader.readInt32());
            }
        }
        reader.readEndDocument();
        return networkMember;
    }

    @Override
    public void encode(BsonWriter writer, NetworkMemberDTO value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeName(PLAYER_UUID);
        uuidCodec.encode(writer, value.getPlayerUUID(), encoderContext);
        writer.writeString(CACHED_NAME, value.getCachedName());
        writer.writeInt32(PLAYER_ACCESS, value.getPlayerAccess());
        writer.writeEndDocument();
    }

    @Override
    public Class<NetworkMemberDTO> getEncoderClass() {
        return NetworkMemberDTO.class;
    }
}
