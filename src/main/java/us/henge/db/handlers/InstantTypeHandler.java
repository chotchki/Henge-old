package us.henge.db.handlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.joda.time.Instant;

public class InstantTypeHandler implements TypeHandler<Instant> {

	@Override
	public Instant getResult(ResultSet rs, String columnName) throws SQLException {
		Timestamp ts = rs.getTimestamp(columnName);
		if(ts != null){
			return new Instant(ts.getTime());
		} else {
			return null;
		}
	}

	@Override
	public Instant getResult(ResultSet rs, int columnNumber) throws SQLException {
		Timestamp ts = rs.getTimestamp(columnNumber);
		if(ts != null){
			return new Instant(ts.getTime());
		} else {
			return null;
		}
	}

	@Override
	public Instant getResult(CallableStatement cs, int columnNumber) throws SQLException {
		Timestamp ts = cs.getTimestamp(columnNumber);
		if(ts != null){
			return new Instant(ts.getTime());
		} else {
			return null;
		}
	}

	@Override
	public void setParameter(PreparedStatement ps, int columnNumber, Instant instant, JdbcType type) throws SQLException {
		if(instant != null){
			ps.setTimestamp(columnNumber, new Timestamp(instant.getMillis()));
		} else {
			ps.setTimestamp(columnNumber, null);
		}
	}

}
