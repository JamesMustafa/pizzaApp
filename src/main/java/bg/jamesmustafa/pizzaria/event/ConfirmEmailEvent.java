package bg.jamesmustafa.pizzaria.event;

import org.springframework.context.ApplicationEvent;

public class ConfirmEmailEvent extends ApplicationEvent {
    private final String to;

    public ConfirmEmailEvent(Object source, String to) {
        super(source);
        this.to = to;
    }

    public String getTo() {
        return to;
    }
}
