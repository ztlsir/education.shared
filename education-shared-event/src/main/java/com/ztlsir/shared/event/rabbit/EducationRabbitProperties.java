package com.ztlsir.shared.event.rabbit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties("education.rabbit")
@Validated
public class EducationRabbitProperties {
    @NotBlank
    private String publishX;

}
