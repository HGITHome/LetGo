package com.dgut.app.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;


import com.dgut.common.upload.FileRepository;

public class UploadUtils {

	/**
	 * 保存文件到指定目录
	 * 
	 * @param saveDirectory
	 * @param iconFile
	 * @param fileRepository
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public  static String saveFile(String saveDirectory, MultipartFile iconFile,
			FileRepository fileRepository, HttpServletRequest request)
					throws IOException {
		String photoUrl = null;

		// 文件名
		String photoOrigName = iconFile.getOriginalFilename();
		// 后缀名
		String photoExt = FilenameUtils.getExtension(photoOrigName)
				.toLowerCase(Locale.ENGLISH);
		photoUrl = fileRepository.storeByExt(saveDirectory, photoExt, iconFile);
		ServletContext context = request.getSession().getServletContext();
		File fi = new File(context.getRealPath(photoUrl)); // 大图文件
		System.out.println(fi);

		return photoUrl;
	}



	/**
	 * 辨别文件格式是否合法(type=1为图片,2为视频)
	 * @param checkFile
	 * @param type
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static Boolean checkFileTypeIsValid(MultipartFile checkFile,String type) throws IOException {
		String fileOrigName = null, fileExt = null;

		Boolean  result=true;

		if (!checkFile.isEmpty()) {
			fileOrigName = checkFile.getOriginalFilename();
			fileExt = FilenameUtils.getExtension(fileOrigName)
					.toLowerCase(Locale.ENGLISH);
			//type=1比较图片格式是否正确
			if(type.equals("1")){
				if (!"GIF,JPG,JPEG,PNG,BMP".contains(fileExt.toUpperCase())) {

					result=false;

				}
			}
			//type=2 比较视频格式是否有误
			else if(type.equals("2")){
				if(!"AVI,FLV,MP4,RMVB".contains(fileExt.toUpperCase())){
					result=false;
				}
			}
		}

		return result;
	}
}
