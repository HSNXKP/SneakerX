package top.naccl.util.mail;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.naccl.config.properties.RabbitMQConstant;
import top.naccl.constant.RedisKeyConstants;
import top.naccl.service.RedisService;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Random;


/**
 * @author: wdd
 * @date: 2023/5/18 18:18
 */
@Component
public class CodeMAil {
    // 打印日志
    private static final Logger LOGGER= LoggerFactory.getLogger(CodeMAil.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private TemplateEngine templateEngine;


    @Autowired
    private RedisService redisService;

    @RabbitListener(queues = RabbitMQConstant.CODE_QUEUE_NAME)
    public void handler(Message message, Channel channel){
        // 拿出邮箱
        String email = (String) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        // 消息号序列
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        // 拿出codeId 就是email
        String data = (String) headers.get("spring_returned_message_correlation");
        String[] split = data.split(RabbitMQConstant.DOT);
        try {
            if (redisService.getValueByHashKey(RedisKeyConstants.CODE_MSG_ID_MAP,email)!= null){
                LOGGER.error("消息已经被消费======={}",split[1]);
                return;
            }
            // 创建消息
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            // 发件人
            helper.setFrom(mailProperties.getUsername());
            // 收件人
            helper.setTo(email);
            // 主题
            helper.setSubject("SneakerX注册邮件");
            helper.setSentDate(new Date());
            // 邮件内容
            // 生成5位数验证码
            String code= String.valueOf((int)(new Random().nextDouble() * (99999 - 10000 + 1)) + 10000);
            Context context = new Context();
            context.setVariable("email",email);
            context.setVariable("code",code);
            String mail = templateEngine.process("code", context);
            helper.setText(mail,true);
            // 发送邮件
            javaMailSender.send(msg);
            LOGGER.info("验证码邮件发送成功");
            // 更新到redis中
            redisService.saveKVToHash(RedisKeyConstants.CODE_MSG_ID_MAP,email,code);
            channel.basicAck(tag,false);
        } catch (MessagingException | IOException e) {
            try {
                // 将消息重新放入队列
                channel.basicNack(tag,false,true);
            } catch (IOException ex) {
                LOGGER.error("邮件发送失败");
            }
            LOGGER.error("邮件发送失败");
        }


    }

}
