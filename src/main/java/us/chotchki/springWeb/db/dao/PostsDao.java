package us.chotchki.springWeb.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import us.chotchki.springWeb.db.pojo.Post;

public interface PostsDao {
	public final static int POSTS_PER_PAGE = 5;
	
	@Select("select id, published, title, content from posts where id between #{id} * " + POSTS_PER_PAGE + " and (#{id} + 1 ) * " + POSTS_PER_PAGE)
	public List<Post> getPostsByPage(int page);
	
	@Select("select id, published, title, content from posts where id = #{id}")
	public List<Post> getPostById(int id);
	
	@Insert("insert into posts (id, published, title, content) values (#{id}, #{published}, #{title}, #{content})")
	@Options(useGeneratedKeys = true)
	public void create(Post post);
}