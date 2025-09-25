package edu.ccrm.domain;
import java.time.LocalDate;

/**
 * Abstract Person class demonstrating abstraction and inheritance.
 */
public abstract class Person {
    protected final String id; // visible to subclasses
    protected String fullName;
    protected String email;
    protected LocalDate createdAt;

    public Person(String id, String fullName, String email) {
        assert id != null : "id must not be null";
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = LocalDate.now();
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }

    // abstract behavior each person must implement
    public abstract String role();

    @Override
    public String toString() {
        return String.format("%s[id=%s,name=%s,email=%s]", role(), id, fullName,email);
}
}
