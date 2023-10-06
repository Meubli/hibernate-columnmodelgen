package com.meubli.model;

import java.util.Map;

public class MetaEntityBuilder {
    private String classSimpleName;
    private String packageName;
    private String table;
    private Map<String, String> mapFieldColumn;

    public MetaEntityBuilder setClassSimpleName(String classSimpleName) {
        this.classSimpleName = classSimpleName;
        return this;
    }

    public MetaEntityBuilder setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public MetaEntityBuilder setTable(String table) {
        this.table = table;
        return this;
    }

    public MetaEntityBuilder setMapFieldColumn(Map<String, String> mapFieldColumn) {
        this.mapFieldColumn = mapFieldColumn;
        return this;
    }

    public MetaEntity createMetaEntity() {
        return new MetaEntity(classSimpleName, packageName, table, mapFieldColumn);
    }
}