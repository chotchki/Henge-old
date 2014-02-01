package us.henge.web;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import us.henge.init.Config.Keys;

@Controller
@RequestMapping(value = "/tracking")
public class Tracking {
	private static final String placeKey = "TRACKERKEY";
	private static final String placeDomain = "TRACKERDOMAIN";
	private static final String googleSupplied = 
			"  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){" + 
			"  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o)," + 
			"  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)" + 
			"  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');" + 
			"  ga('create', '" + placeKey +"', '" + placeDomain + "');" + 
			"  ga('send', 'pageview');";
	private String finalString = " ";
	
	@Autowired
	private ServletContext context;
	
	@PostConstruct
	public void initIt() {
		String trackingKey = context.getInitParameter(Keys.googleApiKey.toString());
		String trackingDomain = context.getInitParameter(Keys.googleApiDomain.toString());
		if(trackingKey == null || trackingDomain == null){
			return;
		}
		
		finalString = googleSupplied.replace(placeKey, trackingKey).replace(placeDomain, trackingDomain);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String index(){
		return finalString;
	}
}
