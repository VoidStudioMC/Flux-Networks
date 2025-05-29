package sonar.fluxnetworks.common.data.codecs;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import sonar.fluxnetworks.common.data.dto.FluxNetworkDataDTO;

public class FluxNetworkDataCodecProvider implements CodecProvider {
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry registry) {
        if (aClass == FluxNetworkDataDTO.class) {
            return (Codec<T>) new FluxNetworkDataCodec(registry);
        }
        return null;
    }
}
