package bg.jamesmustafa.pizzaria.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EmailEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EmailEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(String to, String subject, String text) {
        EmailEvent emailEvent = new EmailEvent(this, to, subject, text);
        this.applicationEventPublisher.publishEvent(emailEvent);
    }
}
