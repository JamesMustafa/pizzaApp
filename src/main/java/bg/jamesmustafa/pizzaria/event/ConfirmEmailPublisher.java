package bg.jamesmustafa.pizzaria.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ConfirmEmailPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public ConfirmEmailPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(String to){
        ConfirmEmailEvent confirmEmailEvent = new ConfirmEmailEvent(this,to);
        this.applicationEventPublisher.publishEvent(confirmEmailEvent);
    }
}
