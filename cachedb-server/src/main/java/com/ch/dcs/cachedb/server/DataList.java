package com.ch.dcs.cachedb.server;

/**
 * DataList
 */
public class DataList {

    private MetaUnit[] meta;
    private String[] columnNames;
    private String[][] data;

    public MetaUnit[] getMeta() {
        return this.meta;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public void setMeta(MetaUnit[] meta) {
        this.meta = meta;
    }

    public String[][] getData() {
        return this.data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }

}