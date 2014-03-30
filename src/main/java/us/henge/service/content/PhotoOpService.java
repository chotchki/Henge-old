package us.henge.service.content;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;

@Service
public class PhotoOpService {
	@Autowired
	private CacheManager cacheManager = null;
	
	public byte[] getResized(File file, int maxWidth) throws IOException{
		Cache thumbCache = cacheManager.getCache("thumbnail");
		
		String key = file.getName() + "|" + maxWidth;
		ValueWrapper vw = thumbCache.get(key);
		if(vw != null){
			return (byte[]) vw.get();
		}
		
		BufferedImage image = ImageIO.read(file); 
		
		ResampleOp  resampleOp = new ResampleOp (DimensionConstrain.createMaxDimension(maxWidth, 2000));
		resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
		BufferedImage rescaledImage = resampleOp.filter(image, null);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(rescaledImage, "JPEG", bos);
        byte[] bImage = bos.toByteArray();

		thumbCache.put(key, bImage);
		
		return bImage;
	}
	
	public void serveResized(HttpServletResponse response, File file, int maxWidth) throws IOException{
		byte[] image = getResized(file, maxWidth);

        response.setContentType("image/jpeg");
        
        response.setContentLength(image.length);
        response.getOutputStream().write(image);
	}
}
