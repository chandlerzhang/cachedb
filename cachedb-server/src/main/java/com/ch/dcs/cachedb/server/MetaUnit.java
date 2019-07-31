package com.ch.dcs.cachedb.server;

/**
 * MetaColumn
 */
public class MetaUnit {

    /**
     * null 默认序列化字符串
     */
    public static final String DEFAULT_NULL_STRING = "(NULL)";

    /**
     * 以下类型之一 string, long, double, boolean, date
     */
    private String type;
    /**
     * 小数位，对应double类型
     */
    private Integer scale;
    /**
     * 日期格式，对应date类型, 使用java日期格式
     */
    private String format;
    /**
     * null 序列化字符串
     */
    private String nullString;

    /**
     * 序列化成字符串
     */
    public String dataToString(Object data) {
        // TODO
        return null;
    }

    /**
     * 反序列化成数据
     */
    public <T> T stringToData(String text) {
        // TODO
        return null;
    }

    public String getType() {
        return this.type;
    }

    public String getNullString() {
        return nullString;
    }

    public void setNullString(String nullString) {
        this.nullString = nullString;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getScale() {
        return this.scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}