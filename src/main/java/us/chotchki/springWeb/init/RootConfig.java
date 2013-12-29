package us.chotchki.springWeb.init;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.joda.time.Instant;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import us.chotchki.springWeb.db.handlers.InstantTypeHandler;

import com.googlecode.flyway.core.Flyway;

@Configuration
@EnableTransactionManagement
@MapperScan("us.chotchki.springWeb.db.dao")
@ComponentScan(value = "us.chotchki.springWeb", excludeFilters = {
		  @ComponentScan.Filter(value=Controller.class)
		  })
public class RootConfig {
	@Bean
	@Resource(name = "jdbc/DB")
	public DataSource dataSource() {
		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
		dsLookup.setResourceRef(true);
		DataSource dataSource = dsLookup.getDataSource("jdbc/DB");
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager txManager() {
		return new DataSourceTransactionManager(dataSource());
	}
	
	@Bean
	public InstantTypeHandler instantTypeHandler(){
		return new InstantTypeHandler();
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		SqlSessionFactory ssf =  sessionFactory.getObject();
		
		ssf.getConfiguration().getTypeHandlerRegistry().register(Instant.class, instantTypeHandler());
		ssf.getConfiguration().getTypeHandlerRegistry().register(Instant.class, null, instantTypeHandler());
		return ssf;
	}

	@Bean
	public Flyway flyway() {
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource());
		flyway.migrate();
		return flyway;
	}
}
