package com.ztlsir.shared.value;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Value
public class ClassAndGrade {
    private int grade;
    private int classes;
}
