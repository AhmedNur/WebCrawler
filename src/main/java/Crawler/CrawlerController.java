package Crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RestController
public class CrawlerController {

    @Autowired
    CrawlerService cs;

    @RequestMapping("/crawler")
    public Set<Map.Entry<String, String>> initiateCrawl(@RequestBody CrawlerForm crawlerForm) throws IOException {
        cs.crawl(crawlerForm.getAddress(), crawlerForm.getDepth());
        return cs.getCrawledUrls();
    }

}
