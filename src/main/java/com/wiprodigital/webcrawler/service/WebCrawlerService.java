package com.wiprodigital.webcrawler.service;

import com.wiprodigital.webcrawler.model.CrawlResults;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebCrawlerService {
    private Logger logger = Logger.getLogger(this.getClass());

    private static ConcurrentHashMap<String, CrawlResults> cache = new ConcurrentHashMap<>();

    public CrawlResults extract(String url) {
        url = decode(url);

        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(url)) {
            throw new RuntimeException("Invalid URL");
        }

        CrawlResults existing = cache.get(url);
        if(existing != null) {
            return existing;
        }

        logger.debug("Extracting details from url [" + url + "]");

        CrawlResults cr = null;
        try {
            URL u = new URL(url);

            Extractor e = new Extractor(u);
            cr = e.extractAndGet();
            cache.put(url, cr);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extra details from site [" + url + "]", e);
        }

        return cr;
    }

    public String decode(String value) {
        String decodedText = null;
        try {
            decodedText = URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode url", e);
        }
        return decodedText;
    }
}
