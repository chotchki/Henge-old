package us.henge.db.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import us.henge.db.pojo.Item;

public interface ItemsDao {
	public final String selectList = "id, parentId, defaultId, name, date, mimeType, hash";
	public final String typeCase = ", (CASE WHEN parentId is null THEN 'ALBUM' WHEN (SELECT i.parentId from Items i where i.id = parentId) is null THEN 'PHOTO' ELSE 'VERSION' END)";
	
	@Select({"select", selectList, typeCase, "from Items where id = #{id}"})
	public Item getItemById(int id);
	
	@Select({"select", selectList, typeCase, "from Items where parentId = #{id}"})
	public List<Item> getItemsByParentId(int id);
	
	@Select({"select", selectList, typeCase, "from Items where hash = #{hash}"})
	public List<Item> getItemsByHash(@Param("hash") byte[] hash);
	
	@Insert({"insert into items (", selectList, ") values (#{id}, #{parentId}, #{defaultId}, #{name}, #{date}, #{mimeType}, #{hash})"})
	@SelectKey(statement = "CALL IDENTITY()", before = false, keyProperty = "id", resultType = BigDecimal.class)
	public int create(Item item);
	
	@Update({"update items",
		 "set parentId = #{parentId},",
		 "defaultId = #{defaultId},",
		 "name = #{name},",
		 "date = #{date},",
		 "mimeType = #{mimeType},",
		 "hash = #{hash}",
		 "where id = #{id}"})
	public void update(Item item);
	
	@Delete("delete from items where id = #{id}")
	public void delete(int id);
}