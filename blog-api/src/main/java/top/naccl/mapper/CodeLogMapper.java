package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.naccl.entity.CodeLog;
import top.naccl.entity.MailLog;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CodeLogMapper {
    int insertCodeLog(CodeLog codeLog);

    List<CodeLog> getStatusFailList(@Param("status") Integer status);

    int updateCodeStatus(@Param("msgId") String msgId, @Param("status") Integer status, @Param("updateTime") java.time.LocalDateTime updateTime);

    int updateCode(CodeLog codeLog);
}
