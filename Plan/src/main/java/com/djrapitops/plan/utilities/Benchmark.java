package main.java.com.djrapitops.plan.utilities;

import com.djrapitops.plugin.utilities.BenchUtil;
import main.java.com.djrapitops.plan.Log;

/**
 * @author Rsl1122
 */
public class Benchmark {

    /**
     * Constructor used to hide the public constructor
     */
    private Benchmark() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Start a benchmark.
     *
     * @param source Thing to bench.
     */
    public static void start(String source) {
        getBenchUtil().start(source);
    }

    /**
     * End a benchmark.
     *
     * @param source Thing to stop benching.
     * @return ms it took.
     */
    public static long stop(String source) {
        long ms = getBenchUtil().stop(source);
        if (ms != -1) {
            Log.debug(source + " took " + ms + " ms");
        }
        return ms;
    }

    /**
     * Used to add Benchmark timings to larger Debug log task parts.
     *
     * @param task   Task this benchmark is a part of.
     * @param source Bench source
     * @return Execution time in ms.
     */
    public static long stop(String task, String source) {
        return getBenchUtil().stop(task, source);
    }

    private static BenchUtil getBenchUtil() {
        return MiscUtils.getIPlan().benchmark();
    }
}
