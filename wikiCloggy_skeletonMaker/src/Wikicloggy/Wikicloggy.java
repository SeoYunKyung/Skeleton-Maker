package Wikicloggy;

import java.awt.image.*;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import imageOp.skeleton.ASkeletonPrunningOp;

import javax.imageio.ImageIO;


public class Wikicloggy extends Frame {
	
	BufferedImage img = null;
	
	public Wikicloggy(String title) throws IOException {	
		super(title);
		
		
		File f = new File("../darkflow/result/result.png");
		img = ImageIO.read(f);
		
		
		setBounds(100,100,600,600);
		setVisible(true);
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		new Wikicloggy("result_cloggy");
		
	}
	
	public void paint(Graphics g) {
		if(img==null)return;
		int imgWidth = img.getWidth(this);
		int imgHeight = img.getHeight(this);
		
		BufferedImage result_cloggy=null;
		
		ASkeletonPrunningOp ASIP = new ASkeletonPrunningOp(img);
		result_cloggy = ASIP.execute();
		
		g.drawImage(result_cloggy,(getWidth()-imgWidth)/2,(getHeight()-imgHeight)/2,this);	
	
	}
	
	
	
}
