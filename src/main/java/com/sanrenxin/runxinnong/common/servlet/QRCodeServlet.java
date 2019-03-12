package com.sanrenxin.runxinnong.common.servlet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sanrenxin.runxinnong.common.entity.ImageBean;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 生成二维码
 * @author LiuDeHua
 * @version 2016-3-11
 */
@SuppressWarnings("serial")
public class QRCodeServlet extends HttpServlet {

    private static int width = 600;              //二维码宽度
    private static int height = 600;             //二维码高度
    private static int onColor = 0xFF000000;     //前景色
    private static int offColor = 0xFFFFFFFF;    //背景色
    private static int margin = 1;               //白边大小，取值范围0~4
    private static ErrorCorrectionLevel level = ErrorCorrectionLevel.L;  //二维码容错率

	public static final String VALIDATE_CODE = "validateCode";
	
	private static Logger log = LoggerFactory.getLogger(QRCodeServlet.class);
	
	private int w = 320;
	private int h = 320;
	
	public QRCodeServlet() {
		super();
	}
	
	@Override
    public void destroy() {
		super.destroy(); 
	}
	
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("开始生成二维码....");
		createImage(request,response);
	}
	
	private void createImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		try {
			BitMatrix bitMatrix = null;
			BufferedImage image = null;
			
			String qrContent = request.getParameter("qrContent");
			
			if(StringUtils.isNotEmpty(qrContent)){
				bitMatrix = new MultiFormatWriter().encode(qrContent, BarcodeFormat.QR_CODE, w, h, hints);
			}

			if(bitMatrix != null){
				image = MatrixToImageWriter.toBufferedImage(bitMatrix);
			}
			
			if(image != null){
				OutputStream out = response.getOutputStream();
				ImageIO.write(image, "JPEG", out);
				
				out.flush();
				out.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param bim	背景图片
	 * @param qrCodePic	二维码图片
	 * @param url 生成后保存的路径
	 * @param FileName 生成后保存的文件名
	 * @return
	 */
	
	public String addBackground_QRCode(BufferedImage bim, ImageBean imageBean, String url, String FileName){
		String PathName=url+FileName+".png";
		try {
	         // 读取二维码图片，并构建绘图对象
	        BufferedImage bgImage = bim;
	        Graphics2D g = bgImage.createGraphics();
	        /**
	         * 读取背景图片
	         */
	        log.debug("开始处理背景图片");
			BufferedImage image = ImageIO.read(new File(imageBean.getImageUrl()));
			 /**
             * 设置要添加图片的大小，如果大于背景图大小用背景图的90%，否则取原图大小
             */
			System.out.println("QRCoder width:"+image.getWidth(null)+"  :hight:"+image.getHeight(null));
			 int widthImage = imageBean.getImageWidth() >bgImage.getWidth()?(bgImage.getWidth()*9/10): imageBean.getImageWidth(), 
			     heightImage = imageBean.getImageHeight() >bgImage.getHeight()?(bgImage.getHeight()*9/10): imageBean.getImageHeight();

            /**
             * 处理被放置图片的存放坐标
             * 如果有定点位置且定点位置+图片的大小不超过背景图片大小用定点位置
             * 否则用居中放置
             */
			     int x,y;
			     int sumWidth =	imageBean.getImageX() + widthImage;
			     int sumHeight = imageBean.getImageY() + heightImage;
			     if(imageBean.getImageX() != 0 && imageBean.getImageY() != 0 && sumWidth <= bgImage.getWidth() && sumHeight <= bgImage.getHeight()){
			    	 x=imageBean.getImageX();
			    	 y=imageBean.getImageY();
			     }else{
			    	 x = (bgImage.getWidth() - widthImage) / 2;
			         y = (bgImage.getHeight() - heightImage) / 2;
			     }
			     log.debug("结束处理背景图片的处理");
			     //处理微信头像
			     if(!String.valueOf(imageBean.getHeadWidth()).equals("0")){
				     log.debug("开始处理要绘制的微信头像内容");
				     BufferedImage imageHead = ImageIO.read(new File(imageBean.getHeadUrl()));
				     int headX,headY,headwidth,headHeight;
				     //如果微信头像长、宽大于背景图片长、宽取背景的1/2
				     headwidth = imageBean.getHeadWidth() >bgImage.getWidth()?(bgImage.getWidth()*1/2): imageBean.getHeadWidth();
				     headHeight = imageBean.getHeadHeight() >bgImage.getHeight()?(bgImage.getHeight()*1/2): imageBean.getHeadHeight();
				     //如果微信头像放置的位置和图片大小超过背景范围，则默认将微信图片放置在(0,0)位置。
				     headX = imageBean.getHeadX();
				     headY = imageBean.getHeadY();
				     int sumHeadWidth = headwidth + headX;
				     int sumHeadHeight = headHeight + headY;
				     if(sumHeadWidth < bgImage.getWidth() && sumHeadHeight < bgImage.getHeight() ){
				    	 headX = imageBean.getHeadX();
					     headY = imageBean.getHeadY();
				     }else{
				    	 headX = 0;
					     headY = 0;
				     }
				     log.debug("结束处理要绘制的微信头像内容");
				     g.drawImage(imageHead, headX, headY, headwidth, headHeight, null);
				}
			     //处理微信昵称
			     if(!String.valueOf(imageBean.getNickWidth()).equals("0")){
				     log.debug("开始处理及绘制的微信昵称");
				     g.setColor(Color.BLACK); 
				     g.setFont(new Font("宋体",Font.BOLD,15)); //字体、字型、字号 
				     int nickX,nickY;
				     //如果微信昵称长、宽大于背景图片长、宽取背景的1/2
				     int strWidth = g.getFontMetrics().stringWidth(imageBean.getNickName());
				     nickX = imageBean.getNickX();
				     nickY = imageBean.getNickY();
				     if(strWidth>imageBean.getNickWidth()){
				    	 String nick1 = imageBean.getNickName().substring(0,imageBean.getNickName().length()/2);
				    	 String nick2 = imageBean.getNickName().substring(imageBean.getNickName().length()/2,imageBean.getNickName().length());
				    	 g.drawString(nick1,nickX , nickY+15);
				    	 g.drawString(nick2,nickX , nickY+35);
				     }else{
				    	 g.drawString(imageBean.getNickName(),nickX , nickY+15);
				     }
				     log.debug("结束处理及绘制的微信昵称");
			     }
			 //开始绘制图片
			  log.debug("开始绘制背景图像及微信头像");
             g.drawImage(image, x, y, widthImage, heightImage, null);
             g.dispose();
             image.flush();
             bgImage.flush();
             log.debug("结束绘制背景图像及微信头像");
             File filePath = new File(url);
             if(!filePath.exists()){
                 filePath.mkdirs();
             }
             ImageIO.write(bgImage, "png", new File(PathName)); 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return PathName;
	}
	
	 /**
     * 给二维码图片添加Logo
     *
     * @param logoPic
     */
    public String addLogo_QRCode(BufferedImage bim, File logoPic, LogoConfig logoConfig, String productName)
    {
        try
        {
            /**
             * 读取二维码图片，并构建绘图对象
             */
            BufferedImage image = bim;
            Graphics2D g = image.createGraphics();

            /**
             * 读取Logo图片
             */
            BufferedImage logo = ImageIO.read(logoPic);
            /**
             * 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
             */
            int widthLogo = logo.getWidth(null)>image.getWidth()*3/10?(image.getWidth()*3/10):logo.getWidth(null), 
                heightLogo = logo.getHeight(null)>image.getHeight()*3/10?(image.getHeight()*3/10):logo.getWidth(null);

            /**
             * logo放在中心
             */
             int x = (image.getWidth() - widthLogo) / 2;
             int y = (image.getHeight() - heightLogo) / 2;
             /**
             * logo放在右下角
             *  int x = (image.getWidth() - widthLogo);
             *  int y = (image.getHeight() - heightLogo);
             */

            //开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
//            g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
//            g.setStroke(new BasicStroke(logoConfig.getBorder()));
//            g.setColor(logoConfig.getBorderColor());
//            g.drawRect(x, y, widthLogo, heightLogo);
            g.dispose();

            //把商品名称添加上去，商品名称不要太长哦，这里最多支持两行。太长就会自动截取啦
            if (productName != null && !productName.equals("")) {
                //新的图片，把带logo的二维码下面加上文字
                BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D outg = outImage.createGraphics();
                //画二维码到新的面板
                outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                //画文字到新的面板
                outg.setColor(Color.BLACK); 
                outg.setFont(new Font("宋体",Font.BOLD,30)); //字体、字型、字号 
                int strWidth = outg.getFontMetrics().stringWidth(productName);
                if (strWidth > 399) {
//                  //长度过长就截取前面部分
//                  outg.drawString(productName, 0, image.getHeight() + (outImage.getHeight() - image.getHeight())/2 + 5 ); //画文字
                    //长度过长就换行
                    String productName1 = productName.substring(0, productName.length()/2);
                    String productName2 = productName.substring(productName.length()/2, productName.length());
                    int strWidth1 = outg.getFontMetrics().stringWidth(productName1);
                    int strWidth2 = outg.getFontMetrics().stringWidth(productName2);
                    outg.drawString(productName1, 200  - strWidth1/2, image.getHeight() + (outImage.getHeight() - image.getHeight())/2 + 12 );
                    BufferedImage outImage2 = new BufferedImage(400, 485, BufferedImage.TYPE_4BYTE_ABGR);
                    Graphics2D outg2 = outImage2.createGraphics();
                    outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
                    outg2.setColor(Color.BLACK); 
                    outg2.setFont(new Font("宋体",Font.BOLD,30)); //字体、字型、字号 
                    outg2.drawString(productName2, 200  - strWidth2/2, outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight())/2 + 5 );
                    outg2.dispose(); 
                    outImage2.flush();
                    outImage = outImage2;
                }else {
                    outg.drawString(productName, 200  - strWidth/2 , image.getHeight() + (outImage.getHeight() - image.getHeight())/2 + 12 ); //画文字 
                }
                outg.dispose(); 
                outImage.flush();
                image = outImage;
            }
            logo.flush();
            image.flush();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.flush();
            ImageIO.write(image, "png", baos);

//            image = toRGBy(image);
            
            //二维码生成的路径，但是实际项目中，我们是把这生成的二维码显示到界面上的，因此下面的折行代码可以注释掉
            //可以看到这个方法最终返回的是这个二维码的imageBase64字符串
            //前端用 <img src="data:image/png;base64,${imageBase64QRCode}"/>  其中${imageBase64QRCode}对应二维码的imageBase64字符串
            ImageIO.write(image, "png", new File("/Users/yooranchen/Desktop/hnd_QRcode_"+productName+"_logo.png")); //TODO  

            String imageBase64QRCode =  Base64.encodeBase64URLSafeString(baos.toByteArray());

            baos.close();
            return imageBase64QRCode;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成带logo的二维码图片
     * @param txt          //二维码内容
     * @param logoPath     //logo绝对物理路径
     * @param imgPath      //二维码保存绝对物理路径
     * @param imgName      //二维码文件名称
     * @param suffix       //图片后缀名
     * @throws Exception
     */
    public  void generateQRImage(String txt, String logoPath, String imgPath, String imgName, String suffix,String mobile) throws Exception{
        File filePath = new File(imgPath);
        if(!filePath.exists()){
            filePath.mkdirs();
        }
        if(imgPath.endsWith(File.separator)){
            imgPath += imgName;
        }else{
            imgPath += File.separator+imgName;
        }
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, level);
        hints.put(EncodeHintType.MARGIN, margin);  //设置白边
        BitMatrix bitMatrix = new MultiFormatWriter().encode(txt, BarcodeFormat.QR_CODE, width, height, hints);
        File qrcodeFile = new File(imgPath);
        writeToFile(bitMatrix, suffix, qrcodeFile, logoPath,mobile);
    }

    /**
     *
     * @param matrix 二维码矩阵相关
     * @param format 二维码图片格式
     * @param file 二维码图片文件
     * @param logoPath logo路径
     * @throws IOException
     */
    public static void writeToFile(BitMatrix matrix,String format,File file,String logoPath,String mobile) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        Graphics2D gs = image.createGraphics();

        int ratioWidth = image.getWidth()*2/10;
        int ratioHeight = image.getHeight()*2/10;
        //载入logo
        Image img = ImageIO.read(new File(logoPath));
        int logoWidth = img.getWidth(null)>ratioWidth?ratioWidth:img.getWidth(null);
        int logoHeight = img.getHeight(null)>ratioHeight?ratioHeight:img.getHeight(null);

        int x = (image.getWidth() - logoWidth) / 2;
        int y = (image.getHeight() - logoHeight) / 2;

        gs.drawImage(img, x, y, logoWidth, logoHeight, null);
        gs.setColor(Color.black);
        gs.setBackground(Color.WHITE);
        gs.dispose();
		//把商品名称添加上去，商品名称不要太长哦，这里最多支持两行。太长就会自动截取啦
        if (mobile != null && !mobile.equals("")) {
            //新的图片，把带logo的二维码下面加上文字
            BufferedImage outImage = new BufferedImage(600, 660, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D outg = outImage.createGraphics();
            //画二维码到新的面板
            outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            outg.setColor(Color.WHITE);
            outg.fillRect(0, 600, 600, 60);
            //画文字到新的面板
            outg.setColor(Color.BLACK); 
            outg.setFont(new Font("宋体",Font.BOLD,40)); //字体、字型、字号 
            int strWidth = outg.getFontMetrics().stringWidth(mobile);
            outg.drawString(mobile, 300  - strWidth/2 , image.getHeight() + (outImage.getHeight() - image.getHeight())/2 + 12 ); //画文字 
            outg.dispose(); 
            outImage.flush();
            image = outImage;
        }
        img.flush();
        if(!ImageIO.write(image, format, file)){
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }
    public static BufferedImage toBufferedImage(BitMatrix matrix){
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
            }
        }
        return image;
    }

    public static void main(String[] args) throws IOException {
    	//BufferedImage bim, File qrCodePic,String mobile
    	/*System.out.println("--start--");
    	BufferedImage bgimg=ImageIO.read(new File("D:/hnd_upload/qrcode/bgimag1.jpg"));
    	ImageBean imageBean =new ImageBean();
    	imageBean.setImageHeight(600);
    	imageBean.setImageWidth(600);
    	imageBean.setImageX(0);
    	imageBean.setImageY(0);
    	imageBean.setImageUrl("D:/hnd_upload/qrcode/1342.png");
    	String reUrl="D:/hnd_upload/qrcode/";
    	String reFileName="11";
    	
    	 QRCodeServlet servlet = new QRCodeServlet();
    	 servlet.addBackground_QRCode(bgimg,imageBean,reUrl,reFileName);
    	 System.out.println("--over--");
    	
    	 */
    	/*
		String mobile = "15332465";
		File imageFile = new File("D://11//");
//		BufferedImage bufImg = null;
//		bufImg = ImageIO.read(imageFile);
//		LogoConfig lc = new LogoConfig();
        QRCodeServlet servlet = new QRCodeServlet();
//        servlet.addLogo_QRCode(bufImg, new File("/Users/yooranchen/Desktop/LOGO300.png"), lc, mobile);
        try {
            servlet.generateQRImage("http://weixin.qq.com/q/02xxvYw0nYcHj10000M07u", "D://11//LOGO300.png",
            		imageFile.getAbsolutePath(),
                    "hnd_QRcode_"+mobile+" .png", "png","22222");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public BufferedImage toRGBy(BufferedImage srcImg){      
    	
        return new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(srcImg, null);
    }
}
