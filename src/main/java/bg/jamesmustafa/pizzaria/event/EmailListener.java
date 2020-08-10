package bg.jamesmustafa.pizzaria.event;

import bg.jamesmustafa.pizzaria.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {
    private EmailService emailService;

    public EmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener(EmailEvent.class)
    public void onEvent(EmailEvent emailEvent) {
        emailService.sendMail(emailEvent.getTo(), emailEvent.getSubject(), emailEvent.getText());
    }
}
