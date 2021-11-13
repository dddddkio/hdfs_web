package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;


@CrossOrigin
@RestController
@RequestMapping("/")

public class HDFSAPI {

	@RequestMapping
	Object hello() {
		return "hello java web";
	}
	
	@RequestMapping("/hdfs")
	Object listHDFSHolder(String path) throws Exception {
		 Configuration configuration = new Configuration();
	     java.net.URI uri = new URI("hdfs://127.0.0.1:8020"); // core-site.html 中配置的端口
	     FileSystem fs = FileSystem.get(uri, configuration, "hadoop");
	     FileStatus[] files = fs.listStatus(new Path(path));

	     Map<String, Boolean> fileMap = new HashMap<String, Boolean>();
	     for (FileStatus file : files) {
	        fileMap.put(file.getPath().getName(), file.isFile());
	     }
	     
	     fs.close();
	     
	     return fileMap;
	}
	
	// 创建文件夹
	@RequestMapping("/mkdir")
	Object mkdirHDFS(String absolutePath) throws Exception {
		 Configuration configuration = new Configuration();
	     java.net.URI uri = new URI("hdfs://127.0.0.1:8020"); // core-site.html 中配置的端口
	     FileSystem fs = FileSystem.get(uri, configuration, "ubuntu");
	     
	     System.out.println(absolutePath);
	     if (fs.exists(new Path(absolutePath))) {
	         return "Directory already exists";
	     } else {
	         fs.mkdirs(new Path(absolutePath));
	         return absolutePath + " created successfully";
	     }
	}
	
	// 上传文件 文件名称若已存在问题未解决
	@RequestMapping("/uploadFile")
	Object uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("dstString") String dstString) throws Exception {
		Configuration configuration = new Configuration();
	    java.net.URI uri = new URI("hdfs://127.0.0.1:8020"); // core-site.html 中配置的端口
	    FileSystem fs = FileSystem.get(uri, configuration, "ubuntu");
        if (file.isEmpty()) {
            return "false";
        }
        String fileName = file.getOriginalFilename();
        String tempFolder = "/home/ubuntu/Code/tempFolder";
        File dest = new File(new File(tempFolder)+ "/" + fileName);
        
        if (!dest.getParentFile().exists()) { 
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest); // 保存文件

        Path src = new Path(tempFolder+"/"+fileName);
        Path dst = new Path(dstString+"/"+fileName);
//	    Path src = new Path((srcString));
//	    Path dst = new Path((dstString));
//	   
	    fs.copyFromLocalFile(src, dst);
	    fs.close();
	    
	    return "Upload file successfully";
	}
	
	// 下载文件
	@RequestMapping("/downloadFile")
	Object downloadFile(String srcString,String dstString) throws Exception {
		Configuration configuration = new Configuration();
	    java.net.URI uri = new URI("hdfs://127.0.0.1:8020"); // core-site.html 中配置的端口
	    FileSystem fs = FileSystem.get(uri, configuration, "ubuntu");
	    
	    Path src = new Path((srcString));
	    Path dst = new Path((dstString));
	   
	    fs.copyToLocalFile(src, dst);
	    fs.close();
	    
	    return "Download file successfully";
	}
	
	// 列出该目录下所有文件
	@RequestMapping("/listFile")
	Object listFile(String folder) throws Exception {
		Configuration configuration = new Configuration();
	    java.net.URI uri = new URI("hdfs://127.0.0.1:8020"); // core-site.html 中配置的端口
	    FileSystem fs = FileSystem.get(uri, configuration, "ubuntu");
	    
        FileStatus[] statuses = fs.listStatus(new Path(folder));
        
        List<HashMap<String, String>> table = new ArrayList<HashMap<String, String>>();
        
        for (FileStatus file : statuses){
        	HashMap<String, String> map = new HashMap<String,String>();
            String isDir = file.isDirectory() ? "Folder" : "File";
            String size = String.valueOf(file.getLen());
            String name = file.getPath().getName();
            map.put("isDir",isDir);
            map.put("size", size);
            map.put("name", name);
            table.add(map);
        }
        System.out.println(1);
	    return table;
	}
	
	// 删除文件
	@RequestMapping("/removeFile")
	Object removeFile(String path) throws Exception {
		Configuration configuration = new Configuration();
	    java.net.URI uri = new URI("hdfs://127.0.0.1:8020"); // core-site.html 中配置的端口
	    FileSystem fs = FileSystem.get(uri, configuration, "ubuntu");

	    fs.delete(new Path((path)),true);
	    fs.close();
		
		return "Remove folder/file successfully - " + path;
	}
	
	
	
}
