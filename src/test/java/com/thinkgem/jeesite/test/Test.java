package com.thinkgem.jeesite.test;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {

	public static void main(String[] args) {
		String path = "F:\\video__todo\\尚硅谷Mybatis视频教程\\视频4";
		File file = new File(path);
		System.out.println("获取文件地址："+path);
		File[] files = file.listFiles();
		double second = 0L;
		for(File f : files ){
			Encoder encoder = new Encoder();
			try {
				MultimediaInfo m = encoder.getInfo(f);
				long ls = m.getDuration();
				second += ls/1000;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		double h = second/3600;
		System.out.println("计算结束-->");
		System.out.println("此视频时长为:" + h + "小时！");
	}

	private ThreadPoolExecutor threadPoolExecutor = null;

	@Override
	protected  void finalize() throws Throwable{
		super.finalize();
		if(null != threadPoolExecutor){
			threadPoolExecutor.shutdown();
			threadPoolExecutor = null;
		}
	}

	@PostConstruct
	public void run(){
		threadPoolExecutor = new ThreadPoolExecutor(2,
				5,
				30,
				TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(300));
		//testMethod();
	}

/*	private void testMethod(){
		System.out.println("testMethod----->");
		// 启动线程调用方法
		threadPoolExecutor.execute();
	}*/

	/*class aa{
		void aa(){
			System.out.println("aa类");
		}
	}
*/
}
