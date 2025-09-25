package edu.ccrm.domain;
import java.util.Objects;

/**
 * Immutable value object for CourseCode.
 */
public final class CourseCode {
    private final String code;

    public CourseCode(String code) {
        this.code = Objects.requireNonNull(code).trim().toUpperCase();
    }

    public String getCode() { return code; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseCode)) return false;
        CourseCode that = (CourseCode) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() { return code.hashCode(); }

    @Override
    public String toString() { return code;}
}
