package us.chotchki.springWeb.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import us.chotchki.springWeb.db.dao.PostsDao;
import us.chotchki.springWeb.db.pojo.Post;

@Repository
public class PostsService {
	@Autowired
	private PostsDao postsDao = null;

	public List<Post> getPostsByPage(int page) {
		return postsDao.getPostsByPage((page - 1) * PostsDao.POSTS_PER_PAGE);
	}
	
	public Post getPostById(int id){
		return postsDao.getPostById(id);
	}
	
	public int getPageCount(){
		return postsDao.getPageCount();
	}

	@Transactional
	public int create(Post post) {
		return postsDao.create(post);
	}
	
	@Transactional
	public void update(Post post){
		postsDao.update(post);
	}
	
	@Transactional
	public void delete(int id){
		postsDao.delete(id);
	}
}
