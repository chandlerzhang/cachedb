package com.ch.dcs.cachedb.server;

/**
 * DataList
 */
public class DataUnit {

    private MetaUnit meta;
    private String data;

    public MetaUnit getMeta() {
        return this.meta;
    }

    public void setMeta(MetaUnit meta) {
        this.meta = meta;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

}