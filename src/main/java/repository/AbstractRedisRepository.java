package repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public abstract class AbstractRedisRepository implements AutoCloseable {
    private static JedisPooled pool;
    private static Jsonb jsonb = JsonbBuilder.create();

    private static String host;

    static {
        try {
            host = getNode("host");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int port;

    static {
        try {
            port = Integer.parseInt((getNode("port")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getNode(String node) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = new String(Files.readAllBytes(Paths.get("src/main/java/repository/Config.json")));
        JsonNode jsonNode = objectMapper.readTree(json);
        return jsonNode.get(node).asText();
    }

    public void initConnection() {
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
        pool = new JedisPooled(new HostAndPort(host, port), clientConfig);
    }

    public AbstractRedisRepository() {
        initConnection();
    }

    public boolean checkConnection() {
        try {
            return pool.getPool().getResource().isConnected();
        } catch (Exception e) {
            System.err.println("Redis connection failed: " + e.getMessage());
            return false; // Zwróć `false`, jeśli nie można połączyć się z Redis
        }
    }

    public void clearCache(){
        Set<String> keys = pool.keys("*");
        for (String key : keys){
            pool.del(key);
        }
    }

    @Override
    public void close() throws Exception {
        pool.close();
    }

    public static JedisPooled getPool() {
        return pool;
    }

    public static void setPool(JedisPooled pool) {
        AbstractRedisRepository.pool = pool;
    }

    public static Jsonb getJsonb() {
        return jsonb;
    }

    public static void setJsonb(Jsonb jsonb) {
        AbstractRedisRepository.jsonb = jsonb;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        AbstractRedisRepository.host = host;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        AbstractRedisRepository.port = port;
    }
}

