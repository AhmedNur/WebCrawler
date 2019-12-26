package Crawler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Link {
    private String rootUrl;
    private String elementHtml;
    private String link;

    public Link(String rootUrl, String elementHtml) {
        this.rootUrl = rootUrl;
        this.elementHtml = elementHtml;
        if (elementHtml.contains("href=")) {
            this.link = elementHtml.substring(elementHtml.indexOf("href="));
            if (this.link.contains("\"")) {
                try {
                    this.link = this.link.substring(this.link.indexOf("\"") + 1);
                    this.link = this.link.substring(0, this.link.indexOf("\""));
                } catch (StringIndexOutOfBoundsException e) {
                    log.debug(elementHtml);
                }
            } else if (this.link.contains("'")) {
                this.link = this.link.substring(this.link.indexOf("'") + 1);
                this.link = this.link.substring(0, this.link.indexOf("'"));
            }
            if (this.link.startsWith("//")) {
                this.link = this.link.substring(2);
            } else if (this.link.startsWith("/")) {
                this.link = rootUrl + this.link;
            } else if (this.link.startsWith("#")) {
                this.link = null;
            }
        }
    }

    public String getLink() {
        return link;
    }
}
