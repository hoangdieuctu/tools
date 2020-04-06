package com.karrostech.tool.constant;

public enum GeneratorType {
    random_int,
    random_double,
    random_uuid,
    random_alphabetic,
    random_alphanumeric,
    current_millis,
    random_default;

    public static GeneratorType from(String type) {
        GeneratorType[] values = GeneratorType.values();
        for (GeneratorType value : values) {
            if (value.name().equals(type)) {
                return value;
            }
        }

        return random_default;
    }
}
