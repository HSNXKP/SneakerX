package top.naccl;

import jdk.jfr.internal.Logger;
import net.minidev.json.JSONObject;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.naccl.entity.Blog;
import top.naccl.entity.Url;
import top.naccl.entity.User;
import top.naccl.mapper.BlogMapper;
import top.naccl.mapper.OrderMapper;
import top.naccl.mapper.UrlMapper;
import top.naccl.model.vo.BlogWithMomentView;
import top.naccl.model.vo.OrderAminVo;
import top.naccl.service.BlogService;
import top.naccl.service.UrlService;
import top.naccl.service.impl.UrlServiceImpl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
class BlogApiApplicationTests {

	@Autowired
	private BlogService blogService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	OrderMapper orderMapper;


	@Test
	void contextLoads() {
		List<OrderAminVo> allOrderList = orderMapper.getAllOrderList(null, null, null, 0L, -1L);
		allOrderList.forEach(System.out::println);
	}

	@Test
	void test() {
	// 2023-8765 之间的数可以被5或者7整除
		int x=2023;
		int n=0;
			for (x=2023;x<8765;x++){
				if (x%5 ==0 && x%7==0){
					n++;
				}
				System.out.println(n);
			}

			String s ="1";
		Integer s1 = Integer.valueOf(s);
		Integer s2 =11;
		String  s21 = String.valueOf(s2);
//		创建ArrayList
		ArrayList<Object> objects = new ArrayList<>();
//		创建数组
		List[] lists = new List[5];
//		创建LinkedList
		LinkedList<Object> objects1 = new LinkedList<>();

		HashMap<Object, Object> objectObjectHashMap = new HashMap<>();

		class s1{
			void test1(){
				System.out.println("测试1");
			}
		}

		class s2{
			void test2(){
				System.out.println("测试2");
			}
		}


	}

	@Test
	void test2(){
		// 拿到文件后戳
		String osName = System.getProperties().getProperty("os.name");
		if(osName.equals("Linux"))
		{
			System.out.println("running in Linux");
		}
		else
		{
			System.out.println("don't running in Linux");
		}

		String name = "asdas.ss";
		String substring = name.substring(name.lastIndexOf("."));
		System.out.printf(substring);
	}

	@Autowired
	UrlMapper urlMapper;

	@Autowired
	BlogMapper blogMapper;



	@Test
	void test3(){
		HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
		objectObjectHashMap.put("1",1);
		objectObjectHashMap.put("2",2);
		objectObjectHashMap.put("S3",3);
		System.out.println(objectObjectHashMap);
		objectObjectHashMap.forEach((k,v) ->{
			String s = "S";
			String s1 = "3";
			String s2 = "S3";
			String s3 = "S4";
			String s4 = "S5";
			List<Object> objects = new ArrayList<>();
			objects.add(s2);
			objects.add(s3);
			objects.add(s4);
			for (Object object : objects) {
				if (k.equals(object)){
					System.out.println("存在");
					objectObjectHashMap.put(k,1);
				}
			}

		});
		System.out.println(objectObjectHashMap);

		List<BlogWithMomentView> bolgListAnonymous = blogMapper.getBolgListAnonymous(1L);
		for (BlogWithMomentView listAnonymous : bolgListAnonymous) {
			System.out.println(listAnonymous);
		}
	}

	public static  class C{
			private static final String[] AUTH_WHITELIST = {
					// admin接口是后台管理接口 放行到下一个过滤器
					"/admin",
					// 动态接口
					"/bolgTitleById",
					// 日志接口
					"/archives",
			};

		public static void main(String[] args) {

			System.out.println(System.currentTimeMillis()/1000);
			System.out.println(System.currentTimeMillis()/1000 - 30 * 60);
			System.out.println(LocalDateTime.ofEpochSecond(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli(), 0, ZoneOffset.ofHours(8)));
			System.out.println(LocalDateTime.ofEpochSecond(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() - 30 * 60 * 1000 , 0, ZoneOffset.ofHours(8)));

			// 将localDateTime转换为时间戳
			System.out.println(LocalDateTime.now().toInstant(ZoneOffset.of("+8")));
			System.out.println( LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8)));

		}


	}





}
