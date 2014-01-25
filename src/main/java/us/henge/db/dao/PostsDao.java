package us.henge.db.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import us.henge.db.pojo.Post;

public interface PostsDao {
	public final static int POSTS_PER_PAGE = 5;
	
	@Select("select id, published, title, content from posts order by published desc OFFSET #{offset} LIMIT " + POSTS_PER_PAGE)
	public List<Post> getPostsByPage(int offset);
	
	@Select("select id, published, title, content from posts where id = #{id}")
	public Post getPostById(int id);
	
	@Select("select GREATEST(CEILING(count(*) / " + POSTS_PER_PAGE + "),1) from posts")
	public int getPageCount();
	
	@Insert("insert into posts (id, published, title, content) values (#{id}, #{published}, #{title}, #{content})")
	@SelectKey(statement = "CALL IDENTITY()", before = false, keyProperty = "id", resultType = BigDecimal.class)
	public int create(Post post);
	
	@Update({"update posts",
			 "set published = #{published},",
			 "title = #{title},",
			 "content = #{content}",
			 "where id = #{id}"})
	public void update(Post post);
	
	@Delete("delete from posts where id = #{id}")
	public void delete(int id);
}