package Crawler;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Document {
    private String url;
    private String rootUrl;
    private String html;
    private ArrayList<Link> links;

    public Document(String url, String html) {
        this.html = html;
        if (url.startsWith("http://") || url.startsWith("https://")) {
            this.url = url;
        } else if (url.startsWith("//")) {
            this.url = "http://" + url.substring(2);
        } else if (url.isEmpty()) {
            return;
        } else {
            this.url = "http://" + url;
        }
        String[] pathArray = this.url.split("/");
        this.rootUrl = pathArray[0] + "//" + pathArray[2];

        links = new ArrayList<>();
        if (html == null) {
            log.debug(url);
        } else {
            Matcher matcher = Pattern.compile("<.[^>]*>").matcher(html);
            while (matcher.find()) {
                links.add(new Link(rootUrl, matcher.group()));
            }
        }
    }

    public ArrayList<String> getLinks() {
        if (links == null) {
            return new ArrayList<>();
        }
        ArrayList<String> retrievedLinks = new ArrayList<>();
        for (Link l : links) {
            if (l.getLink() != null) {
                retrievedLinks.add(l.getLink());
            }
        }
        return retrievedLinks;
    }
}
