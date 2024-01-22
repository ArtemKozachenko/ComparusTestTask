package com.my.comparustesttask.postprocessor;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DataSourceProperties {
    private String name;
    private String url;
    private String table;
    private String user;
    private String password;
    private ColumnsMapping mapping;

    @AllArgsConstructor
    @Data
    public static class ColumnsMapping {
        private String id;
        private String username;
        private String name;
        private String surname;
    }
}
