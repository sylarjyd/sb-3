package com.example.service;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;

import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.constant.VedioConstant;
import com.example.entity.VedioInfo;
import com.example.exception.ScanFilesException;
import com.example.utils.FileSafeCode;
import com.example.utils.RandomUtil;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.ICodec;
@Service
public class VedioService {
	
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 把封装的VedioInfo插入到数据
	 * @param vedioInfo
	 * @return
	 */
	public Integer insertVedioInfo(VedioInfo vedioInfo) {
		int updatCount = jdbcTemplate.update("insert into vedioinfo "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",vedioInfo.getId(),vedioInfo.getName(),vedioInfo.getDescription(),
				vedioInfo.getCite(),vedioInfo.getLicense(),vedioInfo.getFormat(),vedioInfo.getHashtype(),vedioInfo.getHashvalue(),
				vedioInfo.getFile(),vedioInfo.getSize(),vedioInfo.getResolutionx(),vedioInfo.getResolutiony(),vedioInfo.getTags(),
				vedioInfo.getGroup(),vedioInfo.getProvider(),vedioInfo.getProvidedate());
		return updatCount;
	}
	
	
	    private static ArrayList<VedioInfo> scanFiles = new ArrayList<VedioInfo>();  
     
	    /**linkedList实现**/  
	    private static LinkedList<File> queueFiles = new LinkedList<File>();  
	      
	      
	    /** 
	     * TODO:递归扫描指定文件夹下面的指定文件 
	     * @return ArrayList<Object> 
	     * @author 邪恶小先生（LQ） 
	     * @time 2017年11月3日 
	     */  
	    public ArrayList<VedioInfo> scanFilesWithRecursion(String folderPath) throws ScanFilesException{  
	        ArrayList<String> dirctorys = new ArrayList<String>();  
	        File directory = new File(folderPath);  
	        if(!directory.isDirectory()){  
	            throw new ScanFilesException('"' + folderPath + '"' + " input path is not a Directory , please input the right path of the Directory. ^_^...^_^");  
	        }  
	        if(directory.isDirectory()){  
	            File [] filelist = directory.listFiles();  
	            for(int i = 0; i < filelist.length; i ++){  
	                /**如果当前是文件夹，进入递归扫描文件夹**/  
	                if(filelist[i].isDirectory()){  
	                    dirctorys.add(filelist[i].getAbsolutePath());  
	                    /**递归扫描下面的文件夹**/  
	                    scanFilesWithRecursion(filelist[i].getAbsolutePath());  
	                }  
	                /**非文件夹**/  
	                else{  
	                	VedioInfo vedioInfo = createVedioInfo(filelist[i]);
	                    
	                    if(!vedioInfo.getGroup().equals("")) {
                    		scanFiles.add(vedioInfo);  
 	                        insertVedioInfo(vedioInfo);
                    	}
	                }  
	            }  
	        }  
	        return scanFiles;  
	    }  
	      
	    /** 
	     *  
	     * TODO:非递归方式扫描指定文件夹下面的所有文件 
	     * @return ArrayList<Object>  
	     * @param folderPath 需要进行文件扫描的文件夹路径 
	     * @author 邪恶小先生（LQ） 
	     * @time 2017年11月3日 
	     */  
	    public ArrayList<VedioInfo> scanFilesWithNoRecursion(String folderPath) throws ScanFilesException{  
	        File directory = new File(folderPath);  
	        if(!directory.isDirectory()){  
	            throw new ScanFilesException('"' + folderPath + '"' + " input path is not a Directory , please input the right path of the Directory. ^_^...^_^");  
	        }  
	        else{  
	            //首先将第一层目录扫描一遍  
	            File [] files = directory.listFiles();  
	            //遍历扫出的文件数组，如果是文件夹，将其放入到linkedList中稍后处理  
	            for(int i = 0; i < files.length; i ++){  
	                if(files[i].isDirectory()){  
	                    queueFiles.add(files[i]);  
	                }else{  
	                    //暂时将文件名放入scanFiles中  
	                	VedioInfo vedioInfo = createVedioInfo(files[i]);
	                	if(!vedioInfo.getGroup().equals("")) {
                    		scanFiles.add(vedioInfo);  
 	                        insertVedioInfo(vedioInfo);
                    	}
	                   
	                }  
	            }  
	              
	            //如果linkedList非空遍历linkedList  
	            while(!queueFiles.isEmpty()){  
	                //移出linkedList中的第一个  
	                File headDirectory = queueFiles.removeFirst();  
	                File [] currentFiles = headDirectory.listFiles();  
	                for(int j = 0; j < currentFiles.length; j ++){  
	                    if(currentFiles[j].isDirectory()){  
	                        //如果仍然是文件夹，将其放入linkedList中  
	                        queueFiles.add(currentFiles[j]);  
	                    }else{  
	                    	//暂时将文件名放入scanFiles中  
	                    	VedioInfo vedioInfo = createVedioInfo(currentFiles[j]);
	                    	if(!vedioInfo.getGroup().equals("")) {
	                    		scanFiles.add(vedioInfo);  
	 	                        insertVedioInfo(vedioInfo);
	                    	}
	                       
	                    }  
	                }  
	            }  
	        }  
	          
	        return scanFiles;  
	    }  
	    /**
	     * 封装VedioInfo
	     * @param file
	     * @return
	     */
	    public VedioInfo createVedioInfo(File file){
	    	VedioInfo vedioInfo = new VedioInfo(RandomUtil.getRandomId());
        	vedioInfo.setFile(file.getAbsolutePath()==null?"":file.getAbsolutePath());
        	String name =file.getName()==null?"":file.getName();
        	vedioInfo.setName(name);
        	vedioInfo.setSize(file.length()+"");
        	vedioInfo.setHashtype("MD5");
        	String md5Value = "";
			try {
				md5Value = FileSafeCode.getMD5(file);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	vedioInfo.setHashvalue(md5Value);
        	Integer queryMd5Count = queryMd5Count(md5Value);
            //vedioInfo.setGroup(md5Value+(queryMd5Count+1));
        	
        	if(queryMd5Count>0) {
        		vedioInfo.setGroup("");
        	}else {
        		vedioInfo.setGroup(md5Value+"_"+(queryMd5Count+1));
        	}
            vedioInfo.setFormat(name.substring(name.lastIndexOf(".")+1));
            Map<String, String> vedioMetaData = getVedioMetaData(file.getAbsolutePath());
            vedioInfo.setResolutionx(vedioMetaData.get("resolutionx"));
            vedioInfo.setResolutiony(vedioMetaData.get("resolutiony"));
            vedioInfo.setDescription(name);
            vedioInfo.setCite("");
            vedioInfo.setLicense("");
            vedioInfo.setTags("");
            vedioInfo.setProvider(VedioConstant.PROVIDER);
            vedioInfo.setProvidedate(getCurrentDate());
        	return vedioInfo;
	    }
	    
	    /**
	     * 判断文件MD5一致的个数
	     * @param md5Value
	     * @return
	     */
	    public Integer queryMd5Count(String md5Value) {
	    	Integer queryMd5Count = 0;
	    	List<Integer> queryForList = jdbcTemplate.queryForList("select count(1) from vedioinfo where hashvalue = '"+md5Value+"'",Integer.class);
	    	if (null!=queryForList) {
	    		queryMd5Count = queryForList.get(0);
			}
	    	return queryMd5Count;
	    }
	    
	    /**
	     * 通过ffmpeg处理视频的相关信息
	     * @param oldfilePath
	     * @return
	     */
	    public static List<String> getVideoInfo(String oldfilePath) {
	        String ffmpegPath = "C:\\Users\\Administrator\\Desktop\\ffmpeg-20180507-29eb1c5-win64-static\\bin\\ffmpeg.exe";
	        List<String> list = new ArrayList<String>();
	        
	        if (!checkfile(oldfilePath)) {
	              System.out.println(oldfilePath + " is not file");
	              return null;
	          }
	          List<String> commend=new ArrayList<String>();
	          commend.add(ffmpegPath);
	          commend.add("-i");
	          commend.add(oldfilePath);
	          System.out.println(commend.toString());   
	        try {

	            ProcessBuilder builder = new ProcessBuilder();
	            builder.command(commend);
	            builder.redirectErrorStream(true);
	            Process p= builder.start();

	           //1. start
	            BufferedReader buf = null; // 保存ffmpeg的输出结果流
	            String line = null;
	          //read the standard output

	            buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
	           
	            StringBuffer sb= new StringBuffer();
	            while ((line = buf.readLine()) != null) {
	            	//System.out.println("开始");
	                System.out.println(line);
	                //System.out.println("结束");
	                sb.append(line);
	                continue;
	            }
	            int ret = p.waitFor();//这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行
	            //1. end
	            String result = sb.toString();//得到视频流
	            
	            //通过视频流获取视频信息
	            PatternCompiler compiler =new Perl5Compiler();
	            String regexDuration ="Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
	            String regexVideo ="Video: (.*?), (.*?), (.*?)[,\\s]";
	            String regexAudio ="Audio: (\\w*), (\\d*) Hz";
	         
	            Pattern patternDuration = compiler.compile(regexDuration,Perl5Compiler.CASE_INSENSITIVE_MASK);
	            PatternMatcher matcherDuration = new Perl5Matcher();
	            if(matcherDuration.contains(result, patternDuration)){
	                MatchResult re = (MatchResult) matcherDuration.getMatch();
	                list.add(re.group(1));//提取出播放时间
	                list.add(re.group(2));//开始时间
	                list.add(re.group(3));//bitrate 码率 单位 kb
	            }
	             
	            Pattern patternVideo = compiler.compile(regexVideo,Perl5Compiler.CASE_INSENSITIVE_MASK);
	            PatternMatcher matcherVideo = new Perl5Matcher();
	            
	            if(matcherVideo.contains(result, patternVideo)){
	                MatchResult re = (MatchResult) matcherVideo.getMatch();
	                list.add(re.group(1));//编码格式
	                list.add(re.group(2));//视频格式
	                list.add(re.group(3));//分辨率
	                System.out.println("分辨率==========="+re.group(3));
	            }
	             
	            Pattern patternAudio = compiler.compile(regexAudio,Perl5Compiler.CASE_INSENSITIVE_MASK);
	            PatternMatcher matcherAudio = new Perl5Matcher();
	             
	            if(matcherAudio.contains(result, patternAudio)){
	                MatchResult re = (MatchResult) matcherAudio.getMatch();
	                list.add(re.group(1));//音频编码
	                list.add(re.group(2));//音频采样频率
	            }
	        } catch (Exception e) {
	            return null;
	        }
	        return list;
	     }
	    /**
	     * 检测是否是文件
	     * @param oldfilePath
	     * @return
	     */
		private static boolean checkfile(String oldfilePath) {
			boolean isFile = false;
			File file = new File(oldfilePath);
			if (file.isFile()) {
				isFile = true;
			}
			return isFile;
		}

		
		private static String getCurrentDate() {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);
			
		}

		
		public static void main(String[] args) {
			System.out.println(getCurrentDate());
		}
		
		public static Map<String,String> getVedioMetaData(String filename){
			
			Map<String,String> map = new HashMap<>();
			   // first we create a Xuggler container object
	        IContainer container = IContainer.make();

	        // we attempt to open up the container

	        int result = container.open(filename, IContainer.Type.READ, null);

	        // check if the operation was successful

	        if (result<0)

	            throw new RuntimeException("Failed to open media file");

	        // query how many streams the call to open found

	        int numStreams = container.getNumStreams();

	        // query for the total duration

	        long duration = container.getDuration();

	        // query for the file size

	        long fileSize = container.getFileSize();

	        // query for the bit rate

	        long bitRate = container.getBitRate();

	        System.out.println("Number of streams: " + numStreams);

	        System.out.println("Duration (ms): " + duration);

	        System.out.println("File Size (bytes): " + fileSize);

	        System.out.println("Bit Rate: " + bitRate);

	        // iterate through the streams to print their meta data

	        for (int i=0; i<numStreams; i++) {

	            // find the stream object

	            IStream stream = container.getStream(i);

	            // get the pre-configured decoder that can decode this stream;

	            IStreamCoder coder = stream.getStreamCoder();

	            System.out.println("*** Start of Stream Info ***");

	            System.out.printf("stream %d: ", i);

	            System.out.printf("type: %s; ", coder.getCodecType());

	            System.out.printf("codec: %s; ", coder.getCodecID());

	            System.out.printf("duration: %s; ", stream.getDuration());

	            System.out.printf("start time: %s; ", container.getStartTime());

	            System.out.printf("timebase: %d/%d; ",

                stream.getTimeBase().getNumerator(),

                stream.getTimeBase().getDenominator());

	            System.out.printf("coder tb: %d/%d; ",

                coder.getTimeBase().getNumerator(),

                coder.getTimeBase().getDenominator());	       

	            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
	                System.out.printf("sample rate: %d; ", coder.getSampleRate());

	                System.out.printf("channels: %d; ", coder.getChannels());

	                System.out.printf("format: %s", coder.getSampleFormat());

	            }else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {

	                System.out.printf("width: %d; ", coder.getWidth());

	                System.out.printf("height: %d; ", coder.getHeight());

	                System.out.printf("format: %s; ", coder.getPixelType());

	                System.out.printf("frame-rate: %5.2f; ", coder.getFrameRate().getDouble());
	                
	                map.put("resolutionx", coder.getWidth()+"");
	                map.put("resolutiony", coder.getHeight()+"");

	            }

	            System.out.println("*** End of Stream Info ***");

	            }
	        
	        	return map;
	    	}

}
