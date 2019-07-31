package com.ch.dcs.cachedb.server;

/**
 * FormSql
 */
public class FormScript {

    private String scriptName;
    private String script;
    private DataUnit[] params;


    public String getScriptName() {
        return this.scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getScript() {
        return this.script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public DataUnit[] getParams() {
        return this.params;
    }

    public void setParams(DataUnit[] params) {
        this.params = params;
    }


}