package top.naccl.service;

import org.springframework.web.bind.annotation.RequestHeader;
import top.naccl.entity.Blog;
import top.naccl.entity.Moment;
import top.naccl.model.vo.BlogWithMomentView;
import top.naccl.model.vo.Result;

import java.util.List;

public interface MomentService {
	List<Moment> getMomentList();

	List<Moment> getMomentVOList(Integer pageNum, boolean adminIdentity);

	void updateMomentPublishedById(Long momentId, Boolean published);

	Moment getMomentById(Long id);

	void deleteMomentById(Long id);

	void saveMoment(Moment moment);

	void updateMoment(Moment moment);

	/**
	 *通过userId获得内容的标题
	 * @param id
	 */
	List<BlogWithMomentView> getBolgTitleById(Long id, Integer pageNum);

	/**
	 * 点赞动态通过BlogId
	 * @param id
	 */
	Result addLikeByBlogId(Long id);

	/**
	 * 删除Blog
	 *
	 * @param id
	 * @return
	 */
	Result deleteBlogById(Long id,String jwt,Long userId);

	/**
	 * 添加Blog
	 * @param blog
	 * @param type
	 * @return
	 */
	Result editBlog(top.naccl.model.dto.Blog blog, String type,String jwt,Long userId);

	/**
	 * 查询当前Id的Blog
	 * @param id
	 * @return
	 */
	Result getBlogById(Long id,String jwt,Long userId);

	/**
	 * 获得博主的公开信息
	 * @param id
	 * @param pageNum
	 * @return
	 */
	List<BlogWithMomentView> getBolgListAnonymous(Long id, Integer pageNum);
}
