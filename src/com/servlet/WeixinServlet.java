package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.util.CheckUtil;
import com.util.MessageUtil;
import com.util.WeixinUtil;

/**
 * 微信后台交互
 * @author Sumkor
 * https://github.com/Sumkor
 */
public class WeixinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * 接入验证
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String signature=req.getParameter("signature");
		String timestamp=req.getParameter("timestamp");
		String nonce=req.getParameter("nonce");
		String echostr=req.getParameter("echostr");
		PrintWriter out=resp.getWriter();
		//开发者通过检验signature对请求进行校验
		//若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
	}
	
	/**
	 * 消息的接收与响应
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out=resp.getWriter();
		try {
			//微信发送过来的消息类型为xml
			Map<String, String>map=MessageUtil.xmlToMap(req);
			String fromUserName=map.get("FromUserName");
			String toUserName=map.get("ToUserName");
			String msgType=map.get("MsgType");
			String content=map.get("Content");
//			String createTime=map.get("CreateTime");
//			String MsgId=map.get("MsgId");	
			String message=null;
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)){	
				//被动回复消息
				if("1".equals(content)){
					message=MessageUtil.initText(toUserName, fromUserName, MessageUtil.textMenu());
				}else if("2".equals(content)){
					message=MessageUtil.initNewsMessage(toUserName, fromUserName);
				}else if("3".equals(content)){
					message=MessageUtil.initImageMessage(toUserName, fromUserName);
				}else if("4".equals(content)){
					message=MessageUtil.initMusicMessage(toUserName, fromUserName);
				}else if("5".equals(content)){
					message=MessageUtil.initText(toUserName, fromUserName, MessageUtil.transMenu());
				}else if("6".equals(content)){
					message=MessageUtil.initText(toUserName, fromUserName, MessageUtil.weatherMenu());
				}else if("?".equals(content)||"？".equals(content)){
					message=MessageUtil.initText(toUserName, fromUserName, MessageUtil.mainMenu());
				}else if(content.startsWith("翻译")){
					String word = content.replaceAll("^翻译", "").trim();
					if("".equals(word)){
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.transMenu());
					}else{
						message = MessageUtil.initText(toUserName, fromUserName, WeixinUtil.translate(word));
					}
				}else if(content.startsWith("天气")){
					String word = content.replaceAll("^天气", "").trim();
					if("".equals(word)){
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.weatherMenu());
					}else{
						message = MessageUtil.initText(toUserName, fromUserName, WeixinUtil.weather(word));
					}
				}		
			}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
				//自定义菜单-事件推送
				String eventType=map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					message=MessageUtil.initText(toUserName, fromUserName, MessageUtil.mainMenu());
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.mainMenu());
				}else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
					String url = map.get("EventKey");
					message = MessageUtil.initText(toUserName, fromUserName, url);
				}else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
					String key = map.get("EventKey");
					message = MessageUtil.initText(toUserName, fromUserName, key);
				}
			}else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){
				String label = map.get("Label");
				message = MessageUtil.initText(toUserName, fromUserName, label);
			}
			System.out.println(message);
			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
}
