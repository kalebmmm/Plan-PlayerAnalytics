package main.java.com.djrapitops.plan.systems.cache;

import main.java.com.djrapitops.plan.data.Session;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import test.java.utils.MockUtils;
import test.java.utils.TestInit;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JavaPlugin.class)
public class SessionCacheTest {

    private final UUID uuid = MockUtils.getPlayerUUID();
    private SessionCache sessionCache;
    private Session session;

    @Before
    public void setUp() throws Exception {
        TestInit t = TestInit.init();
        sessionCache = new SessionCache(null);
        session = new Session(12345L, "World1", "SURVIVAL");
        sessionCache.cacheSession(uuid, session);
    }

    @Test
    public void testAtomity() {
        SessionCache reloaded = new SessionCache(null);
        Optional<Session> cachedSession = reloaded.getCachedSession(uuid);
        assertTrue(cachedSession.isPresent());
        assertEquals(session, cachedSession.get());
    }
}