package Crawler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CrawlerService {

    private Map<String, String> crawledUrls = new HashMap<>();

    public String crawl(String address, int depth) throws IOException {
        Document currentSite = new Document("", "");
        RestTemplate request = new RestTemplate();
        ResponseEntity response;
        Boolean isUrlResolved = false;
        while (!isUrlResolved) {
            try {
                if (!address.startsWith("http")) {
                    address = "http://" + address;
                }
                response = request.getForEntity(address, String.class);
            } catch (IllegalArgumentException e) {
                if (address.isEmpty()) {
                    log.debug("Blank Address");
                    isUrlResolved = true;
                    continue;
                }
                log.debug("Request processing failed with root cause, " + e.getCause() + ": " + address);
                isUrlResolved = true;
                continue;
            } catch (HttpClientErrorException e) {
                log.debug(e.getStatusText() + ": " + address);
                isUrlResolved = true;
                continue;
            } catch (Exception e) {
                log.debug(e.getMessage() + ": " + address);
                isUrlResolved = true;
                continue;
            }
            if (response.getStatusCode().value() == 200 && response.hasBody()) {
                currentSite = new Document(address, response.getBody().toString());
                isUrlResolved = true;
            } else if (response.getHeaders().containsKey("Location")) {
                address = response.getHeaders().get("Location").get(0);
            } else {
                log.debug(address);
                log.debug(response.toString());
                isUrlResolved = true;
            }
        }
        if (depth > 0) {
            for (String link : currentSite.getLinks()) {
                crawledUrls.put(link, "");
            }
            for (String link : currentSite.getLinks()) {
                crawl(link, --depth);
                depth++;
            }
        }
        return crawledUrls.keySet().toString();
    }
}
