package com.ch.dcs.cachedb.server;

/**
 * FormSql
 */
public class FormSql {

    private String sql;
    private DataUnit[] params;


    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public DataUnit[] getParams() {
        return this.params;
    }

    public void setParams(DataUnit[] params) {
        this.params = params;
    }

}