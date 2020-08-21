package bg.jamesmustafa.pizzaria.event;

import bg.jamesmustafa.pizzaria.db.entity.User;
import bg.jamesmustafa.pizzaria.service.ConfirmationTokenService;
import bg.jamesmustafa.pizzaria.service.EmailService;
import bg.jamesmustafa.pizzaria.service.UserDetailsServiceImpl;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ConfirmEmailListener {
    private final ConfirmationTokenService tokenService;
    private final EmailService emailService;

    public ConfirmEmailListener(ConfirmationTokenService tokenService, EmailService emailService) {
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @Async
    @EventListener(ConfirmEmailEvent.class)
    public void onEvent(ConfirmEmailEvent emailEvent){
        this.emailConfirm(emailEvent);
        //notification- confirmation email has been send to your email account!
    }

    public void emailConfirm(ConfirmEmailEvent emailEvent){
        User user = emailEvent.getUser();
        String token = UUID.randomUUID().toString();
        this.tokenService.createToken(token, user);

        var link = '"' + "http://localhost:8000/authentication/emailConfirm/" + token + '"';
        var confirmLink = String.format("<html><body><a href=%s>FROM HERE</a></body></html>",link);
        var message = "Dear Customer,</br></br>"
                + "In order to gain access to checkout orders, we first need to make sure that your email account is real and it belongs to you." + "</br></br>"
                + "So please confirm your email account from the link below !" + "</br></br>"
                + "Confirm email now: "+confirmLink+" </br></br>"
                + "<b>We wish your day to be as nice as our pizza! :)</b>";

        this.emailService.sendMail(user.getEmail(),"Confirm your email address for Pizza La Jem", message);
    }
}
