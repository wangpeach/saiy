package com.wly.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("unused")
public class FileOperate {

	/**
	 * 保存文件
	 * 
	 * @param file
	 * @param fileName
	 * @param filePath
	 * @param fileType
	 * @return
	 */
	public static String saveFile(File file, String fileName, String filePath,
			String fileType) {
		String path = "";
		try {
			InputStream in = new FileInputStream(file);
			path = saveFile(in, fileName, filePath, fileType);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * 创建新文件
	 * @param content
	 * @param fullPath
	 * @return
	 */
	public static void createNewFile(String content, String fullPath) {
		File file = new File(fullPath);
		try {
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String saveFile(InputStream in, String fileName,
			String filePath, String fileType) {
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String path = dir.getPath();
		path = path + "/" + fileName + "." + fileType;
		holdDisk(in, path);
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}

	private static boolean holdDisk(InputStream inputStream, String filePath) {
		try {
			File file = new File(filePath);
			OutputStream output = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) >= 0) {
				output.write(bytes, 0, read);
			}
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static InputStream getStream(String path, boolean close) {
		InputStream stream = null;
		File file = new File(path);
		if(file.exists()) {
			try {
				stream = new FileInputStream(file);
				if(close) {
					stream.close();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return stream;
	}
	
	public static List<InputStream> getFileStream(List<File> file) {
		List<InputStream> streams = new ArrayList<InputStream>();
		InputStream stream = null;
		try {
			for (File item : file) {
				stream = new FileInputStream(item);
				streams.add(stream);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return streams;
	}
	
	public static boolean isExists(String path) {
		File file = new File(path);
		if(file.exists()) {
			return true;
		}
		return false;
	}
	
	public static File getFile(String path) {
		File file = new File(path);
		if(file.exists()) {
			return file;
		}
		return null;
	}

	public static void moveFile(String filePath, String toBasePath) {
		File dir = new File(toBasePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(filePath);
		file.renameTo(new File(dir, file.getName()));
	}
	
	/**
	 * 移动文件
	 * 
	 * @param filePath
	 *            原始文件路径
	 * @param toBasePath
	 *            移动到指定目录下
	 */
	public static void moveFile(List<String> filePath, String toBasePath) {
		File dir = new File(toBasePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		for (String item : filePath) {
			File file = new File(item);
			file.renameTo(new File(dir, file.getName()));
		}
	}
	
	/**
	 * 移动文件 并且重命名
	 * 
	 * @param filePath
	 *            原始文件路径
	 * @param toBasePath
	 *            移动到指定目录下
	 */
	public static void moveFileAndRename(Map<String,String> filePath, String toBasePath) {
		File dir = new File(toBasePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String iteml=null;
		for (String item : filePath.keySet()) {
			iteml=UUID.randomUUID().toString()+(item.substring(item.lastIndexOf("."), item.length()));
			
			fileChannelFile(item,iteml);
			
			File file = new File(iteml);
			file.renameTo(new File(dir, filePath.get(item)));
		}
	}
	
	public static void deleteFiles(List<String> path) {
		for (String item : path) {
			deleteFile(item);
		}
	}
	
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		deleteFile(file);
	}
	
	 private static boolean deleteFile(File file){  
        File[] files = file.listFiles();  
        if(files != null && files.length > 0) {
        	for(File deleteFile : files){  
                if(deleteFile.isDirectory()){  
                    //如果是文件夹，则递归删除下面的文件后再删除该文件夹  
                    if(!deleteFile(deleteFile)){  
                        //如果失败则返回  
                        return false;  
                    }  
                } else {  
                    if(!deleteFile.delete()){  
                        //如果失败则返回  
                        return false;  
                    }  
                }  
            }  
        }
        return file.delete();  
    } 
	
	public static boolean createPath(String path) {
		boolean result = false;
		File file = new File(path);
		if(!file.exists()) {
			 result = file.mkdirs();
		} else {
			result = true;
		}
		return result;
	}
	
	/**
	 * 高速通道复制文件
	 * @param rawPath 原始路径
	 * @param targetPath 目标路径
	 */
	@SuppressWarnings("resource")
	public static void fileChannelFile(String rawPath, String targetPath) {	
		File file = new File(rawPath);
		if(file.exists()) {
			try {
				
				FileInputStream fis = new FileInputStream(rawPath);
				FileOutputStream fos = new FileOutputStream(targetPath);
				FileChannel in = fis.getChannel();
				FileChannel out = fos.getChannel();
				try {
					in.transferTo(0, in.size(), out);
					out.close();
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将源文件/文件夹生成指定格式的压缩文件,格式zip
	 * @param resourcesPath 源文件/文件夹
	 * @param targetPath 目的压缩文件保存路径
	 * @return
	 * @throws Exception
	 */
    public static String compressedFile(String resourcesPath,String targetPath) throws Exception{  
        File resourcesFile = new File(resourcesPath);     //源文件  
        File targetFile = new File(targetPath);           //目的  
        //如果目的路径不存在，则新建  
        if(!targetFile.exists()){       
            targetFile.mkdirs();    
        }  
          
        String targetName = resourcesFile.getName()+".zip";   //目的压缩文件名  
        String targetZip = targetPath+"\\"+targetName;
        File zipexists = new File(targetZip);
        if(zipexists.exists()) {
        	zipexists.delete();
        }
        FileOutputStream outputStream = new FileOutputStream(targetZip);  
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(outputStream));  
        
        createCompressedFile(out, resourcesFile, "");  
          
        out.close();
        return targetZip;
    }  
      
    /** 
     * @desc 生成压缩文件。 
     *               如果是文件夹，则使用递归，进行文件遍历、压缩 
     *       如果是文件，直接压缩 
     * @param out  输出流 
     * @param file  目标文件 
     * @return void 
     * @throws Exception  
     */  
    private static void createCompressedFile(ZipOutputStream out,File file,String dir) throws Exception{  
        //如果当前的是文件夹，则进行进一步处理  
        if(file.isDirectory()){  
            //得到文件列表信息  
            File[] files = file.listFiles();  
            //将文件夹添加到下一级打包目录  
            out.putNextEntry(new ZipEntry(dir+"/"));  
            
            dir = dir.length() == 0 ? "" : dir +"/";  
              
            //循环将文件夹中的文件打包  
            for(int i = 0 ; i < files.length ; i++){
                createCompressedFile(out, files[i], dir + files[i].getName());         //递归处理  
            }  
        }  
        else{   //当前的是文件，打包处理  
            //文件输入流  
            FileInputStream fis = new FileInputStream(file);
            out.putNextEntry(new ZipEntry(dir));  
            //进行写操作  
            int j =  -1;  
            byte[] buffer = new byte[1024];  
            while ((j = fis.read(buffer)) != -1) {
            	out.write(buffer, 0, j);
		     }
            //关闭输入流  
            fis.close();  
        }  
    }  
	
    //获取项目完整路径
	public static String getRootPath(String cut) {
//		return ServletActionContext.getServletContext().getRealPath("/");
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		return path.substring(0, path.indexOf(cut) + cut.length() + 1);
	}

	/**
	 * 图片压缩工具类 提供的方法中可以设定生成的 缩略图片的大小尺寸、压缩尺寸的比例、图片的质量等
	 * 
	 * <pre>
	 * 	调用示例：
	 * resiz(srcImg, tarDir + "car_1_maxLength_11-220px-hui.jpg", 220, 0.7F);
	 * </pre>
	 * 
	 * @project haohui-b2b
	 * @author cevencheng
	 * @create 2012-3-22 下午8:29:01
	 */
	public static class ImageUtil {

		public static InputStream Tosmallerpic(String f, File filelist,
				String ext, String n, int w, int h, float per) {
			Image src;
			InputStream in = null;
			try {
				src = ImageIO.read(filelist); // 构造Image对象

				String img_midname = f + n.substring(0, n.indexOf(".")) + ext
						+ n.substring(n.indexOf("."));
				int old_w = src.getWidth(null); // 得到源图宽
				int old_h = src.getHeight(null);
				int new_w = 0;
				int new_h = 0; // 得到源图长

				double w2 = (old_w * 1.00) / (w * 1.00);
				double h2 = (old_h * 1.00) / (h * 1.00);

				// 图片跟据长宽留白，成一个正方形图。
				BufferedImage oldpic;
				if (old_w > old_h) {
					oldpic = new BufferedImage(old_w, old_w,
							BufferedImage.TYPE_INT_RGB);
				} else {
					if (old_w < old_h) {
						oldpic = new BufferedImage(old_h, old_h,
								BufferedImage.TYPE_INT_RGB);
					} else {
						oldpic = new BufferedImage(old_w, old_h,
								BufferedImage.TYPE_INT_RGB);
					}
				}
				Graphics2D g = oldpic.createGraphics();
				g.setColor(Color.white);
				if (old_w > old_h) {
					g.fillRect(0, 0, old_w, old_w);
					g.drawImage(src, 0, (old_w - old_h) / 2, old_w, old_h,
							Color.white, null);
				} else {
					if (old_w < old_h) {
						g.fillRect(0, 0, old_h, old_h);
						g.drawImage(src, (old_h - old_w) / 2, 0, old_w, old_h,
								Color.white, null);
					} else {
						// g.fillRect(0,0,old_h,old_h);
						g.drawImage(src.getScaledInstance(old_w, old_h,
								Image.SCALE_SMOOTH), 0, 0, null);
					}
				}
				g.dispose();
				src = oldpic;
				// 图片调整为方形结束
				if (old_w > w)
					new_w = (int) Math.round(old_w / w2);
				else
					new_w = old_w;
				if (old_h > h)
					new_h = (int) Math.round(old_h / h2);// 计算新图长宽
				else
					new_h = old_h;
				BufferedImage tag = new BufferedImage(new_w, new_h,
						BufferedImage.TYPE_INT_RGB);
				// tag.getGraphics().drawImage(src,0,0,new_w,new_h,null);
				// //绘制缩小后的图
				tag.getGraphics()
						.drawImage(
								src.getScaledInstance(new_w, new_h,
										Image.SCALE_SMOOTH), 0, 0, null);
				// FileOutputStream newimage = new
				// FileOutputStream(img_midname); // 输出到文件流
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				// JPEGImageEncoder encoder = JPEGCodec
				// .createJPEGEncoder(newimage);
				// JPEGEncodeParam jep =
				// JPEGCodec.getDefaultJPEGEncodeParam(tag);
				//
				// jep.setQuality(per, true);
				// encoder.encode(tag, jep);
				ImageIO.write(tag, "", os);
				in = new ByteArrayInputStream(os.toByteArray());
				// encoder.encode(tag); //近JPEG编码
				// newimage.close();
			} catch (IOException ex) {
			}
			return in;
		}

		/**
		 * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		 * 
		 * @param imgFilePath
		 *            图片路径
		 * @return String
		 */
		public static String toBase64(String imgFilePath) {
			byte[] data = null;
			// 读取图片字节数组
			try {
				InputStream in = new FileInputStream(imgFilePath);
				data = new byte[in.available()];
				in.read(data);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 对字节数组Base64编码
			BASE64Encoder encoder = new BASE64Encoder();
			String strData = encoder.encode(data);// 返回Base64编码过的字节数组字符串
			String type = imgFilePath.substring(
					imgFilePath.lastIndexOf(".") + 1, imgFilePath.length());
			strData = "data:image/" + type + ";base64," + strData;
			return strData;
		}

		/**
		 * 对字节数组字符串进行Base64解码并生成图片
		 * 
		 * @param imgStr
		 *            Base64字符串
		 * @param imgFilePath
		 *            生成图片保存路径
		 */
		public static void generateImage(String imgStr, String imgFilePath,String fileName,String fileType) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				File dir = new File(imgFilePath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				String path = dir.getPath();
				path = path + "\\" + fileName + "." + fileType;
				// Base64解码
				byte[] bytes = decoder.decodeBuffer(imgStr);
				File file = new File(path);
				OutputStream out = new FileOutputStream(file);
				
				out.write(bytes);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public static InputStream generateImage(String base64) {
			InputStream in = null;
			BASE64Decoder decoder = new BASE64Decoder();
			// Base64解码
			try {
				byte[] bytes = decoder.decodeBuffer(base64);
				ByteArrayOutputStream  out = new ByteArrayOutputStream();
				out.write(bytes);
				in = new ByteArrayInputStream(out.toByteArray());
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return in;
		}

		/**
		 * 对字节数组字符串进行Base64解码并生成图片
		 * 
		 * @param imgStr
		 *            图片字符串
		 * @return byte[]
		 */
		public static byte[] getStrToBytes(String imgStr) {
			if (imgStr == null) // 图像数据为空
				return null;
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				// Base64解码
				byte[] bytes = decoder.decodeBuffer(imgStr);
				for (int i = 0; i < bytes.length; ++i) {
					if (bytes[i] < 0) {// 调整异常数据
						bytes[i] += 256;
					}
				}
				// 生成jpeg图片
				return bytes;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public static class videoConvert {
		public static void main(String[] args) {
			videoConvert.convert("F:\\workspace\\eclipse\\Luna\\DocSupervise\\WebContent\\WEB-INF\\plugin\\", "D:/resource/路径怎么了/video/original/20150209112101979.rmvb", "D:/resource/20150209112101979.mp4",
					"D:\\resource\\20150209112101979.jpg");
		}

		/**
		 * 功能函数
		 * 
		 * @param inputFile
		 *            待处理视频，需带路径
		 * @param outputFile
		 *            处理后视频，需带路径
		 * @return
		 */
		public static boolean convert(String plugin, String inputFile, String outputFile,
				String newimg) {
			if (!checkfile(inputFile)) {
				return false;
			}
			String outpath = outputFile.substring(0, outputFile.lastIndexOf("\\"));
			checkpath(outpath);
			if (process(plugin, inputFile, outputFile, newimg)) {
				return true;
			}
			return false;
		}

		// 检查文件是否存在
		private static boolean checkfile(String path) {
			File file = new File(path);
			if (!file.isFile()) {
				return false;
			}
			return true;
		}
		
		private static void checkpath(String path) {
			File file = new File(path);
			if(!file.exists()) {
				file.mkdirs();
			}
		}

		/**
		 * 转换过程 ：先检查文件类型，在决定调用 processFlv还是processAVI
		 * 
		 * @param inputFile
		 * @param outputFile
		 * @return
		 */
		private static boolean process(String plugin, String inputFile, String outputFile,
				String newimg) {
			int type = checkContentType(inputFile);
			boolean status = false;
			if (type == 0) {
				String inputType = inputFile.substring(
						inputFile.lastIndexOf(".") + 1, inputFile.length())
						.toLowerCase();
					status = processMP4(plugin, inputFile, outputFile, newimg);// 直接将文件转为flv文件
					processImg(plugin, inputFile, newimg);
			} else if (type == 1) {
				
				String avifilepath = processAVI(plugin, type, inputFile);
				if (avifilepath == null)
					return false;// avi文件没有得到
				status = processMP4(plugin, avifilepath, outputFile, newimg);// 将avi转为flv
				processImg(plugin, outputFile, newimg);
				FileOperate.deleteFile(avifilepath);
			}
			return status;
		}

		/**
		 * 检查视频类型
		 * 
		 * @param inputFile
		 * @return ffmpeg 能解析返回0，不能解析返回1
		 */
		private static int checkContentType(String inputFile) {
			String type = inputFile.substring(inputFile.lastIndexOf(".") + 1,
					inputFile.length()).toLowerCase();
			// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
			if (type.equals("avi")) {
				return 0;
			} else if (type.equals("avi")) {
				return 0;
			} else if (type.equals("wmv")) {
				return 0;
			} else if (type.equals("3gp")) {
				return 0;
			} else if (type.equals("mov")) {
				return 0;
			} else if (type.equals("mp4")) {
				return 0;
			} else if (type.equals("asf")) {
				return 0;
			} else if (type.equals("asx")) {
				return 0;
			} else if (type.equals("flv")) {
				return 0;
			}
			// 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
			// 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
			else if (type.equals("wmv9")) {
				return 1;
			} else if (type.equals("rm")) {
				return 1;
			} else if (type.equals("rmvb")) {
				return 1;
			}
			return 9;
		}

		/**
		 * ffmepg: 能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
		 * 
		 * @param inputFile
		 * @param outputFile
		 * @return
		 */
		private static boolean processMP4(String plugin, String inputFile, String outputFile,
				String newimg) {
			if (!checkfile(inputFile)) {
				System.out.println(inputFile + " is not file");
				return false;
			}

			List<String> commend = new ArrayList<String>();
			commend.add(plugin + "ffmpeg");
			commend.add("-i");
			commend.add(inputFile);
			commend.add("-c:v");
			commend.add("libx264");
			commend.add("-crf");
			commend.add("19");
			commend.add("-preset");
			commend.add("slow");
			commend.add("-c:a");
			commend.add("aac");
			commend.add("-strict");
			commend.add("experimental");
			commend.add("-b:a");
			commend.add("192k");
			commend.add("-ac");
			commend.add("2");
			commend.add(outputFile);
			StringBuffer test = new StringBuffer();
			for (int i = 0; i < commend.size(); i++)
				test.append(commend.get(i) + " ");
			System.out.println(test);
			try {
				Process process = new ProcessBuilder(commend).redirectErrorStream(true).start();
				final InputStream in = process.getInputStream();
				final InputStream error = process.getErrorStream();
				new Thread(){
					public void run() {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(in));
						try {
							String lineIn = null;
							while ((lineIn = br.readLine()) != null) {
								if (lineIn != null)
									System.out.println(lineIn);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
				new Thread() {
					public void run() {
						BufferedReader br2 = new BufferedReader(
								new InputStreamReader(error));
						try {
							String lineErr = null;
							while ((lineErr = br2.readLine()) != null) {
								if (lineErr != null)
									System.out.println(lineErr);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
				process.waitFor();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		/**
		 * Mencoder: 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
		 * 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
		 * 
		 * @param type
		 * @param inputFile
		 * @return
		 */
		private static String processAVI(String plugin, int type, String inputFile) {
			String name = inputFile.substring(0, inputFile.lastIndexOf("."))
					+ ".avi";
			File file = new File(name);
			if (file.exists())
				file.delete();
			List<String> commend = new ArrayList<String>();
			commend.add(plugin + "mencoder");
			commend.add(inputFile);
			commend.add("-oac");
			commend.add("mp3lame");
			commend.add("-lameopts");
			commend.add("preset=64");
			commend.add("-ovc");
			commend.add("xvid");
			commend.add("-xvidencopts");
			commend.add("bitrate=1000");
			commend.add("-of");
			commend.add("avi");
			commend.add("-o");
			commend.add(name);

			StringBuffer test = new StringBuffer();
			for (int i = 0; i < commend.size(); i++)
				test.append(commend.get(i) + " ");
			System.out.println(test);
			try {
				ProcessBuilder builder = new ProcessBuilder();
				builder.command(commend);
				Process p = builder.start();
				/**
				 * 清空Mencoder进程 的输出流和错误流 因为有些本机平台仅针对标准输入和输出流提供有限的缓冲区大小，
				 * 如果读写子进程的输出流或输入流迅速出现失败，则可能导致子进程阻塞，甚至产生死锁。
				 */
				final InputStream is1 = p.getInputStream();
				final InputStream is2 = p.getErrorStream();
				new Thread() {
					public void run() {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is1));
						try {
							String lineB = null;
							while ((lineB = br.readLine()) != null) {
								if (lineB != null)
									System.out.println(lineB);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
				new Thread() {
					public void run() {
						BufferedReader br2 = new BufferedReader(
								new InputStreamReader(is2));
						try {
							String lineC = null;
							while ((lineC = br2.readLine()) != null) {
								if (lineC != null)
									System.out.println(lineC);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();

				// 等Mencoder进程转换结束，再调用ffmepg进程
				p.waitFor();
				return name;
			} catch (Exception e) {
				System.err.println(e);
				return null;
			}
		}

		/**
		 * 截图
		 * 
		 * @param inputFile
		 * @param newimg
		 */
		public static void processImg(String plugin, String inputFile, String newimg) {
			List<String> commend = new ArrayList<String>();
			commend.add(plugin + "ffmpeg");
			commend.add("-ss");
			commend.add("00:00:10");
			commend.add("-i");
			commend.add(inputFile);
			commend.add(newimg);
			commend.add("-r");
			commend.add("1");
			commend.add("-vframes");
			commend.add("1");
			commend.add("-an");
			commend.add("-vcodec");
			commend.add("mjpeg");
//			commend.add("-i");
//			commend.add(inputFile);
//			commend.add("-y");
//			commend.add("-f");
//			commend.add("image2");
//			commend.add("-ss");
//			commend.add("10");
//			commend.add("-t");
//			commend.add("0.001");
//			commend.add("-s");
//			commend.add("100x100");

			StringBuffer test = new StringBuffer();
			for (int i = 0; i < commend.size(); i++)
				test.append(commend.get(i) + " ");
			System.out.println("图片生成命令：" + test.toString());
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			try {
				builder.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取磁盘信息
	 * @return
	 */
	/*public static List<FileBean> getDriveInfo() {
		List<FileBean> filebean = new ArrayList<FileBean>();
		File[] roots = File.listRoots();//获取磁盘分区列表     
        for (File file : roots) {
        	if(file.getTotalSpace() > 0) {
        		FileBean bean = new FileBean();
            	bean.setDrive(file.getPath().substring(0, 1));
            	bean.setTotalSpace(file.getTotalSpace()/1024/1024/1024+"G");
            	bean.setSurplusSpace(file.getFreeSpace()/1024/1024/1024+"G");
            	bean.setUsedSpace((file.getTotalSpace() - file.getFreeSpace())/1024/1024/1024+"G");
            	filebean.add(bean);
        	}
        }
        return filebean;
	}*/
}
