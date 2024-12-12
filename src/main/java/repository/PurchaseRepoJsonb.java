package repository;

import jakarta.json.bind.Jsonb;
import model.PurchaseJsonb;
import org.bson.types.ObjectId;
import redis.clients.jedis.JedisPooled;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PurchaseRepoJsonb extends AbstractRedisRepository {

    private final JedisPooled pool = getPool();
    private final Jsonb jsonb = getJsonb();
    private final String prefix = "purchase:";

    public void save(PurchaseJsonb purchase) {
        if (purchase.getId() == null) {
            purchase.setId(new ObjectId().toString());
        }
        String key = prefix + purchase.getId();
        String json = jsonb.toJson(purchase);
        pool.set(key, json);
    }

    public void update(PurchaseJsonb purchase) {
        if (purchase.getId() != null) {
            save(purchase);
        } else {
            throw new IllegalArgumentException("Updated purchase id is null.");
        }
    }

    public List<PurchaseJsonb> findAll() {
        List<PurchaseJsonb> all = new ArrayList<>();
        Set<String> keys = pool.keys(prefix + "*");
        for (String key : keys) {
            all.add(jsonb.fromJson(pool.get(key), PurchaseJsonb.class));
        }
        return all;
    }

    public PurchaseJsonb findById(ObjectId id) {
        String key = prefix + id;
        String json = pool.get(key);
        return jsonb.fromJson(json, PurchaseJsonb.class);
    }

    public void delete(ObjectId id) {
        String key = prefix + id.toString();
        pool.del(key);
    }

    public void saveWithTimeout(PurchaseJsonb purchase, int timeoutSeconds) {
        if (purchase.getId() == null) {
            purchase.setId(new ObjectId().toString());
        }
        String key = prefix + purchase.getId();
        String json = jsonb.toJson(purchase);
        pool.setex(key, timeoutSeconds, json); // Ustaw timeout
    }

    @Override
    public void clearCache() {
        Set<String> keys = pool.keys(prefix + "*");
        for (String key : keys){
            pool.del(key);
        }
    }
}