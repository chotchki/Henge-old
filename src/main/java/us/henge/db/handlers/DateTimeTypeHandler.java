package us.henge.db.handlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateTimeTypeHandler implements TypeHandler<DateTime> {

	@Override
	public DateTime getResult(ResultSet rs, String columnName) throws SQLException {
		Timestamp ts = rs.getTimestamp(columnName);
		if(ts != null){
			return new DateTime(ts.getTime());
		} else {
			return null;
		}
	}

	@Override
	public DateTime getResult(ResultSet rs, int columnNumber) throws SQLException {
		Timestamp ts = rs.getTimestamp(columnNumber);
		if(ts != null){
			return new DateTime(ts.getTime());
		} else {
			return null;
		}
	}

	@Override
	public DateTime getResult(CallableStatement cs, int columnNumber) throws SQLException {
		Timestamp ts = cs.getTimestamp(columnNumber);
		if(ts != null){
			return new DateTime(ts.getTime());
		} else {
			return null;
		}
	}

	@Override
	public void setParameter(PreparedStatement ps, int columnNumber, DateTime instant, JdbcType type) throws SQLException {
		if(instant != null){
			DateTime dt = instant.toDateTime(DateTimeZone.UTC);
			ps.setTimestamp(columnNumber, new Timestamp(dt.getMillis()));
		} else {
			ps.setTimestamp(columnNumber, null);
		}
	}

}
