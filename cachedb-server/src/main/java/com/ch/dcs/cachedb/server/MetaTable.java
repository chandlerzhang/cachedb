package com.ch.dcs.cachedb.server;

/**
 * MetaTable
 */
public class MetaTable {

    private String name;
    private String[] columnNames;
    private MetaUnit[] meta;


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getColumnNames() {
        return this.columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }


    public MetaUnit[] getMeta() {
        return this.meta;
    }

    public void setMeta(MetaUnit[] meta) {
        this.meta = meta;
    }



}