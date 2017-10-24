package ped.bstu.by.contacts.Validation;

import android.text.TextUtils;

/**
 * Created by Egor on 11.10.2017.
 */

public class EmptyValidator implements Validator<String> {

    @Override
    public boolean isValid(String value) {
        return !TextUtils.isEmpty(value);
    }

    @Override
    public String getDescription() {
        return "Поле не должно быть пустым!";
    }

}
