package top.naccl.service;

import top.naccl.entity.Blog;
import top.naccl.entity.Moment;

import java.util.List;

public interface MomentService {
	List<Moment> getMomentList();

	List<Moment> getMomentVOList(Integer pageNum, boolean adminIdentity);

	void addLikeByMomentId(Long momentId);

	void updateMomentPublishedById(Long momentId, Boolean published);

	Moment getMomentById(Long id);

	void deleteMomentById(Long id);

	void saveMoment(Moment moment);

	void updateMoment(Moment moment);

	/**
	 *通过userId获得内容的标题
	 * @param id
	 */
	List<Blog> getBolgTitleById(Long id);
}
