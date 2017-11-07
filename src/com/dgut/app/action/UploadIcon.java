package com.dgut.app.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgut.app.utils.JSONUtils;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.manager.MemberMng;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.dgut.common.upload.FileRepository;
import com.dgut.common.web.ResponseUtils;
import com.dgut.main.web.CmsUtils;


@Controller
public class UploadIcon {
	private static final Logger log = LoggerFactory
			.getLogger(UploadIcon.class);


	



	@RequestMapping(value = "/uploadIcon.do", method = RequestMethod.POST)
	public void getIndex(
			@RequestParam(value = "icon", required = true) MultipartFile icon,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException{

		Member member=CmsUtils.getMember(request);
		//		member=memberMng.findById(3);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (CmsUtils.getMember(request) == null) {

			jsonMap.put("state", "-2");
			jsonMap.put("msg", "太久未登录，请重新登录");
			try {
				ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			} catch (IOException e) {
				log.error("上传头像时:"+e.getMessage());
			}
		}



		String iconUrl=null;
		if(!icon.isEmpty()){
			try {
				iconUrl=savePhoto( icon, request);
				member.setIcon(iconUrl);
				memberMng.updateMember(member);
				jsonMap.put("state", "0");
				jsonMap.put("icon", iconUrl);
				jsonMap.put("msg", "上传头像成功");
				ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				log.error("保存用户头像时:"+e1.getMessage());
			}
		}
		else{
			jsonMap.put("state", "-1");
			jsonMap.put("msg", "头像不能为空");
			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
		}
		

	}

	/**
	 * 保存图片到本地
	 * @param iconFile
	 * @return
	 * @throws IOException 
	 */
	public    String savePhoto(

		MultipartFile iconFile,HttpServletRequest request) throws IOException {
		String photoUrl=null;

		//文件名
		String photoOrigName = iconFile.getOriginalFilename();
		//后缀名
		String photoExt = FilenameUtils.getExtension(photoOrigName).toLowerCase(
				Locale.ENGLISH);
		photoUrl = fileRepository.storeByExt("/upload/app/file/icon", photoExt, iconFile);
		ServletContext  context=request.getSession().getServletContext();
		File fi = new File(context.getRealPath(photoUrl)); //大图文件  
		System.out.println(fi);


		return photoUrl;
	}
	
	@Autowired
	private FileRepository fileRepository;



	@Autowired
	private MemberMng memberMng;


	private Member member;
}
