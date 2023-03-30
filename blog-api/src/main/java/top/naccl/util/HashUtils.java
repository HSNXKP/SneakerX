package top.naccl.util;

import lombok.val;
import org.apache.commons.codec.digest.MurmurHash3;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * @Description: Hash工具类
 * @Author: wdd
 * @Date: 2020-11-17
 */
public class HashUtils {
	private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	/**
	 * Md5加密
	 * @param str
	 * @return
	 */
	public static String getMd5(CharSequence str) {
		return DigestUtils.md5DigestAsHex(str.toString().getBytes());
	}

	public static long getMurmurHash32(String str) {
		int i = MurmurHash3.hash32(str);
		long num = i < 0 ? Integer.MAX_VALUE - (long) i : i;
		return num;
	}

	public static String getBC(CharSequence rawPassword) {
		return bCryptPasswordEncoder.encode(rawPassword);
	}

	/**
	 * 密码校验
	 * @param rawPassword
	 * @param encodedPassword
	 * @return
	 */
	public static boolean matchBC(CharSequence rawPassword, String encodedPassword) {
		// 前面为前端传入的不加密 密码 ,后面这个是数据库的加密密码
		return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);

	}

	public static void main(String[] args) {
		boolean b = matchBC("123456", "$2a$10$4wnwMW8Z4Bn6wR4K1YlbquQunlHM/4rvudVBX8oyfx16xeVtI6i7C");
		System.out.printf(String.valueOf(b));

	}
}
