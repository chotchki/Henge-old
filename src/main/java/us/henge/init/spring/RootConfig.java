package us.henge.init.spring;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import us.henge.db.handlers.DateTimeTypeHandler;
import us.henge.db.handlers.InstantTypeHandler;

import com.googlecode.flyway.core.Flyway;

@Configuration
@EnableTransactionManagement
@MapperScan("us.henge.db.dao")
@ComponentScan(value = "us.henge", excludeFilters = {
		  @ComponentScan.Filter(value=Controller.class)
		  })
public class RootConfig {
	private static final Logger log = LoggerFactory.getLogger(RootConfig.class);
	
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
	public DateTimeTypeHandler dateTimeTypeHandler(){
		return new DateTimeTypeHandler();
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		SqlSessionFactory ssf =  sessionFactory.getObject();
		
		ssf.getConfiguration().getTypeHandlerRegistry().register(Instant.class, instantTypeHandler());
		ssf.getConfiguration().getTypeHandlerRegistry().register(Instant.class, null, instantTypeHandler());
		ssf.getConfiguration().getTypeHandlerRegistry().register(DateTime.class, dateTimeTypeHandler());
		ssf.getConfiguration().getTypeHandlerRegistry().register(DateTime.class, null, dateTimeTypeHandler());
		return ssf;
	}

	@Bean
	public Flyway flyway() {
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource());
		flyway.migrate();
		return flyway;
	}
	
	/**
	 * Increases Bcrypt rounds until it hits 0.5 seconds
	 * @return
	 * @throws Exception 
	 */
	@Bean
	public BCryptPasswordEncoder BCryptPasswordEncoder() throws Exception{
		BCryptPasswordEncoder b;
		for(int i = 10; i < 32; i++){
			b = new BCryptPasswordEncoder(i);
			Instant start = new Instant();
			b.encode("password");
			Instant end = new Instant();
			
			Duration dur = new Duration(start, end);
			if(dur.isLongerThan(Duration.millis(400))){
				log.info("Bcrypt rounds set to: {}", i);
				return b;
			}
		}
		throw new Exception("Bcrypt cannot be setup in a reasonable number of rounds");
	}
}
