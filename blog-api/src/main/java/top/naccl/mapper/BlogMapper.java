package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Blog;
import top.naccl.model.dto.BlogView;
import top.naccl.model.dto.BlogVisibility;
import top.naccl.model.vo.*;

import java.util.List;

/**
 * @Description: 博客文章持久层接口
 * @Author: wdd
 * @Date: 2020-07-26
 */
@Mapper
@Repository
public interface BlogMapper {
	List<Blog> getListByTitleAndCategoryId(String title, Integer categoryId);

	List<SearchBlog> getSearchBlogListByQueryAndIsPublished(String query);

	List<Blog> getIdAndTitleList();

	List<NewBlog> getNewBlogListByIsPublished();

	List<BlogInfo> getBlogInfoListByIsPublished();

	List<BlogInfo> getBlogInfoListByCategoryNameAndIsPublished(String categoryName);

	List<BlogInfo> getBlogInfoListByTagNameAndIsPublished(String tagName);

	List<String> getGroupYearMonthByUserId(Long id);

	List<ArchiveBlog> getArchiveBlogListByYearMonthByUserId(String yearMonth,Long id);

	List<RandomBlog> getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend(Integer limitNum);

	List<BlogView> getBlogViewsList();

	int deleteBlogById(Long id);

	int deleteBlogTagByBlogId(Long blogId);

	int saveBlog(top.naccl.model.dto.Blog blog);

	int saveBlogTag(Long blogId, Long tagId);

	int updateBlogRecommendById(Long blogId, Boolean recommend);

	int updateBlogVisibilityById(Long blogId, BlogVisibility bv);

	int updateBlogTopById(Long blogId, Boolean top);

	int updateViews(Long blogId, Integer views);

	Blog getBlogById(@Param("id") Long id, @Param("userId")Long userId);

	String getTitleByBlogId(Long id);

	BlogDetail getBlogByIdIsNotPublish(Long id);
	BlogDetail getBlogByIdAndIsPublished(Long id);

	String getBlogPassword(Long blogId);

	int updateBlog(top.naccl.model.dto.Blog blog);

	int countBlog();

	int countBlogByUserId(Long id);

	int countBlogByCategoryId(Long categoryId);

	int countBlogByTagId(Long tagId);

	Boolean getCommentEnabledByBlogId(Long blogId);

	Boolean getPublishedByBlogId(Long blogId);

	List<CategoryBlogCount> getCategoryBlogCountList();


	List<BlogWithMomentView> getBolgTitleById(Long id);

	List<BlogWithMomentView> getBolgListAnonymous(Long userId);

    Boolean addLikeByBlogId(Long id);

	List<Blog> getBlogByUserId(Long id);

	Long getPublishedByBlogIdWithUserId(Long blogId, Long userId);

}
