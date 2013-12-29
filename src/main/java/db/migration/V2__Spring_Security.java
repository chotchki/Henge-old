package db.migration;

import db.Migration;

public class V2__Spring_Security extends Migration {
	@Override
	public String[] getSQLs() {
		return new String[] {
				"create table users( " + 
						"username varchar(500) not null primary key, " +
						"password varchar(500) not null, " +
						"enabled boolean not null)",
				"create table authorities (" +
						"username varchar(500) not null, " +
						"authority varchar(500) not null, " +
						"constraint fk_authorities_users foreign key(username) references users(username))",
				"create unique index ix_auth_username on authorities (username,authority)"};
	}
}
