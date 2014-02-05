package db.migration;

import db.Migration;

public class V5__Item_Allow_Null extends Migration {

	@Override
	public String[] getSQLs() {
		return new String[]{
			"ALTER TABLE Items ALTER COLUMN mimeType SET NULL",
			"ALTER TABLE Items ALTER COLUMN hash SET NULL"
		};
	}

}
