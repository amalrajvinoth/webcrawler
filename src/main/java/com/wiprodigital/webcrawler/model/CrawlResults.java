package com.wiprodigital.webcrawler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class CrawlResults {

    private String title;
    private String description;
    private String url;
    private List<Sitemap> sitemap = new ArrayList<>();
    private List<Resource> resources = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Sitemap> getSitemap() {
        return sitemap;
    }

    public void setSitemap(List<Sitemap> sitemap) {
        this.sitemap = sitemap;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public void addResource(Resource resource) {
        this.resources.add(resource);
    }
}
