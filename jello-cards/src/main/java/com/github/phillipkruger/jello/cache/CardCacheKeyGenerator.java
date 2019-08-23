package com.github.phillipkruger.jello.cache;

import com.github.phillipkruger.jello.Card;
import java.lang.annotation.Annotation;
import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;
import org.jsr107.ri.annotations.DefaultGeneratedCacheKey;

/**
 * How to get the cache key from the Card Object
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class CardCacheKeyGenerator implements CacheKeyGenerator {
    @Override
    public GeneratedCacheKey generateCacheKey(final CacheKeyInvocationContext< ? extends Annotation> cacheKeyInvocationContext) {

        final CacheInvocationParameter[] allParameters = cacheKeyInvocationContext.getAllParameters();
        for (final CacheInvocationParameter parameter : allParameters) {
            if (Card.class.equals(parameter.getRawType())) {
                final Card card = Card.class.cast(parameter.getValue());
                return new DefaultGeneratedCacheKey(new Object[] { card.getId() });
            }
        }

        throw new IllegalArgumentException("No card argument found in method signature");
    }
}
