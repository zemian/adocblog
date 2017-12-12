package com.zemian.adocblog.data.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A entity domain to represents the "settings" table data.
 */
public class Setting {
    public static enum Type {
        STRING, BOOLEAN, INTEGER, DOUBLE, LIST;
    }
    private Integer settingId;
    private String category;
    private String name;
    private String value;
    private Type type = Type.STRING;
    private String description;

    public Setting() {
    }

    public Setting(String category, String name, String value) {
        this.category = category;
        this.name = name;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValueByType() {
        if (type == Type.BOOLEAN) {
            return (T)Boolean.valueOf(value);
        } else if (type == Type.INTEGER) {
            return (T)Integer.valueOf(value);
        } else if (type == Type.DOUBLE) {
            return (T)Double.valueOf(value);
        } else if (type == Type.LIST) {
            return (T)Arrays.asList(value.split(","));
        } else {
            return (T)value;
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "id=" + settingId +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
