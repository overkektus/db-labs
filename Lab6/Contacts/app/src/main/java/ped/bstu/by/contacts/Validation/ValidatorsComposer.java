package ped.bstu.by.contacts.Validation;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Egor on 11.10.2017.
 */

public class ValidatorsComposer<T> implements Validator<T> {
    private final List<Validator<T>> validators;
    private String description;

    public ValidatorsComposer(Validator<T>... validators) {
        this.validators = Arrays.asList(validators);
    }

    @Override
    public boolean isValid(T value) {
        for(Validator<T> validator : validators) {
            if(!validator.isValid(value)) {
                description = validator.getDescription();
                return false;
            }
        }
        return true;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
