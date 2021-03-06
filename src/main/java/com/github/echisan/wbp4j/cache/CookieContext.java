package com.github.echisan.wbp4j.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * cookie缓存的上下文
 * 是com.github.echisan.wbp4j.cache.AbstractCookieContext的实现子类
 * 实现具体的逻辑
 */
public class CookieContext extends AbstractCookieContext {
    private static final Logger logger = LoggerFactory.getLogger(CookieContext.class);

    public CookieContext(CookieCacheAccessor accessor) {
        super(accessor);
    }

    public CookieContext() {
    }

    /**
     * 当保存cookie时，需要同时保存到内存以及缓存中，以方便下一次的调用获取
     *
     * @param cookie cookie
     * @throws IOException IOException
     */
    @Override
    public void saveCookie(String cookie) throws IOException {
        CookieHolder.setCookie(cookie);
        accessor.saveCookie(cookie);
    }

    @Override
    public String getCookie() throws IOException {
        String cookie = CookieHolder.getCookie();
        if (cookie == null) {

            logger.debug("cannot find cookie in memory, read from persistence cache.");
            cookie = accessor.getCookie();
        }
        if (cookie == null) {

            logger.debug("cannot find cookie in persistence cache.");

            return null;
        }

        CookieHolder.setCookie(cookie);

        return cookie;
    }

    @Override
    public void clear() throws IOException {
        CookieHolder.setCookie(null);
        accessor.clear();
        logger.debug("clear cookie cache success!");
    }
}
