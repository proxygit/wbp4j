package cn.echisan.wbp4j.io;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;

/**
 * Created by echisan on 2018/11/5
 */
public class CookieContext implements CookieCacheable {
    private static final Logger logger = Logger.getLogger(CookieContext.class);
    private static ClassLoader cookieContextClassLoader = CookieContext.class.getClassLoader();
    private static volatile CookieContext context = null;
    private static final Object lock = new Object();
    private volatile String COOKIE = null;
    private String defaultCookieFileName = "wbp-cookie-cache.txt";

    private CookieContext() {
    }

    public static CookieContext getInstance() {
        if (context == null) {
            synchronized (lock) {
                if (context == null) {
                    context = new CookieContext();
                } else {
                    return context;
                }
            }
        }
        return context;
    }

    public synchronized String getCOOKIE() throws IOException {
        if (context.COOKIE != null) {
            return context.COOKIE;
        }
        String cookie;
        if ((cookie = context.readCookie()) == null) {
            return null;
        }
        return cookie;
    }

    public synchronized void setCOOKIE(String cookie) throws IOException {
        context.COOKIE = cookie;
        saveCookie(cookie);
    }

    @Override
    public void saveCookie(String cookie) throws IOException {
        String path;
        URL resourceAsURL = cookieContextClassLoader.getResource(defaultCookieFileName);
        if (resourceAsURL == null) {
            resourceAsURL = cookieContextClassLoader.getResource("");
            path = resourceAsURL + defaultCookieFileName;
            if (path.startsWith("file:")) {
                path = path.replace("file:", "");
            }
        } else {
            path = resourceAsURL.getPath();
        }
        File file = new File(path);
        if (!file.exists()) {
            boolean mkdir = file.createNewFile();
            if (!mkdir) {
                logger.warn("无法缓存cookie.", new IOException("缓存cookie失败！path:" + file.getAbsolutePath()));
            }
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
        bw.write(cookie);
        bw.flush();
        bw.close();
    }

    @Override
    public String readCookie() throws IOException {
        URL resourceAsURL = cookieContextClassLoader.getResource(defaultCookieFileName);
        if (resourceAsURL == null) {
            return null;
        }
        File file = new File(resourceAsURL.getPath());
        if (!file.exists()) {
            logger.warn("无法读取cookie，缓存文件不存在", new IOException());
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        br.close();
        return sb.toString();
    }
}
