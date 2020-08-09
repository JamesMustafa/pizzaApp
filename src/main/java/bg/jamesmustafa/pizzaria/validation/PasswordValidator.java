package bg.jamesmustafa.pizzaria.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidatorImpl.class)
public @interface PasswordValidator {

    String message() default "Passwords do not match";
    String pass();
    String confPass();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
