package us.henge.web.photo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import us.henge.db.pojo.Item;
import us.henge.db.pojo.ItemType;
import us.henge.db.pojo.Post;
import us.henge.db.service.ItemsService;

@Controller
@RequestMapping(value = "/photos")
public class Photos {	
	@Autowired
	private ItemsService itemsService = null;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return "redirect:/photos/page/1";
	}
	
	@RequestMapping(value = "/page/{number}")
	public String showPage(@PathVariable int number, Model mod) {
		List<Item> albums = itemsService.getAlbumsByPage(number);
		mod.addAttribute("albums", albums);
		
		mod.addAttribute("currentPage", number);
		mod.addAttribute("totalPages", itemsService.getAlbumCount());
		return "photos/page";
	}
	
	@RequestMapping(value = "/auto/{number}", method = RequestMethod.GET)
	public String uncertain(@PathVariable int number, RedirectAttributes redirAttr) {
		Item i = itemsService.getItemById(number);
		if(i == null){
			redirAttr.addFlashAttribute("error", "Item number " + number + " is not a valid number");
			return "redirect:/photos";
		} else if (i.getItemType() == ItemType.ALBUM){
			return "redirect:/photos/album/" + number;
		} else {
			return "redirect:/photos/photo/" + number;
		}
	}
}