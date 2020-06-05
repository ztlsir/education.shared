package com.ztlsir.shared.validation.Impl;

import org.springframework.web.multipart.MultipartFile;
import com.ztlsir.shared.validation.ImageCheck;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;

public class ImageCheckImpl implements ConstraintValidator<ImageCheck, MultipartFile> {

    public void initialize(ImageCheck arg0) {
    }

    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext arg1) {
        return isValid(multipartFile);
    }

    public boolean isValid(MultipartFile multipartFile) {
        try {
            if (multipartFile == null || multipartFile.isEmpty()) {
                return false;
            }

            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream()); // 通过临时文件获取图片流
            if (bufferedImage == null) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
