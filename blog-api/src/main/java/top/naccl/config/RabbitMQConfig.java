package top.naccl.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import top.naccl.config.properties.RabbitMQConstant;
import top.naccl.constant.RedisKeyConstants;
import top.naccl.entity.User;
import top.naccl.mapper.CodeLogMapper;
import top.naccl.mapper.MailLogMapper;
import top.naccl.mapper.ScheduleJobMapper;
import top.naccl.service.RedisService;
import top.naccl.service.ScheduleJobService;

import java.time.LocalDateTime;

/**
 * @author: wdd
 * @date: 2023/5/13 18:33
 */
@Configuration
public class RabbitMQConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitTemplate.class);
    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private MailLogMapper mailLogMapper;

    @Autowired
    ScheduleJobService scheduleJobService;

    @Autowired
    ScheduleJobMapper scheduleJobMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    CodeLogMapper codeLogMapper;


    @Bean
    public RabbitTemplate rabbitTemplate() {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        /**
         * 消息确认回调，确认消息是否到达broker
         * data：消息唯一标识
         * ack：确认结果
         * cause：失败原因
         */
        rabbitTemplate.setConfirmCallback(((data, ack, cause) -> {
            String id = data.getId();
            String[] split = id.split(RabbitMQConstant.DOT);
            // 如果是验证码消息发送的话
            if(split[0].equals(RabbitMQConstant.CodeConstant.CODE_ID_PREFIX)){
                if (ack) {
                    LOGGER.info("{}=====>消息发送成功", split[1]);
                    codeLogMapper.updateCodeStatus(split[1], RabbitMQConstant.CodeConstant.SUCCESS, LocalDateTime.now());
                } else {
                    LOGGER.info("{}=====>消息发送失败", split[1]);
                    // 如果定时任务没有开启，开启定时任务
                    if (!scheduleJobMapper.getJobById(6L).getStatus()) {
                        // 运行定时任务
                        scheduleJobService.updateJobStatusById(6L, true);
                    }
                }
            }else if (split[0].equals(RabbitMQConstant.MailConstant.MAIL_ID_PREFIX)){
                if (ack) {
                        LOGGER.info("{}=====>消息发送成功", split[1]);
                        mailLogMapper.updateMailStatus(split[1], RabbitMQConstant.MailConstant.SUCCESS, LocalDateTime.now());
                        // 发送失败
                        // 不发送消息 重新消费
//                        User user = new User();
//                        rabbitTemplate.convertAndSend(
//                                RabbitMQConstant.MailConstant.MAIL_EXCHANGE_NAME,
//                                RabbitMQConstant.MailConstant.MAIL_ROUTING_KEY_NAME,
//                                user,
//                                new CorrelationData(RabbitMQConstant.MailConstant.MAIL_ID_PREFIX + RabbitMQConstant.DOT + split[1])
//                        );
                } else {
                    LOGGER.error("{}=====>消息发送失败", split[1]);
                    // 如果定时任务没有开启，开启定时任务
                    if (!scheduleJobMapper.getJobById(3L).getStatus()) {
                        // 运行定时任务
                        scheduleJobService.updateJobStatusById(3L, true);
                    }
                }
            }
        }));

        /**
         * 消息失败回调
         * msg:消息主题
         * repCode:状态码
         * repText:相应描述
         * exchange:交换机
         * routingKey:路由键
         */
        rabbitTemplate.setReturnCallback((msg, repCode, repText, exchange, routingKey) -> {
            if(exchange.equals(RabbitMQConstant.CodeConstant.CODE_EXCHANGE_NAME)){
                LOGGER.error("{}=====>消息发送失败", msg.getBody());
                // 如果定时任务没有开启，开启定时任务
                if (!scheduleJobMapper.getJobById(6L).getStatus()) {
                    // 运行定时任务
                    scheduleJobService.updateJobStatusById(6L, true);
                }
            }else{
                LOGGER.error("{}=====>消息发送失败", msg.getBody());
                // 如果定时任务没有开启，开启定时任务
                if (!scheduleJobMapper.getJobById(3L).getStatus()) {
                    // 运行定时任务
                    scheduleJobService.updateJobStatusById(3L, true);
                }
            }
        });

        return rabbitTemplate;

    }

    /**
     * 队列
     *
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(RabbitMQConstant.MailConstant.MAIL_QUEUE_NAME);
    }

    /**
     * directExchange
     * direct类型交换机
     *
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(RabbitMQConstant.MailConstant.MAIL_EXCHANGE_NAME);
    }

    /**
     * 绑定交换机 路由指定的队列
     *
     * @return
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(RabbitMQConstant.MailConstant.MAIL_ROUTING_KEY_NAME);
    }


    /**
     * 验证码队列
     *
     * @return
     */
    @Bean
    public Queue codeQueue() {
        return new Queue(RabbitMQConstant.CodeConstant.CODE_QUEUE_NAME);
    }


    /**
     * directExchange
     * direct类型交换机
     *
     * @return
     */
    @Bean
    public DirectExchange directCodeExchange() {
        return new DirectExchange(RabbitMQConstant.CodeConstant.CODE_EXCHANGE_NAME);
    }

    /**
     * 绑定交换机 路由指定的队列
     *
     * @return
     */
    @Bean
    public Binding codeBinding() {
        return BindingBuilder.bind(codeQueue()).to(directCodeExchange()).with(RabbitMQConstant.CodeConstant.CODE_ROUTING_KEY_NAME);
    }

}
