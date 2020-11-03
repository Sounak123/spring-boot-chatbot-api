package com.lang.process.chatbot.controller;

import java.io.File;

import javax.annotation.PostConstruct;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lang.process.chatbot.model.Message;

@RestController
public class ChatbotController {
	
	private static final boolean TRACE_MODE = false;
	static String botName = "ULTRON";
	
	private Chat chatSession;
	private Bot bot;
	
	@PostConstruct
	private void init() {
		String resourcesPath = getResourcesPath();
        System.out.println(resourcesPath);
        MagicBooleans.trace_mode = TRACE_MODE;
        bot = new Bot("super", resourcesPath);
        chatSession = new Chat(bot);
        bot.brain.nodeStats();
	}
	
	@PostMapping("/chat")
	public String chatWithBot(@RequestBody Message message) {
		 String request = message.getValue();
         if (MagicBooleans.trace_mode)
             System.out.println("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
         String response = chatSession.multisentenceRespond(request);
         while (response.contains("&lt;"))
             response = response.replace("&lt;", "<");
         while (response.contains("&gt;"))
             response = response.replace("&gt;", ">");
		return response;
	}
	
	
	
	
	
	private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        System.out.println(path);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        return resourcesPath;
    }
	
	
}
