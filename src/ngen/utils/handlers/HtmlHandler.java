package ngen.utils.handlers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ngen.utils.UriUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianggao on 5/1/16.
 */
public class HtmlHandler {
    private final Document doc;

    public HtmlHandler(final String html) {
        doc = Jsoup.parse(html);
    }

    public List<String> fetchAllUris() {
        Elements links = doc.select("a[href]");
        return translate(links);
    }

    private List<String> translate(Elements links) {
        List<String> ret = new ArrayList<String>();
        for (Element link : links) {
            String uri = link.attr("href");
            if (validateUri(uri)) {
                ret.add(uri);
            }
        }
        return ret;
    }

    private boolean validateUri(final String uri) {
        try {
            new URL(uri);
            return UriUtils.validateProtocol(uri);
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
