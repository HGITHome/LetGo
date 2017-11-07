package com.dgut.app.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgut.app.utils.JSONUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.dgut.app.utils.UploadUtils;
import com.dgut.common.image.ImageScale;
import com.dgut.common.upload.FileRepository;
import com.dgut.common.web.ResponseUtils;
import com.dgut.main.web.CmsUtils;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Photo;
import com.dgut.main.member.entity.Talk;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.member.manager.TalkMng;

@Controller
public class AddTalkAct {

	private static final Logger log = LoggerFactory.getLogger(AddTalkAct.class);

	private static final int MINI_WIDTH = 256;
	private static final int MINI_HEIGHT = 256;

	private static final int LARGE_WIDTH = 700;
	private static final int LARGE_HEIGHT = 800;

	@RequestMapping(value = "/addTalk.do", method = RequestMethod.POST)
	public void getIndex(
			@RequestParam(name = "photos", required = false) MultipartFile[] photos,
			@RequestParam(name = "video", required = false) MultipartFile video,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {

		Member member = CmsUtils.getMember(request);
		// member = memberMng.findById(3);
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		String video_directory = "/upload/app/file/talk/video";
		String photo_directory = "/upload/app/file/talk/photo";


		if (CmsUtils.getMember(request) == null) {

			jsonMap.put("state", -2);
			jsonMap.put("msg", "太久未登录，请重新登录");

			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;
		}
		String content = request.getParameter("content");

		if (StringUtils.isNotBlank(content) && content.length() >= 3) {
			content = content.substring(1, content.length() - 1);
		}

		// 校验说说是否合法
		String result = validateTalk(photos, video, content);
		if (result != null) {
			jsonMap.put("state", -1);
			jsonMap.put("msg", " 发表说说失败");
			jsonMap.put("error", result);

			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));
			return;

		} else {
			String video_url = null;
			if (video != null && !video.isEmpty()) {
				video_url = UploadUtils.saveFile(video_directory, video,
						fileRepository, request);
			}

			List<Photo> talk_photos = new ArrayList<Photo>();
			Photo photo = null;
			String sourcePhoto_Url = null, mini_photo = null,large_photo=null;
			if (photos != null) {
				for (int i = 0; i < photos.length; i++) {
					if (!photos[i].isEmpty()) {

						photo = new Photo();
						ServletContext  sc=request.getServletContext();
						sourcePhoto_Url = UploadUtils.saveFile(photo_directory,
								photos[i], fileRepository, request);

						mini_photo = sourcePhoto_Url.substring(0, sourcePhoto_Url.lastIndexOf('/'))+"/mini"+
								sourcePhoto_Url.substring(sourcePhoto_Url.lastIndexOf('/'));
						imageScale.resizeFix(new File(sc.getRealPath(sourcePhoto_Url)), new File(sc.getRealPath(mini_photo)), MINI_WIDTH,
								MINI_HEIGHT);

						large_photo =  sourcePhoto_Url;
						imageScale.resizeFix(new File(sc.getRealPath(sourcePhoto_Url)), new File(sc.getRealPath(large_photo)), LARGE_WIDTH,
								LARGE_HEIGHT);

						photo.setPhoto(large_photo);
						photo.setMini_photo(mini_photo);

						talk_photos.add(photo);
					}
				}
			}

			Talk talk = talkMng.save(content, video_url, talk_photos, member);
			jsonMap.put("state", 0);
			jsonMap.put("msg", " 发表说说成功");

			ResponseUtils.renderJson(response, JSONUtils.printObject(jsonMap));

		}

	}

	/**
	 * 检验说说的参数，格式是否完整
	 *
	 * @param photos
	 * @param video
	 * @param words
	 * @return
	 * @throws IOException
	 */
	private String validateTalk(MultipartFile[] photos, MultipartFile video,
								String words) throws IOException {
		String result = null;

		if (video != null) {
			if (video.isEmpty() && StringUtils.isBlank(words)
					&& isEmpty(photos)) {
				// 说说参数不完整
				result = "301";
			} else if (!UploadUtils.checkFileTypeIsValid(video, "2")) {
				// 视频格式有误
				result = "302";
			}
		} else {
			if (StringUtils.isBlank(words) && isEmpty(photos)) {
				// 说说参数不完整
				result = "301";
			}
		}

		if (photos != null) {
			for (MultipartFile f : photos) {
				if (!UploadUtils.checkFileTypeIsValid(f, "1")) {
					// 图片格式有误
					result = "303";
					break;
				}
			}
		}

		return result;
	}

	/**
	 * 检查文件是否为空
	 *
	 * @param photos
	 * @return
	 */
	private boolean isEmpty(MultipartFile[] photos) {
		Boolean isEmpty = true;
		if(photos!=null) {
			for (MultipartFile f : photos) {
				if (!f.isEmpty()) {
					isEmpty = false;
					break;
				}
			}
		}
		return isEmpty;
	}

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private ImageScale imageScale;

	@Autowired
	private MemberMng memberMng;

	@Autowired
	private TalkMng talkMng;

	@Resource(name="memberLogMng")
	private UserLogMng memberLogMng;

	private Member member;
}
