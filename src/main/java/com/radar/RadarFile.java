package com.radar;

public class RadarFile {
    private RadarHeadfile headfile;
    private int imgType;
    private Data4Json imgData;
    private String imgUrl;

    public RadarHeadfile getHeadfile() {
        return headfile;
    }

    public int getImgType() {
        return imgType;
    }

    public Data4Json getImgData() {
        return imgData;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setHeadfile(RadarHeadfile headfile) {
        this.headfile = headfile;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public void setImgData(Data4Json imgData) {
        this.imgData = imgData;
    }
}
