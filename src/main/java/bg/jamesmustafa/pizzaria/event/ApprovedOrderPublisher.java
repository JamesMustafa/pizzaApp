package bg.jamesmustafa.pizzaria.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ApprovedOrderPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public ApprovedOrderPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishDecline(String to, String orderId) {
        ApprovedOrderEvent approvedEvent = new ApprovedOrderEvent(this, to, orderId);
        
        this.applicationEventPublisher.publishEvent(approvedEvent);
    }

    public void publishSuccess(String to, String orderId, String arrivingOn, String price) {
        ApprovedOrderEvent approvedEvent = new ApprovedOrderEvent(this, to, orderId, arrivingOn, price);
        this.applicationEventPublisher.publishEvent(approvedEvent);
    }
}
