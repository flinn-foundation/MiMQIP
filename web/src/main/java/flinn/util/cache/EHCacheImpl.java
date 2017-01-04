package flinn.util.cache;

import org.apache.log4j.Logger;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.management.ManagementService;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

/**
 * Basic naive (example) EHCacheImpl (starting point, not a finished product).
 * 
 * @param <K>
 * @param <V>
 */
public final class EHCacheImpl<K, V> implements ICache<K, V> {

	protected static final Logger LOG = Logger.getLogger(EHCacheImpl.class);

	private static boolean mBeanSet;
	private final String cacheName;
	private final CacheManager cacheManager;
	private final boolean putNullInCache;

	private EHCacheImpl(final String cacheName, final int timeToLiveSeconds,
			final int timeToIdleSeconds, final int maxElements,
			final boolean putNullInCache) {
		this.putNullInCache = putNullInCache;
		// this creates a Singleton ehCache mgr, so mult instances of the
		// wrapper are fine
		// for now we don't share cache (same name, across VMs, etc), but could
		this.cacheName = cacheName;
		this.cacheManager = CacheManager.create();
		// can only set the mBean once, or get an error
		if (!EHCacheImpl.mBeanSet) {
			EHCacheImpl.mBeanSet = true;
			MBeanServer mBeanServer = ManagementFactory
					.getPlatformMBeanServer();
			ManagementService.registerMBeans(this.cacheManager, mBeanServer,
					true, true, true, true, true);
		}

		LOG.debug("looking for cache: " + cacheName);

		// check if cache already exists, if not, create
		if (!this.cacheManager.cacheExists(cacheName)) {
			LOG.debug("creating new cache: " + cacheName);
			Cache cache = new Cache(new CacheConfiguration(this.cacheName,
					maxElements)
					.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
					.timeToLiveSeconds(timeToLiveSeconds)
					.timeToIdleSeconds(timeToIdleSeconds).diskPersistent(false)
					.overflowToDisk(false).overflowToOffHeap(false)
					.statistics(true));
			this.cacheManager.addCache(cache);
		}
	}

	public static <K, V> EHCacheImpl<K, V> getDefaultInstance(
			final String cacheName) {
		return new EHCacheImpl<K, V>(cacheName, 300, 100, 0, false);
	}

	public static <K, V> EHCacheImpl<K, V> getInstance(final String cacheName,
			final int timeToLiveSeconds, final int timeToIdleSeconds,
			final int maxElements, final boolean putNullInCache) {
		return new EHCacheImpl<K, V>(cacheName, timeToLiveSeconds,
				timeToIdleSeconds, maxElements, putNullInCache);
	}

	
	public void put(final K key, final V value) {
		if (this.putNullInCache) {
			this.getCache().put(new Element(key, value));
		} else {
			if (value != null) {
				this.getCache().put(new Element(key, value));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public V get(final K key) {
		Element element = this.getCache().get(key);
		if (element != null) {
			return (V) element.getObjectValue();
		}
		return null;
	}

	public void delete(final K key) {
		this.getCache().remove(key);
	}

	public void flush() {
		this.getCache().flush();
	}

	private Ehcache getCache() {
		return this.cacheManager.getEhcache(this.cacheName);
	}
	
	public void deleteAll() {
		this.getCache().removeAll();
	}
	
}