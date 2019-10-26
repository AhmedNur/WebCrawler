package Crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CrawlerController {

    @Autowired
    CrawlerService cs;

    @PostMapping("/crawler")
    public ResponseEntity initiateCrawl(@RequestParam("url") String url, @RequestParam("depth") int depth) throws IOException {
        return new ResponseEntity<>(cs.crawl(url, depth), HttpStatus.OK);
    }

}
