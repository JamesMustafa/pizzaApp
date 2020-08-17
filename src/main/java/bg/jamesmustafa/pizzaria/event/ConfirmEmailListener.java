package bg.jamesmustafa.pizzaria.event;

import bg.jamesmustafa.pizzaria.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class ConfirmEmailListener {
    private final EmailService emailService;

    public ConfirmEmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener(ConfirmEmailEvent.class)
    public void onEvent(){

    }
}
