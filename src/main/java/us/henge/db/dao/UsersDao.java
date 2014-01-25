package us.henge.db.dao;

import org.apache.ibatis.annotations.Insert;

import us.henge.db.pojo.User;

public interface UsersDao {
	@Insert("insert into users (username, password, enabled) values (#{username}, #{password}, #{enabled})")
	public int create(User user);
}