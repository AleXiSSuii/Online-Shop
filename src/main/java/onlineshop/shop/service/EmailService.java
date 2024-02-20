package onlineshop.shop.service;

import onlineshop.shop.model.Order;
import onlineshop.shop.model.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendOrderConfirmation(String recipientEmail, Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Подтверждение заказа");
        message.setText("Заказ #" + order.getId() + " успешно размещен.");
        javaMailSender.send(message);
    }
    public void sendSuccessRegistration(User user){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Вы успешно зарегестрированы на website");
        message.setText("Добро пожаловать, " + user.getName());
        javaMailSender.send(message);
    }
    public void send(String recipientEmail,String subject,String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }

}
