package com.wiprodigital.webcrawler.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sitemap {
    private String text;
    private String href;

    @JsonManagedReference
    private List<Sitemap> nodes = new ArrayList<>();

    public Sitemap(String name, String link) {
        this.text = name;
        this.href = link;
    }

    public List<Sitemap> getNodes() {
        return nodes;
    }

    public void setNodes(List<Sitemap> nodes) {
        this.nodes = nodes;
    }

    public void addChild(Sitemap child) {
        this.nodes.add(child);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "Sitemap{" +
                "text='" + text + '\'' +
                ", href='" + href + '\'' +
                ", nodes=" + nodes +
                '}';
    }
}
