package benchmarks;

import org.openjdk.jmh.annotations.*;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class RedisBenchmark {

    private Jedis jedis;
    private Random random;

    @Setup(Level.Trial)
    public void setup() {
        jedis = new Jedis("localhost", 6379);
        random = new Random();

        // Przygotowanie dużej ilości danych
        for (int i = 0; i < 10000; i++) {
            jedis.set("key" + i, "value" + i);
        }
    }

    @TearDown(Level.Trial)
    public void teardown() {
        if (jedis != null) {
            jedis.flushAll();
            jedis.close();
        }
    }

    @Benchmark
    @Fork(1)
    public String testGetOperation() {
        int randomKey = random.nextInt(10000); // Losowy klucz z zakresu 0–9999
        return jedis.get("key" + randomKey);
    }

    @Benchmark
    @Fork(1)
    public void testSetOperation() {
        jedis.set("key" + System.nanoTime(), "value");
    }

    @Benchmark
    @Fork(1)
    public void testListPushOperation() {
        jedis.lpush("myList", "value");
    }

    @Benchmark
    @Fork(1)
    public String testListPopOperation() {
        return jedis.rpop("myList");
    }
}
