package bg.jamesmustafa.pizzaria.event;

import bg.jamesmustafa.pizzaria.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ApprovedOrderListener {
    private final EmailService emailService;

    public ApprovedOrderListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener(ApprovedOrderEvent.class)
    public void onEvent(ApprovedOrderEvent orderEvent) {

        var link = '"' + "http://localhost:8000/orders/history/" + orderEvent.getOrderId() + '"';
        var orderLink = String.format("<html><body><a href=%s>FROM HERE</a></body></html>",link);

        if(orderEvent.getArrivingOn() != null && !orderEvent.getPrice().isEmpty()){
            var message = "Dear Customer,</br></br>"
                    + "Your order has been placed and we are now working on it." + "</br></br>" +  "The arriving time of the delivery is expected to be around "
                    + orderEvent.getArrivingOn() + ".</br>" + "Total price of the order: " + orderEvent.getPrice() + ". </br></br>"
                    + "You can as well see your order in your order history or: " + orderLink + ". </br></br>"
                    + "<b>We wish your day to be as nice as your meal! :)</b>";

            this.emailService.sendMail(orderEvent.getTo(),"Your order has been confirmed.", message);
        }
        else {
            var message = "Dear Customer,</br></br>"
                    + "Your order has not been placed and we are sorry for that.</br></br>"
                    + "You can still see you order " + orderLink + " and maybe try again another time</br></br>"
                    + "<b>Wish you a happy and joyful day!</b>";

            this.emailService.sendMail(orderEvent.getTo(),"Your order has been declined.", message);
        }
    }
}
