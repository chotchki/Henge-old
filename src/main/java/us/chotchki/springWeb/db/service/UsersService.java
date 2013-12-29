package us.chotchki.springWeb.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import us.chotchki.springWeb.db.dao.AuthoritiesDao;
import us.chotchki.springWeb.db.dao.UsersDao;
import us.chotchki.springWeb.db.pojo.User;

@Repository
public class UsersService {
	@Autowired
	private AuthoritiesDao authoritiesDao = null;
	
	@Autowired
	private UsersDao usersDao = null;

	@Transactional
	public void create(User user){
		usersDao.create(user);
		for(String authority : user.getAuthorities()){
			authoritiesDao.create(user.getUsername(), authority);
		}
	}
}
