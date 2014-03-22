package us.henge.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.henge.db.pojo.Item;
import us.henge.db.service.ItemsService;
import us.henge.utility.Hasher;
import us.henge.utility.ImageFilenameFilter;
import us.henge.web.pojo.AddPreview;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

@Service
public class PhotoService {
	private static final Logger log = LoggerFactory.getLogger(PhotoService.class);
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private InitParamService initParamService = null;
	
	@Autowired
	private ItemsService itemsService;
	
	@Transactional
	public void addPhotos(int parent, List<String> fileNames) throws Exception{
		Item parentItem = itemsService.getItemById(parent);
		
		if(parentItem == null){
			throw new Exception("Parent Item " + parent + " does not exist");
		}
		
		for(String fileName: fileNames){
			File file = getFile(fileName);
			byte[] hash = contentService.copyContent(file);
			
			Item item = new Item();
			item.setName(fileName);
			
			try(InputStream is = new BufferedInputStream(new FileInputStream(file))){
				String mimeType = URLConnection.guessContentTypeFromStream(is);
				if(mimeType == null) {
					mimeType = URLConnection.guessContentTypeFromName(fileName);
				}
				
				if(mimeType != null){
					item.setMimeType(mimeType);
				}
			} 
			
			item.setParentId(parentItem.getId());
			item.setHash(hash);
			
			Metadata metadata = ImageMetadataReader.readMetadata(file);
			ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
			if(directory != null){
				Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
				item.setDate(new DateTime(date));
			}
			if(item.getDate() == null){
				item.setDate(new DateTime(file.lastModified()));
			}
			itemsService.create(item);
		}
	}
	
	public List<AddPreview> listFolder(){
		List<AddPreview> fd = new ArrayList<AddPreview>();
		
		File[] pictures = initParamService.getUploadDirectory().listFiles(new ImageFilenameFilter());
		if(pictures == null){
			return fd;
		}
		
		for(File pic : pictures){
			try {
				AddPreview ap = new AddPreview();
				ap.setName(pic.getName());
				ap.setDuplicate(itemsService.existsByHash(Hasher.hash(pic)));
			
				Metadata metadata = ImageMetadataReader.readMetadata(pic);
				ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
				if(directory != null){
					Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
					ap.setDate(new DateTime(date));
				}
				
				fd.add(ap);
			} catch (ImageProcessingException | IOException | NoSuchAlgorithmException e) {
				AddPreview ap = new AddPreview();
				ap.setName("Load Error " + pic.getName());
				fd.add(ap);
			}
		}
		
		return fd;
	}
	
	public File getFile(String fileName){
		return new File(initParamService.getUploadDirectory().getPath() + File.separator + fileName);
	}
}
