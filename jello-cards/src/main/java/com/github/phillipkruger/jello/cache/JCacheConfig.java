package com.github.phillipkruger.jello.cache;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import javax.annotation.Resource;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * JCache, CDI, JNDI. Configure the JCache Provider
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * 
 */
@Log
@ApplicationScoped
public class JCacheConfig {
    
    @Resource(lookup = "java:global/jcacheConfigFile")
    private String jcacheConfigFile;
    
    @Produces
    @ApplicationScoped
    @Getter
    private CacheManager manager;
    
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        try {
            CachingProvider cachingProvider = Caching.getCachingProvider();
            log.log(Level.INFO, " ### JCache starting");
            URI resource = getConfigFile(cachingProvider);
            manager = cachingProvider.getCacheManager(resource,getClass().getClassLoader());

            for(String name:manager.getCacheNames()) {
                log.log(Level.INFO, "Found cache [{0}]", name);
            }
        }catch (CacheException ce){
            log.log(Level.WARNING, "Could not start cache ! [{0}]", ce.getMessage());
        }      
    }
    
    private URI getConfigFile(CachingProvider cachingProvider){
        try {
            ClassLoader cl = getClass().getClassLoader();
            URI resource = cl.getResource(jcacheConfigFile).toURI();
            log.log(Level.INFO, "Using [{0}] as configuration", resource);
            return resource;
        }catch (URISyntaxException ex) {
            log.log(Level.WARNING, "No config file provided, using default");
            return cachingProvider.getDefaultURI();
        }
    }
}
