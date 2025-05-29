package sonar.fluxnetworks.common.data.codecs;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import sonar.fluxnetworks.common.data.dto.FluxConnectorDTO;
import sonar.fluxnetworks.common.data.dto.FluxNetworkDTO;
import sonar.fluxnetworks.common.data.dto.NetworkMemberDTO;

import java.util.ArrayList;
import java.util.UUID;

import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_COLOR;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_ENERGY;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_ID;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_NAME;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_PASSWORD;
import static sonar.fluxnetworks.common.data.TagConstants.NETWORK_SECURITY;
import static sonar.fluxnetworks.common.data.TagConstants.OWNER_UUID;
import static sonar.fluxnetworks.common.data.TagConstants.PLAYER_LIST;
import static sonar.fluxnetworks.common.data.TagConstants.UNLOADED;
import static sonar.fluxnetworks.common.data.TagConstants.WIRELESS_MODE;

public class FluxNetworkCodec implements Codec<FluxNetworkDTO>{

    Codec<UUID> uuidCodec;
    Codec<FluxConnectorDTO> fluxConnectorCodec;
    Codec<NetworkMemberDTO> networkMemberCodec;

    public FluxNetworkCodec(CodecRegistry registry) {
        this.uuidCodec = registry.get(UUID.class);
        this.fluxConnectorCodec = registry.get(FluxConnectorDTO.class);
        this.networkMemberCodec = registry.get(NetworkMemberDTO.class);
    }

    @Override
    public FluxNetworkDTO decode(BsonReader reader, DecoderContext decoderContext) {
        FluxNetworkDTO fluxNetworkDTO = new FluxNetworkDTO();
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (name.equals(NETWORK_ID)) {
                fluxNetworkDTO.setNetworkID(reader.readInt64());
            } else if (name.equals(NETWORK_NAME)) {
                fluxNetworkDTO.setNetworkName(reader.readString());
            } else if (name.equals(OWNER_UUID)) {
                fluxNetworkDTO.setOwnerUUID(uuidCodec.decode(reader, decoderContext));
            } else if (name.equals(NETWORK_SECURITY)) {
                fluxNetworkDTO.setNetworkSecurity(reader.readInt32());
            } else if (name.equals(NETWORK_PASSWORD)) {
                fluxNetworkDTO.setNetworkPassword(reader.readString());
            } else if (name.equals(NETWORK_COLOR)) {
                fluxNetworkDTO.setNetworkColor(reader.readInt32());
            } else if (name.equals(NETWORK_ENERGY)) {
                fluxNetworkDTO.setNetworkEnergy(reader.readInt32());
            } else if (name.equals(WIRELESS_MODE)) {
                fluxNetworkDTO.setWirelessMode(reader.readInt32());
            } else if (name.equals(PLAYER_LIST)) {
                reader.readStartArray();
                fluxNetworkDTO.setPlayerList(new ArrayList<>());
                while (reader.readBsonType() == BsonType.DOCUMENT) {
                    fluxNetworkDTO.getPlayerList()
                            .add(decoderContext.decodeWithChildContext(networkMemberCodec, reader));
                }
                reader.readEndArray();
            } else if (name.equals(UNLOADED)) {
                reader.readStartArray();
                fluxNetworkDTO.setUnloaded(new ArrayList<>());
                while (reader.readBsonType() == BsonType.DOCUMENT) {
                    fluxNetworkDTO.getUnloaded()
                            .add(decoderContext.decodeWithChildContext(fluxConnectorCodec, reader));
                }
                reader.readEndArray();
            }
        }
        reader.readEndDocument();
        return fluxNetworkDTO;
    }

    @Override
    public void encode(BsonWriter writer, FluxNetworkDTO value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeInt64(NETWORK_ID, value.getNetworkID());
        writer.writeString(NETWORK_NAME, value.getNetworkName());
        writer.writeName(OWNER_UUID);
        uuidCodec.encode(writer, value.getOwnerUUID(), encoderContext);
        writer.writeInt32(NETWORK_SECURITY, value.getNetworkSecurity());
        writer.writeString(NETWORK_PASSWORD, value.getNetworkPassword());
        writer.writeInt32(NETWORK_COLOR, value.getNetworkColor());
        writer.writeInt32(NETWORK_ENERGY, value.getNetworkEnergy());
        writer.writeInt32(WIRELESS_MODE, value.getWirelessMode());
        if (value.getPlayerList() != null && !value.getPlayerList().isEmpty()) {
            writer.writeName(PLAYER_LIST);
            writer.writeStartArray();
            for (NetworkMemberDTO networkMember : value.getPlayerList()) {
                networkMemberCodec.encode(writer, networkMember, encoderContext);
            }
            writer.writeEndArray();
        }
        if (value.getUnloaded() != null && !value.getUnloaded().isEmpty()) {
            writer.writeName(UNLOADED);
            writer.writeStartArray();
            for (FluxConnectorDTO connector : value.getUnloaded()) {
                fluxConnectorCodec.encode(writer, connector, encoderContext);
            }
            writer.writeEndArray();
        }
        writer.writeEndDocument();
    }

    @Override
    public Class<FluxNetworkDTO> getEncoderClass() {
        return FluxNetworkDTO.class;
    }
}
