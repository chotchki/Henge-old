package us.chotchki.springWeb.service;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.springframework.stereotype.Service;

@Service
public class Markdown {
	private PegDownProcessor pegDown = new PegDownProcessor(Extensions.ALL);
	
	public synchronized String toHTML(String content) {
		return pegDown.markdownToHtml(content);
	}
}
