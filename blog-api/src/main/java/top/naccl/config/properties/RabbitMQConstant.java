package top.naccl.config.properties;

import org.springframework.stereotype.Component;

/**
 * @author: wdd
 * @date: 2023/5/19 0:25
 */
@Component
public class RabbitMQConstant {

    public static final String DOT = ",";

    public static final String MAIL_QUEUE_NAME = "mail.queue";
    public static final class MailConstant {
        // 消息投递中
        public static final Integer DELIVERING = 0;
        // 消息投递成功
        public static final Integer SUCCESS = 1;
        // 消息投递失败
        public static final Integer FAILURE = 2;
        // 最大重试次数
        public static final Integer MAX_TRY_COUNT = 3;
        // 消息超时时间
        public static final Integer MSG_TIMEOUT = 1;
        // 队列
        public static final String MAIL_QUEUE_NAME = "mail.queue";
        // 交换机
        public static final String MAIL_EXCHANGE_NAME = "mail.exchange";
        // 路由
        public static final String MAIL_ROUTING_KEY_NAME = "mail.routing.key";
        // 唯一标识符前戳
        public static final String MAIL_ID_PREFIX = "mail";
    }

    public static final String CODE_QUEUE_NAME = "code.queue";
    public static final class CodeConstant {
        // 消息投递中
        public static final Integer DELIVERING = 0;
        // 消息投递成功
        public static final Integer SUCCESS = 1;
        // 消息投递失败
        public static final Integer FAILURE = 2;
        // 最大重试次数
        public static final Integer MAX_TRY_COUNT = 3;
        // 消息超时时间
        public static final Integer MSG_TIMEOUT = 1;
        // 验证码队列
        public static final String CODE_QUEUE_NAME = "code.queue";
        // 验证码交换机
        public static final String CODE_EXCHANGE_NAME = "code.exchange";
        // 验证码路由
        public static final String CODE_ROUTING_KEY_NAME = "code.routing.key";
        // 唯一标识符前戳
        public static final String CODE_ID_PREFIX = "code";
    }
}
