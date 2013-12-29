package us.chotchki.springWeb.db.dao;

import org.apache.ibatis.annotations.Insert;

public interface AuthoritiesDao {
	@Insert("insert into authorities (username, authority) values (#{username}, #{authority})")
	public int create(String username, String authority);
}