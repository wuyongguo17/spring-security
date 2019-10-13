package com.imooc.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imooc.dto.FileInfo;

@RestController
@RequestMapping("/file")
public class FileController {
	private String folder = "F:\\JavaEE\\springSecurity\\imooc.security.dome\\src\\main\\java\\com\\imooc\\web\\controller";
	
	//文件上传
	@PostMapping
	public FileInfo upload(MultipartFile file) throws Exception {
		System.out.println(file.getName());
		System.out.println(file.getOriginalFilename());
		System.out.println(file.getSize());
		
		//上传文件写入到的文件
		
		File localFile = new File(folder,new Date().getTime() + ".txt");
		file.transferTo(localFile);
		
		return new FileInfo(localFile.getAbsolutePath());
	}
	
	//文件的下载
	@GetMapping("/{id}")
	public void download(HttpServletRequest request,HttpServletResponse response,@PathVariable String id) throws IOException {
		//jdk7的新语法，写在try括号里，jdk会自动关闭流
		try(FileInputStream inputStream = new FileInputStream(new File(folder,id+".txt"));
				ServletOutputStream outputStream = response.getOutputStream();){
			response.setContentType("application/x-download");
			response.addHeader("Content-Disposition", "attachment;filename=test.txt");
			//将inputStream中的内容写到outputStream中，实际上就是下载
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();
		}
	}
}
