package com.wiprodigital.webcrawler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {
    private long id;
    private String name;
    private ResourceType type;
    private String link;
    private GroupType group;

    public Resource(long id, String text, String url, ResourceType rt) {
        this.id = id;
        this.name = text;
        this.link = url;
        this.type = rt;
    }

    public Resource(long id, String text, String url, ResourceType rt, GroupType group) {
        this.id = id;
        this.name = text;
        this.link = url;
        this.type = rt;
        this.group = group;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public GroupType getGroup() {
        return group;
    }

    public void setGroup(GroupType group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", link='" + link + '\'' +
                ", group=" + group +
                '}';
    }
}
