package com.example;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.example.entity.VedioInfo;
import com.example.exception.ScanFilesException;
import com.example.service.VedioService;


@SpringBootApplication
public class Sb3Application { 
	//扫描文件获取信息
	public static void main(String[] args) throws ScanFilesException {
		ConfigurableApplicationContext context = SpringApplication.run(Sb3Application.class, args);
		VedioService vedioService = context.getBean(VedioService.class);
		ArrayList<VedioInfo> scanFilesWithNoRecursion = vedioService.scanFilesWithNoRecursion("E:\\BaiduNetdiskDownload\\vedio");
//		ArrayList<VedioInfo> scanFilesWithNoRecursion2 = vedioService.scanFilesWithRecursion("H:\\云南招商\\企业");
//		for (VedioInfo vedioInfo : scanFilesWithNoRecursion2) {
//			System.out.println(vedioInfo.toString());
//		}
		//List<String> videoInfo = VedioService.getVideoInfo("E:\\BaiduNetdiskDownload\\vedio\\1.以太坊web3接口.avi");
		
		//VedioService.getVedioMetaData("E:\\BaiduNetdiskDownload\\vedio\\1.以太坊web3接口.avi");
	}
}
