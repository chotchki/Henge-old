package us.chotchki.springWeb.db.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface AuthoritiesDao {
	@Insert("insert into authorities (username, authority) values (#{username}, #{authority})")
	public int create(@Param("username") String username, @Param("authority") String authority);
}