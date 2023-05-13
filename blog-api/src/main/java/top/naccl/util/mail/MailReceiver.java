package top.naccl.util.mail;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.naccl.config.properties.MailConstants;
import top.naccl.constant.RedisKeyConstants;
import top.naccl.entity.User;
import top.naccl.service.RedisService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * @author: wdd 邮件发送
 * @date: 2023/5/13 17:36
 */
@Component
public class MailReceiver {

    // 打印日志
    private static final Logger LOGGER= LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate jsonRedisTemplate;

    @Autowired
    private RedisService redisService;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel){
        // 拿出用户
        User user = (User) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        // 消息号序列
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        // 拿出msgId
        String msgId = (String) headers.get("spring_returned_message_correlation");
        try {
            if (redisService.getValueByHashKey(RedisKeyConstants.MAIL_MSG_ID_MAP,msgId)!=null){
                LOGGER.error("消息已经被消费======={}",msgId);
                /**
                 * tag：消息序号
                 */
                channel.basicAck(tag,false);
                return;
            }
            // 创建消息
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            // 发件人
            helper.setFrom(mailProperties.getUsername());
            // 收件人
            helper.setTo(user.getEmail());
            // 主题
            helper.setSubject("SneakerX邮件");
            helper.setSentDate(new Date());
            // 邮件内容
            Context context = new Context();
            context.setVariable("nickname",user.getNickname());
            context.setVariable("createTime",user.getCreateTime().format(ofPattern("yyyy-MM-dd HH:mm:ss")));
            String mail = templateEngine.process("mail", context);
            helper.setText(mail,true);
            // 发送邮件
            javaMailSender.send(msg);
            LOGGER.info("邮件发送成功");
            // 更新到redis中
            redisService.saveKVToHash(RedisKeyConstants.MAIL_MSG_ID_MAP,msgId, user.getId() + ":" + "成功发送邮件，用户名：" +user.getUsername() + "的邮箱" + "：" + user.getEmail());
            channel.basicAck(tag,false);
        } catch (MessagingException | IOException e) {
            try {
                channel.basicNack(tag,false,true);
            } catch (IOException ex) {
                LOGGER.error("邮件发送失败");
            }
            LOGGER.error("邮件发送失败");
        }


    }
}
