//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Component
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//
//@Component
// class EmailServiceImpl: EmailService {
//
//    @Autowired
//    lateinit var  emailSender; JavaEmailSender
//
//    fun  sendSimpleMessage(to:String,subject: String ,text:String) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        emailSender.send(message);
//
//    }
//}
