package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.naccl.annotation.VisitLogger;
import top.naccl.entity.Tag;
import top.naccl.enums.VisitBehavior;
import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;
import top.naccl.service.TagService;
import top.naccl.util.StringUtils;

import java.util.List;

/**
 * @Description: 标签
 * @Author: wdd
 * @Date: 2020-08-17
 */
@RestController
public class TagController {
	@Autowired
	BlogService blogService;


	@Autowired
	private TagService tagService;


	/**
	 * 根据标签name分页查询公开博客列表
	 *
	 * @param tagName 标签name
	 * @param pageNum 页码
	 * @return
	 */
	@VisitLogger(VisitBehavior.TAG)
	@GetMapping("/tag")
	public Result tag(@RequestParam String tagName,
	                  @RequestParam(defaultValue = "1") Integer pageNum) {
		// 根据标签name分页查询公开博客列表
		PageResult<BlogInfo> pageResult = blogService.getBlogInfoListByTagNameAndIsPublished(tagName, pageNum);
		return Result.ok("请求成功", pageResult);
	}

	/**
	 * 增加标签
	 *
	 * @param tag
	 * @return
	 */
	@PostMapping("/user/addTag")
	public Result addTag(@RequestBody Tag tag) {
		//TODO 前端存在标签#的情况 需要处理
		tag.setName(tag.getName().replace("#", ""));
		if (StringUtils.isEmpty(tag.getName())) {
			return Result.error("参数不能为空");
		}
		//查询标签是否已存在
		Tag tag1 = tagService.getTagByName(tag.getName());
		//如果 tag1.getId().equals(tag.getId()) == true 就是更新标签
		if (tag1 != null && !tag1.getId().equals(tag.getId())) {
			return Result.error("该标签已存在");
		}
		tagService.saveTag(tag);
		return Result.ok("添加成功");

	}




}
