package com.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import net.sf.json.JSONObject;

import org.apache.http.ParseException;
import org.junit.Test;

import com.menu.Menu;
import com.po.AccessToken;

public class WeixinUtilTest {

	@Test
	public void testGetAccessToken() {
		try {
			AccessToken token = WeixinUtil.getAccessToken();
			System.out.println("票据"+token.getToken());
			System.out.println("有效时间"+token.getExpiresIn());
			System.out.println(System.currentTimeMillis());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpload(){	
		String path_temp="D:/TOOLS/MyEclipse/Workspaces/WeixinDemo/WebRoot/music/3nd-untroubled terror.jpg";
		String path_perm="D:/TOOLS/MyEclipse/Workspaces/WeixinDemo/WebRoot/image/github.png";
		try {
//			AccessToken token = WeixinUtil.getAccessToken();
			String token = TokenUtil.getExistsToken();
			String imageId=WeixinUtil.upload_temp(path_temp, token, "thumb");
			String URL=WeixinUtil.upload_perm(path_perm, token);
			System.out.println("临时图片id "+imageId);
			System.out.println("永久图片url "+URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateMenu(){
		try {
			AccessToken token = WeixinUtil.getAccessToken();
			Menu menu=WeixinUtil.initMenu();
			String menuStr=JSONObject.fromObject(menu).toString();
			int result=WeixinUtil.createMenu(token.getToken(), menuStr);
			if(result==0){
				System.out.println("创建菜单成功");
			}else{
				System.out.println("错误码"+result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQueryMenu(){
		try {
			AccessToken token = WeixinUtil.getAccessToken();
			JSONObject jsonObject = WeixinUtil.queryMenu(token.getToken());
			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTranslate(){
		try {
			String result1 = WeixinUtil.translate("中国国家防火墙");
			String result2 = WeixinUtil.translate("Great Firewall of China");
			System.out.println(result1);
			System.out.println(result2);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void testWeather(){
		try {
			String result = WeixinUtil.weather("广州");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetExistsToken(){
		String token = TokenUtil.getExistsToken();
		System.out.println(token);
		System.out.println();
	}
}
