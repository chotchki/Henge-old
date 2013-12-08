package db;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

import org.springframework.jdbc.core.JdbcTemplate;

import com.googlecode.flyway.core.api.migration.MigrationChecksumProvider;
import com.googlecode.flyway.core.api.migration.spring.SpringJdbcMigration;

public abstract class Migration implements MigrationChecksumProvider, SpringJdbcMigration{
	public abstract String[] getSQLs();
	
	public final void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		for(String sql: getSQLs()) {
			jdbcTemplate.execute(sql);
		}
	}
	
	public final Integer getChecksum() {
		CRC32 crc = new CRC32();
		for(String sql: getSQLs()) {
			crc.update(sql.getBytes(StandardCharsets.UTF_8));
		}
		return (int) crc.getValue();
	}
}
