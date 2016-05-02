package utils;

import exceptions.UriException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xianggao on 5/1/16.
 */
public class UriUtils {
    public final static Set<String> SUPPORTED_PROTOCOLS = new HashSet<String>();
    static {
        SUPPORTED_PROTOCOLS.add("http://");
        SUPPORTED_PROTOCOLS.add("https://");
    }
    public final static String HTTPS_PROTOCOL = "https://";

    private UriUtils() { }

    public static String fetchUrl(final String uri) {
        if (uri == null) return null;
        String prefixUri = uri;
        try {
            if (!validateProtocol(uri)) {
                prefixUri = HTTPS_PROTOCOL + uri;
            }
            URL url = new URL(prefixUri);
            URLConnection c = url.openConnection();
            InputStream in = c.getInputStream();
            String ret = IOUtils.toString(in, CharEncoding.UTF_8);
            in.close();
            return ret;
        } catch (Exception e) {
            throw new UriException(e);
        }
    }

    public static boolean validateProtocol(final String uri) {
        if (uri == null) return false;
        for (final String protocol : SUPPORTED_PROTOCOLS) {
            if (uri.startsWith(protocol)) return true;
        }
        return false;
    }
}
