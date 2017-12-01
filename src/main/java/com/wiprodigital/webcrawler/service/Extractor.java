package com.wiprodigital.webcrawler.service;

import com.wiprodigital.webcrawler.model.*;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Extractor {

    private HashSet<String> links = new HashSet<>();
    private CrawlResults cr = new CrawlResults();

    private URL domainUrl;
    private String urlStart;
    private long id = 0;

    private final static Pattern STATIC_RESOURCE = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz))$");
    private final static Pattern IMG_FROM_STYLE =  Pattern.compile("^.+url\\(.+[\"|']*((http|https|ftp).+[\\w\\W]+)[\"|']\\).+$");

    private Logger logger = Logger.getLogger(this.getClass());

    Extractor(URL domainUrl) {
        this.domainUrl = domainUrl;
        this.urlStart = domainUrl.getProtocol() + "://" + domainUrl.getAuthority();
        try {
            Document doc = Jsoup.connect(domainUrl.toString()).get();
            cr.setTitle(doc.title());
            Elements descElements = doc.select("meta[name=description]");
            if(descElements != null) {
                Element descEle = descElements.first();
                if(descEle != null) {
                    String desc = descEle.attr("content");
                    cr.setDescription(desc);
                }
            }
            cr.setUrl(domainUrl.toString());
        } catch(Exception e) {
            throw new RuntimeException("Failed to extract title from url [" + domainUrl.toString() + "]", e);
        }
    }

    private void extract(String url) {
        if (!links.contains(url)) {
            Document document = null;
            try {
                document = Jsoup.connect(url).get();
            } catch (Exception e) {
                logger.error("URL: " + url+", " + e.getMessage());
            }
            if(document != null) {
                try {
                    links.add(url);
                    Elements allLinks = document.select("[href],[src],[content],[style~=(?i)\\.(png|jpe?g)]");

                    for (Element page : allLinks) {
                        String href = page.attr("abs:href");
                        if (!links.contains(href) || !links.contains(page.attr("abs:src"))) {
                            extractResource(page);
                        } else if (page.is("[style~=(?i)\\.(gif|png|jpe?g)]")) {
                            String style = page.attr("style");
                            Matcher m = IMG_FROM_STYLE.matcher(style);
                            while (m.find()) {
                                String imgUrl = m.group(1);
                                addResource(getLinkName(page), imgUrl, ResourceType.IMAGE, GroupType.STATIC);
                            }
                        }
                        if (!links.contains(href) && href.startsWith(urlStart) && href.matches("^.+\\.(html|htm|asp|php)$") && !isStaticResource(href)) {
                            extract(href);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to extract details from url [" + url + "]", e);
                }
            }
        }
    }

    private void addResource(String linkName, String url, ResourceType type, GroupType gType) {
        if(url == null || url.trim().isEmpty()) {
            return;
        }
        Resource r = new Resource(++id, linkName, url, type);
        r.setGroup(gType);
        cr.addResource(r);
    }

    private void extractResource(Element src) {
        if (src.is("img[src]")) {
            addResource(getLinkName(src), src.attr("abs:src"), ResourceType.IMAGE,GroupType.STATIC);
        } if (isInternalStaticResource(src.attr("abs:src")) || isInternalStaticResource(src.attr("abs:href"))) {
            addResource(getLinkName(src), src.attr("abs:src"), ResourceType.LINK,GroupType.INTERNAL);
        } else if (src.attr("abs:href").startsWith(this.urlStart)) {
            addResource(getLinkName(src), src.attr("abs:href"), ResourceType.LINK,GroupType.INTERNAL);
        } else {
            addResource(getLinkName(src), src.attr("abs:href"), ResourceType.LINK,GroupType.EXTERNAL);
        }
    }

    private String getLinkName(Element src) {
        String name = src.text();
        if(src.is("link[href]")) {
            name = src.attr("rel");
        }
        if(name == null || name.isEmpty()) {
            name = src.attr("alt");
        }
        if(name == null || name.isEmpty()) {
            name = src.attr("title");
        }

        if(name == null || name.isEmpty()) {
            if(src.is("[href]")) {
                String href = src.attr("abs:href");
                try {
                    URL u = new URL(href);
                    name = u.getAuthority();
                } catch (Exception ignore){
                    // Ignore
                }
            }
        }
        return name;
    }

    private List<Sitemap> extractSitemap() {
        List<Sitemap> sitemaps = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(domainUrl.toString()).get();
            Elements menus = doc.select("li.wd-navbar-nav-elem");
            Set<String> sitemapSet = new HashSet<>();
            for (Element menu : menus) {
                Element mainMenu = menu.select("a[href]").first();
                String url = mainMenu.attr("href");
                Sitemap main = new Sitemap(mainMenu.text(), url);
                Elements subMenus = menu.select("ul.dropdown-menu > li > a");
                sitemapSet.clear();
                for(Element subMenu : subMenus) {
                    if(!sitemapSet.contains(subMenu.text())) {
                        // Avoid duplicate item.
                        sitemapSet.add(subMenu.text());
                        Sitemap child = new Sitemap(subMenu.text(), subMenu.attr("href"));
                        main.addChild(child);
                    }
                }
                sitemaps.add(main);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract sitemap details from url [" + domainUrl + "]", e);
        }
        return sitemaps;
    }

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. Indicates whether the given url should be crawled or not.
     */
    private boolean isStaticResource(String referringPage) {
        String href = referringPage.toLowerCase();
        return STATIC_RESOURCE.matcher(href).matches();
    }

    private boolean isInternalStaticResource(String referringPage) {
        String href = referringPage.toLowerCase();
        return STATIC_RESOURCE.matcher(href).matches() && href.startsWith(urlStart);
    }



    CrawlResults extractAndGet() {
        extract(domainUrl.toString());
        cr.setSitemap(extractSitemap());
        return cr;
    }
}
