package bg.jamesmustafa.pizzaria.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidatorImpl implements ConstraintValidator<PasswordValidator, Object> {

    private String password;
    private String confirmPassword;
    private String message;


    @Override
    public void initialize(PasswordValidator constraintAnnotation) {
        this.password = constraintAnnotation.pass();
        this.confirmPassword = constraintAnnotation.confPass();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);
        Object pass = wrapper.getPropertyValue(password);
        Object confPass = wrapper.getPropertyValue(confirmPassword);

        boolean isValid =
                (pass == null && confPass == null) ||
                        (pass != null && pass.equals(confPass));

        if (!isValid) {
            context.
                    buildConstraintViolationWithTemplate(message).
                    addPropertyNode(password).
                    addConstraintViolation().
                    buildConstraintViolationWithTemplate(message).
                    addPropertyNode(confirmPassword).
                    addConstraintViolation().
                    disableDefaultConstraintViolation();
        }

        return isValid;
    }
}
