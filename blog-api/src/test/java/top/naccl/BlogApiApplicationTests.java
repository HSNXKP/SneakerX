package top.naccl;

import jdk.jfr.internal.Logger;
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
			for (String s : AUTH_WHITELIST) {
				String s1 ="/archives";
				if (s1.equals(s)){
					return;
				}
			}
			System.out.println("进来了");
		}


	}





}
