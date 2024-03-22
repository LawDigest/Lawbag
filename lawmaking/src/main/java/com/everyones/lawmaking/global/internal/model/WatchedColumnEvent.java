package com.everyones.lawmaking.global.internal.model;

import lombok.Data;

import java.util.List;


@Data
public class WatchedColumnEvent {
    public enum ColumnEventType {
        INSERT, UPDATE, DELETE
    }
    private final String eventName;
    private final String tableName;
    private final String columnName;
    private final ColumnEventType eventType;
    private final List<String> resultColumnNames;
}