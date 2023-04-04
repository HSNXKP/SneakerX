package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Category;
import top.naccl.entity.Product;
import top.naccl.entity.Tag;
import top.naccl.model.vo.NewBlog;
import top.naccl.model.vo.RandomBlog;
import top.naccl.model.vo.Result;
import top.naccl.service.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: 站点相关
 * @Author: wdd
 * @Date: 2020-08-09
 */

@RestController
public class IndexController {
	@Autowired
	SiteSettingService siteSettingService;
	@Autowired
	BlogService blogService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	TagService tagService;

	@Autowired
	private ProductService productService;

	/**
	 * 获取站点配置信息、最新推荐博客、分类列表、标签云、随机博客、随机商品
	 *
	 * @return
	 */
	@GetMapping("/site")
	public Result site() {
		Map<String, Object> map = siteSettingService.getSiteInfo();
		List<NewBlog> newBlogList = blogService.getNewBlogListByIsPublished();
		List<Category> categoryList = categoryService.getCategoryNameList();
		List<Tag> tagList = tagService.getTagListNotId();
		List<RandomBlog> randomBlogList = blogService.getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend();
		List<Product> randomProductList = productService.getProductsIsRecommend();
		map.put("newBlogList", newBlogList);
		map.put("categoryList", categoryList);
		map.put("tagList", tagList);
		map.put("randomBlogList", randomBlogList);
		map.put("randomProductList",randomProductList);
		return Result.ok("请求成功", map);
	}
}
