package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.naccl.entity.MailLog;

import java.util.List;

@Mapper
public interface MailLogMapper {


    int insertMailLog(MailLog mailLog);

    int updateMailStatus(@Param("msgId") String msgId, @Param("status") Integer status, @Param("updateTime") java.time.LocalDateTime updateTime);


    List<MailLog> getStatusFailList(@Param("status") Integer status);

    int updateMail(MailLog mailLog);

    MailLog getMailById(String s);

}
