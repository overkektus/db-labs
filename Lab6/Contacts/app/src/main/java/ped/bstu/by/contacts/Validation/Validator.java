package ped.bstu.by.contacts.Validation;

/**
 * Created by Egor on 11.10.2017.
 */

public interface Validator<T> {
    boolean isValid(T value);

    String getDescription();
}
