package app.store;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import app.model.Student;
import com.google.gson.Gson;

public class RedisStore {
    static JedisPool pool;
    static Gson gson = new Gson();

    public static void init() {
        // Pool ayarları: 10 concurrent siege için yeterli
        JedisPoolConfig cfg = new JedisPoolConfig();
        cfg.setMaxTotal(50);   // aynı anda açılabilecek toplam bağlantı
        cfg.setMaxIdle(20);
        cfg.setMinIdle(5);

        pool = new JedisPool(cfg, "localhost", 6379);

        // 10k kaydı bas (tek bağlantı kullanarak)
        try (Jedis jedis = pool.getResource()) {
            for (int i = 0; i < 10000; i++) {
                String id = "2025" + String.format("%06d", i);
                Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
                jedis.set(id, gson.toJson(s));
            }
        }
    }

    public static Student get(String id) {
        try (Jedis jedis = pool.getResource()) {
            String json = jedis.get(id);
            return json != null ? gson.fromJson(json, Student.class) : null;
        }
    }
}
