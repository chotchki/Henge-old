package us.chotchki.springWeb.db.dao;

import org.apache.ibatis.annotations.Insert;

import us.chotchki.springWeb.db.pojo.User;

public interface UsersDao {
	@Insert("insert into users (username, password, enabled) values (#{username}, #{password}, #{enabled})")
	public int create(User user);
}