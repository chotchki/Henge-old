package us.henge.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import us.henge.db.dao.ItemsDao;
import us.henge.db.pojo.Item;

@Repository
public class ItemsService {
	@Autowired
	private ItemsDao itemsDao = null;
	
	public Item getItemById(int id){
		return itemsDao.getItemById(id);
	}
	
	public boolean existsByHash(byte[] hash){
		List<Item> items = itemsDao.getItemsByHash(hash);
		if(items.size() == 0){
			return false;
		} else {
			return true;
		}
	}
	
	@Transactional
	public int create(Item item) {
		return itemsDao.create(item);
	}
	
	@Transactional
	public void update(Item item){
		itemsDao.update(item);
	}
	
	@Transactional
	public void delete(int id){
		itemsDao.delete(id);
	}
}
