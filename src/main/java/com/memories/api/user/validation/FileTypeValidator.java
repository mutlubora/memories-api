package com.memories.api.user.validation;

import com.memories.api.file.FileService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FileTypeValidator implements ConstraintValidator<FileType, String> {
    private final FileService fileService;

    private String[] types;
    public FileTypeValidator(FileService fileService) {
        this.fileService = fileService;
    }
    @Override
    public void initialize(FileType constraintAnnotation) {
        this.types = constraintAnnotation.types();
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        String type = fileService.detectType(value);

        for (String validType : types) {
            if (type.contains(validType)) {
                return true;
            }
        }

        String validTypes = String.join(", ", types);
        context.disableDefaultConstraintViolation();
        var hibernateConstraintValidatorContext = context
                .unwrap(HibernateConstraintValidatorContext.class);
        hibernateConstraintValidatorContext.addMessageParameter("types", validTypes);
        hibernateConstraintValidatorContext
                .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addConstraintViolation();
        return false;
    }


}
