package Crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class CrawlerService {

    private Map<String, String> crawledUrls = new HashMap<>();

    public Boolean validateUrl(String address)
    {
        try {
            URL url = new URL(address);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public Document getSite(String address) throws IOException
    {
        Document doc = Jsoup.connect(address).get();
        return doc;
    }

    public Elements getLinks(Document doc)
    {
        Elements allElements = doc.getAllElements();
        Elements links = allElements.select("a[href]");
        return links;
    }

    public void crawl(String address, int depth) throws IOException
    {
        if(validateUrl(address))
        {
            try
            {
                Document currentSite = getSite(address);
                Elements retrievedUrls = getLinks(currentSite);
                crawledUrls.put(address, currentSite.html());
                for (Element link : retrievedUrls)
                {
                    if (validateUrl(link.attr("href")) && depth > 0)
                    {
                        crawledUrls.put(link.attr("href"), "");
                    }
                }
                for (Element link : retrievedUrls)
                {
                    if (depth > 0) {
                        crawl(link.attr("href"), --depth);
                        depth++;
                    }
                }
            }
            catch (IOException e)
            {
            }
        }
    }

    public Set<Map.Entry<String, String>> getCrawledUrls()
    {
        return crawledUrls.entrySet();
    }
}
