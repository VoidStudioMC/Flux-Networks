package sonar.fluxnetworks.common.data.codecs;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import sonar.fluxnetworks.common.data.dto.NetworkMemberDTO;

public class NetworkMemberCodecProvider implements CodecProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry registry) {
        if (aClass == NetworkMemberDTO.class) {
            return (Codec<T>) new NetworkMemberCodec(registry);
        }
        return null;
    }
}
