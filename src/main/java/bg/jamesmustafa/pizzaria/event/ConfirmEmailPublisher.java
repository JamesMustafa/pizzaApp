package bg.jamesmustafa.pizzaria.event;

import bg.jamesmustafa.pizzaria.db.entity.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ConfirmEmailPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public ConfirmEmailPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(User user){
        ConfirmEmailEvent confirmEmailEvent = new ConfirmEmailEvent(this, user);
        this.applicationEventPublisher.publishEvent(confirmEmailEvent);
    }
}
