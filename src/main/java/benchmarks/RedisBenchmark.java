package benchmarks;

import org.openjdk.jmh.annotations.*;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class RedisBenchmark {

    private Jedis jedis;

    @Setup(Level.Trial)
    public void setup() {
        jedis = new Jedis("localhost", 6379);

        for (int i = 0; i < 100; i++) {
            jedis.set("key" + i, "value" + i);
        }
    }

    @TearDown(Level.Trial)
    public void teardown() {
        jedis.close();
    }

    @Benchmark
    @Fork(value = 2, warmups = 2)
    public String testGetOperation() {
        return jedis.get("key500"); // Read a specific key
    }
}
