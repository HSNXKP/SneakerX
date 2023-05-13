package top.naccl.config;

import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.naccl.config.properties.MailConstants;
import top.naccl.entity.MailLog;
import top.naccl.mapper.MailLogMapper;
import top.naccl.mapper.ScheduleJobMapper;
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


    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate= new RabbitTemplate(connectionFactory);
        /**
         * 消息确认回调，确认消息是否到达broker
         * data：消息唯一标识
         * ack：确认结果
         * cause：失败原因
         */
        rabbitTemplate.setConfirmCallback(((data, ack, cause) -> {
            String msgId =data.getId();
            if (ack){
                LOGGER.info("{}=====>消息发送成功",msgId);
                mailLogMapper.updateMailStatus(msgId,MailConstants.SUCCESS, LocalDateTime.now());
            }else {
                LOGGER.error("{}=====>消息发送失败",msgId);
                // 如果定时任务没有开启，开启定时任务
                if (!scheduleJobMapper.getJobById(3L).getStatus()){
                    // 运行定时任务
                    scheduleJobService.updateJobStatusById(3L,true);
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
        rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingKey)->{
            LOGGER.error("{}=====>消息发送失败",msg.getBody());
            // 如果定时任务没有开启，开启定时任务
            if (!scheduleJobMapper.getJobById(3L).getStatus()){
                // 运行定时任务
                scheduleJobService.updateJobStatusById(3L,true);
            }
        });

        return rabbitTemplate;

    }

    /**
     * 队列
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

    /**
     * directExchange
     * direct类型交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    /**
     * 绑定交换机 路由指定的队列
     * @return
     */
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }

}
