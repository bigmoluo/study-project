package com.example.studyprojectbacked.entity.dto;

import com.example.studyprojectbacked.entity.BaseData;
import com.example.studyprojectbacked.entity.RestBeen;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

@Data
public class AccountPrivacy implements BaseData {
    final int id;
    boolean phone = true;
    boolean email = true;
    boolean qq = true;
    boolean wx = true;
    boolean gender = true;

    public String[] hiddenFields() {
        List<String> strings = new LinkedList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getType().equals(boolean.class) && !field.getBoolean(this))
                    strings.add(field.getName());
            } catch (Exception ignored) {

            }
        }
        return strings.toArray(String[]::new);
    }
}
