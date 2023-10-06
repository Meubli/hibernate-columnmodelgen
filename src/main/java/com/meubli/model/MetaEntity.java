package com.meubli.model;

import java.util.Map;

public class MetaEntity {

    private String classSimpleName;

    private String packageName;

    private String table;

    private Map<String, String> mapFieldColumn;

    MetaEntity(String classSimpleName, String packageName, String table, Map<String, String> mapFieldColumn) {
        this.classSimpleName = classSimpleName;
        this.packageName = packageName;
        this.table = table;
        this.mapFieldColumn = mapFieldColumn;
    }

    public String getTable() {
        return table;
    }

    public Map<String, String> getMapFieldColumn() {
        return mapFieldColumn;
    }


    public String getClassSimpleName() {
        return classSimpleName;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public String toString() {
        return "MetaEntity{" +
                "classSimpleName='" + classSimpleName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", table='" + table + '\'' +
                ", mapFieldColumn=" + mapFieldColumn +
                '}';
    }
}
