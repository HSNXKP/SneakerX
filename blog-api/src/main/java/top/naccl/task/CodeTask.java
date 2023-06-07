package top.naccl.task;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.naccl.config.properties.RabbitMQConstant;
import top.naccl.entity.CodeLog;
import top.naccl.entity.MailLog;
import top.naccl.entity.User;
import top.naccl.mapper.CodeLogMapper;
import top.naccl.mapper.MailLogMapper;
import top.naccl.mapper.ScheduleJobMapper;
import top.naccl.service.ScheduleJobService;
import top.naccl.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: wdd
 * @date: 2023/5/19 15:07
 */
@Component
public class CodeTask {

    @Autowired
    CodeLogMapper codeLogMapper;

    @Autowired
    UserService userService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ScheduleJobService scheduleJobService;

    @Autowired
    ScheduleJobMapper scheduleJobMapper;


    public void syncCodeTaskToDatabase(){
        // 查询状态处于投递中的数据 status=0   因为当有一个用户注册的时候，发送邮件失败时，会开启定时任务，所以就不能设置时间的限制，可能会遗漏用户
        List<CodeLog> list = codeLogMapper.getStatusFailList(RabbitMQConstant.CodeConstant.DELIVERING);
        list.forEach(codeLog -> {
            // 如果重试次数超过最大重复次数，更新投递状态为投递失败，不再重试
            if (RabbitMQConstant.CodeConstant.MAX_TRY_COUNT < codeLog.getCount()){
                codeLogMapper.updateCodeStatus(codeLog.getMsgId(),RabbitMQConstant.CodeConstant.FAILURE, LocalDateTime.now());
            }
            codeLog.setCount(codeLog.getCount() + 1);
            codeLog.setUpdateTime(LocalDateTime.now());
            codeLog.setTryTime(LocalDateTime.now().plusMinutes(RabbitMQConstant.CodeConstant.MSG_TIMEOUT));
            codeLogMapper.updateCode(codeLog);
            // 重新发送消息
            rabbitTemplate.convertAndSend(RabbitMQConstant.CodeConstant.CODE_EXCHANGE_NAME,RabbitMQConstant.CodeConstant.CODE_ROUTING_KEY_NAME,codeLog.getEmail(),new CorrelationData(RabbitMQConstant.CodeConstant.CODE_ID_PREFIX + RabbitMQConstant.DOT + codeLog.getMsgId()));
        });
        if (list.size() == 0){
            if (scheduleJobMapper.getJobById(6L).getStatus()){
                // 停止定时任务
                scheduleJobService.updateJobStatusById(6L,false);
            }
        }
    }
}
