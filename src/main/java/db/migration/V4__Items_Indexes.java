package db.migration;

import db.Migration;

public class V4__Items_Indexes extends Migration {

	@Override
	public String[] getSQLs() {
		return new String[] {
			"CREATE INDEX Items_Dup_Idx on Items ( hash )",
			"CREATE INDEX Items_Par_Idx on Items ( parentId )"
		};
	}

}
