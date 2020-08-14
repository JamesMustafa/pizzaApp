package bg.jamesmustafa.pizzaria.event;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class ApprovedOrderEvent extends ApplicationEvent {
    private String to;
    private String orderId;
    private String arrivingOn;
    private String price;

    public ApprovedOrderEvent(Object source, String to, String orderId) {
        super(source);
        this.to = to;
        this.orderId = orderId;
    }

    public ApprovedOrderEvent(Object source, String to, String orderId, String arrivingOn, String price) {
        super(source);
        this.to = to;
        this.orderId = orderId;
        this.arrivingOn = arrivingOn;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public String getTo() {
        return to;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getArrivingOn() {
        return arrivingOn;
    }
}
