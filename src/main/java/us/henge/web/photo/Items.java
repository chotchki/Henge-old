package us.henge.web.photo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;

import us.henge.db.pojo.Item;
import us.henge.db.service.ItemsService;
import us.henge.service.content.ContentService;
import us.henge.service.content.PhotoOpService;
import us.henge.service.content.PhotoService;

@Controller
@RequestMapping(value = "/photos/items")
public class Items {
	@Autowired
	private ContentService contentService = null;
	
	@Autowired
	private ItemsService itemsService = null;
	
	@Autowired
	private PhotoService photoService = null;
	
	@Autowired
	private PhotoOpService photoOpService = null;
	
	@RequestMapping(value = "/{number}/view/{size}", method = RequestMethod.GET)
	public void viewAlbum(@PathVariable("number") int number, @PathVariable("size") int size, HttpServletResponse response) throws Exception {
		Item item = itemsService.getItemById(number);
		byte[] hash = item.getHash();

		photoOpService.serveResized(response, contentService.getTargetPath(hash), size);
	}
}