package com.radar.util;

import com.radar.RadarHeadfile;

import java.util.List;

public class FileDom {
    protected String name;
    protected List<RadarHeadfile> innerFiles;
    protected List<FileDom> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RadarHeadfile> getInnerFiles() {
        return innerFiles;
    }

    public void setInnerFiles(List<RadarHeadfile> innerFiles) {
        this.innerFiles = innerFiles;
    }

    public List<FileDom> getChildren() {
        return children;
    }

    public void setChildren(List<FileDom> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "FileDom{" +
                "name='" + name + '\'' +
                ", innerFiles=" + innerFiles +
                ", children=" + children +
                '}';
    }
}
