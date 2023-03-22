package top.naccl.model.vo;

import lombok.*;

/**
 * @Author wdd
 * @Date 2023/3/22 10:13
 * @PackageName:top.naccl.model.vo
 * @ClassName: NewPasswordVo
 * @Version 1.0
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewPasswordVo {
    private Long id;
    private String oldPassword;
    private String pass;
    private String newPassword;
}
