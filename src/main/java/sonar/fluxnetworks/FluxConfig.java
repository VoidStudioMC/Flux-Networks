package sonar.fluxnetworks;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.bson.Document;
import org.bson.codecs.UuidCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import sonar.fluxnetworks.common.data.dto.FluxNetworkDataDTO;
import sonar.fluxnetworks.common.data.codecs.ChunkPosCodec;
import sonar.fluxnetworks.common.data.codecs.FluxConnectorCodec;
import sonar.fluxnetworks.common.data.codecs.FluxNetworkCodecProvider;
import sonar.fluxnetworks.common.data.codecs.FluxNetworkDataCodecProvider;
import sonar.fluxnetworks.common.data.codecs.NetworkMemberCodecProvider;
import sonar.fluxnetworks.common.handler.ItemEnergyHandler;
import sonar.fluxnetworks.common.handler.TileEntityHandler;

import java.io.File;
import java.util.UUID;

import static sonar.fluxnetworks.common.data.TagConstants.INDEX_FIELD;

public class FluxConfig {

    public static Configuration config;

    public static final String GENERAL = "general";
    public static final String CLIENT = "client";
    public static final String ENERGY = "energy";
    public static final String NETWORKS = "networks";
    public static final String BLACKLIST = "blacklists";
    public static final String DATABASE = "database";

    public static MongoCollection<FluxNetworkDataDTO> MONGO_COLLECTION;

    public static boolean enableButtonSound, enableOneProbeBasicInfo, enableOneProbeAdvancedInfo, enableOneProbeSneaking;
    public static boolean enableFluxRecipe, enableOldRecipe, enableChunkLoading, enableSuperAdmin;
    public static int defaultLimit, basicCapacity, basicTransfer, herculeanCapacity, herculeanTransfer, gargantuanCapacity, gargantuanTransfer;
    public static int maximumPerPlayer, superAdminRequiredPermission;
    public static String[] blockBlacklistStrings, itemBlackListStrings;
    public static UUID MONGO_SERVER_ID;
    private static MongoClient MONGO_CLIENT;
    private static MongoDatabase MONGO_DB;
    private static String MONGO_URL;
    private static String MONGO_DB_NAME;
    private static String MONGO_COLLECTION_NAME;

    public static void init(File file) {
        config = new Configuration(new File(file.getPath(), "flux_networks.cfg"));
        config.load();
        read();
        verifyAndReadBlacklist();
        config.save();
        generateFluxChunkConfig();
        connectToDB();
    }

    private static void connectToDB(){
        MONGO_CLIENT = MongoClients.create(MONGO_URL);
        ChunkPosCodec chunkPosCodec = new ChunkPosCodec();
        UuidCodec uuidCodec = new UuidCodec();
        FluxConnectorCodec fluxConnectorCodec = new FluxConnectorCodec();
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(chunkPosCodec, uuidCodec, fluxConnectorCodec),
                CodecRegistries.fromProviders(new FluxNetworkCodecProvider(), new FluxNetworkDataCodecProvider(), new NetworkMemberCodecProvider()),
                MongoClientSettings.getDefaultCodecRegistry()));
        MONGO_DB = MONGO_CLIENT.getDatabase(MONGO_DB_NAME);
        MONGO_COLLECTION = MONGO_DB.getCollection(MONGO_COLLECTION_NAME, FluxNetworkDataDTO.class).withCodecRegistry(codecRegistry);
        if (MONGO_COLLECTION.countDocuments(new Document(INDEX_FIELD, MONGO_SERVER_ID)) < 1) {
            MONGO_COLLECTION.insertOne(new FluxNetworkDataDTO(MONGO_SERVER_ID));
        }
    }

    public static void verifyAndReadBlacklist() {
        TileEntityHandler.blockBlacklist.clear();
        for (String str : blockBlacklistStrings) {
            if (!str.contains(":")) {
                FluxNetworks.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, please use 'modid:name@meta'");
            }
            String root = str;
            int meta = -1;
            if (str.contains("@")) {
                String[] split = str.split("@");
                root = split[0];
                try {
                    meta = Integer.parseInt(split[1]);
                    TileEntityHandler.blockBlacklist.put(root, meta);
                } catch (Exception e) {
                    FluxNetworks.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, meta must be positive integer'");
                }
            } else {
                TileEntityHandler.blockBlacklist.put(root, meta);
            }
        }
        ItemEnergyHandler.itemBlackList.clear();
        for (String str : itemBlackListStrings) {
            if (!str.contains(":")) {
                FluxNetworks.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, please use 'modid:name@meta'");
            }
            String root = str;
            int meta = -1;
            if (str.contains("@")) {
                String[] split = str.split("@");
                root = split[0];
                try {
                    meta = Integer.parseInt(split[1]);
                    ItemEnergyHandler.itemBlackList.put(root, meta);
                } catch (Exception e) {
                    FluxNetworks.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, meta must be positive integer'");
                }
            } else {
                ItemEnergyHandler.itemBlackList.put(root, meta);
            }
        }
    }

    public static void generateFluxChunkConfig() {
        if (!ForgeChunkManager.getConfig().hasCategory(FluxNetworks.MODID)) {
            ForgeChunkManager.getConfig().get(FluxNetworks.MODID, "maximumChunksPerTicket", 1000000).setMinValue(0);
            ForgeChunkManager.getConfig().get(FluxNetworks.MODID, "maximumTicketCount", 1000000).setMinValue(0);
            ForgeChunkManager.getConfig().save();
        }
    }

    public static void read() {

        defaultLimit = config.getInt("Default Transfer Limit", ENERGY, 800000, 0, Integer.MAX_VALUE, "The default transfer limit of a flux connector");

        basicCapacity = config.getInt("Basic Storage Capacity", ENERGY, 1000000, 0, Integer.MAX_VALUE, "");
        basicTransfer = config.getInt("Basic Storage Transfer", ENERGY, 20000, 0, Integer.MAX_VALUE, "");
        herculeanCapacity = config.getInt("Herculean Storage Capacity", ENERGY, 8000000, 0, Integer.MAX_VALUE, "");
        herculeanTransfer = config.getInt("Herculean Storage Transfer", ENERGY, 120000, 0, Integer.MAX_VALUE, "");
        gargantuanCapacity = config.getInt("Gargantuan Storage Capacity", ENERGY, 128000000, 0, Integer.MAX_VALUE, "");
        gargantuanTransfer = config.getInt("Gargantuan Storage Transfer", ENERGY, 1440000, 0, Integer.MAX_VALUE, "");

        maximumPerPlayer = config.getInt("Maximum Networks Per Player", NETWORKS, 3, -1, Integer.MAX_VALUE, "Maximum networks each player can have. -1 = no limit");
        enableSuperAdmin = config.getBoolean("Allow Network Super Admin", NETWORKS, true, "Allows someone to be a network super admin, otherwise, no one can access or dismantle your flux devices or delete your networks without permission");
        superAdminRequiredPermission = config.getInt("Permission level required to activate Super Admin", NETWORKS, 1, 0, Integer.MAX_VALUE, "See ops.json. If the player has permission level equal or greater to the value set here they will be able to Activate Super Admin. Setting this to 0 will allow anyone to active Super Admin.");

        enableFluxRecipe = config.getBoolean("Enable Flux Recipe", GENERAL, true, "Enables redstones being compressed with the bedrock and obsidian to get flux");
        enableOldRecipe = config.getBoolean("Enable Old Recipe", GENERAL, false, "Enables redstone being turned into Flux when dropped in fire. (Need \"Enable Flux Recipe\" = true, so the default recipe can't be disabled if turns this on)");
        enableChunkLoading = config.getBoolean("Allow Flux Chunk Loading", GENERAL, true, "Allows flux tiles to work as chunk loaders");

        MONGO_URL = config.getString("Url to connect to mongo instance", DATABASE, "mongodb://localhost:27017", "");
        MONGO_DB_NAME = config.getString("Mongo database name", DATABASE, "local", "");
        MONGO_COLLECTION_NAME = config.getString("Mongo collection name", DATABASE, "networks", "");
        MONGO_SERVER_ID = UUID.fromString(config.getString("Unique server id for multi-server features", DATABASE, UUID.randomUUID().toString(), "Do not change this value unless you want to lose all network data!"));

        enableButtonSound = config.getBoolean("Enable GUI Button Sound", CLIENT, true, "Enable navigation buttons sound when pressing it");
        enableOneProbeBasicInfo = config.getBoolean("Enable Basic One Probe Info", CLIENT, true, "Displays: Network Name, Live Transfer Rate & Internal Buffer");
        enableOneProbeAdvancedInfo = config.getBoolean("Enable Advanced One Probe Info", CLIENT, true, "Displays: Transfer Limit & Priority etc");
        enableOneProbeSneaking = config.getBoolean("Enable sneaking to display Advanced One Probe Info", CLIENT, true, "Displays Advanced Info when sneaking only");

        blockBlacklistStrings = getBlackList("Block Connection Blacklist", BLACKLIST, new String[]{"actuallyadditions:block_phantom_energyface"}, "a blacklist for blocks which flux connections shouldn't connect to, use format 'modid:name@meta'");
        itemBlackListStrings = getBlackList("Item Transfer Blacklist", BLACKLIST, new String[]{}, "a blacklist for items which the Flux Controller shouldn't transfer to, use format 'modid:name@meta'");
    }

    public static String[] getBlackList(String name, String category, String[] defaultValue, String comment) {
        Property prop = config.get(category, name, defaultValue);
        prop.setLanguageKey(name);
        prop.setValidValues(null);
        prop.setComment(comment);
        return prop.getStringList();
    }
}
