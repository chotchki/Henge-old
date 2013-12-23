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
		return postsDao.getPostsByPage(page);
	}
	
	public List<Post> getPostById(int id){
		return postsDao.getPostById(id);
	}

	@Transactional
	public void create(Post post) {
		postsDao.create(post);
	}
}