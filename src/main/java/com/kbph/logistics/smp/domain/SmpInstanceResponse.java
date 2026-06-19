package com.kbph.logistics.smp.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmpInstanceResponse {
	
    private List<ApiData> data;

    @Getter
    @Setter
    public static class ApiData {
        private String instance;
        private String prefix;
        private List<Schema> schemas;

        @Getter
        @Setter
        public static class Schema {
            private String oldSchema;
            private String newSchema;
        }
    }
	
}
