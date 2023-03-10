package top.naccl;

import net.minidev.json.JSONObject;
import org.json.JSONString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.naccl.entity.Blog;
import top.naccl.entity.Url;
import top.naccl.entity.User;
import top.naccl.mapper.UrlMapper;
import top.naccl.service.BlogService;
import top.naccl.service.UrlService;
import top.naccl.service.impl.UrlServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
class BlogApiApplicationTests {

	@Autowired
	private BlogService blogService;

	@Test
	void contextLoads() {
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
		String str ="abc/123/$%@//";
		for (int i = 0; i <4 ; i++) {
			String[] split = str.split("/");
			System.out.println(Arrays.toString(split));
		}
	}

	@Autowired
	UrlMapper urlMapper;



	@Test
	void test3(){
		User user = new User();
		// 代码1.在暂存得接口上加上AOP注解
		// 思维层次1.前提暂存 2.受试者编号，访事*，操作*
		// 3.前端给id，编号，访事sql查询，一条没有插入日志 访事 查询出来不用插入日志
		// 4.前端给id，编号，访事，事件，sql查询，一条没有插入日志 事件 查询出来不用插入日志


//		String s ="2022-";
//		String format = String.format(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//		System.out.println(format);
//		System.out.println((LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
		user.setPassword("12345");
		user.setUsername("admin");
		ArrayList<User> users = new ArrayList<>();
		users.add(user);
	}





}
