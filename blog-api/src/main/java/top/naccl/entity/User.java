package top.naccl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户实体类
 * @Author: wdd
 * @Date: 2020-07-19
 */

@NoArgsConstructor
@Getter
@Setter
@ToString
public class User implements UserDetails {
	private Long id;
	private String username; //用户名
	private String password; //密码
	private String nickname; //昵称
	private String avatar; //头像
	private String email; //邮箱
	private String code; //邮箱验证码
	private String userFlag;// 登陆后的前戳
	private String flagColor;//前戳的颜色
	private String userSign;//个人签名
	private Long fans;//粉丝数量
	private Long follow;//关注数量

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	private LocalDateTime createTime; //创建账号时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	private LocalDateTime updateTime; //更新动态时间
	private String role; //角色

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority(role));
		return authorityList;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
}
