package edu.ccrm.util;
import java.util.Objects;

public class Validators {
    public static void requireNonEmpty(String v, String name) {
        if (v == null || v.trim().isEmpty()) throw new IllegalArgumentException(name + " required");
    }
}
