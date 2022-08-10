package com.zkyt.enums.File;

public enum EnumFileType {
    RAR(".rar", 0),
    ZIP(".zip", 1),
    XML(".xml", 2),
    YML(".yml", 3),
    JAVA(".java", 4),
    TXT(".txt", 5);
    private String value;
    private int code;

    EnumFileType(String value) {
        this.value = value;
    }

    EnumFileType(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private EnumFileType(String value, int code) {
        this.value = value;
        this.code = code;
    }

    @Override
    public String toString() {
        return "EnumFileType{" +
                "value='" + value + '\'' +
                ", code=" + code +
                '}';
    }
}
 
