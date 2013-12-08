package us.chotchki.springWeb.init;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.googlecode.flyway.core.Flyway;

@Configuration
@EnableTransactionManagement
public class RootConfig {
	
	@Bean
	@Resource(name="jdbc/DB")
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
    public Flyway flyway() {
    	Flyway flyway = new Flyway();
    	flyway.setDataSource(dataSource());
    	flyway.migrate();
    	return flyway;
    }
}
