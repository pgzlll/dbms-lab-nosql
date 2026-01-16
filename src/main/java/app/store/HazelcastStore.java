
package app.store;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import app.model.Student;

public class HazelcastStore {
    static HazelcastInstance hz;
    static IMap<String, Student> map;

    public static void init() {
        ClientConfig config = new ClientConfig();
        config.setClusterName("dev");

        config.getNetworkConfig().addAddress("127.0.0.1:5701");

        hz = HazelcastClient.newHazelcastClient(config); // config dosyasına bağlanır

        map = hz.getMap("ogrenciler");
        for (int i = 0; i < 10000; i++) {
            String id = "2025" + String.format("%06d", i);
            Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
            map.put(id, s);
        }
    }

    public static Student get(String id) {
        return map.get(id);
    }
}
