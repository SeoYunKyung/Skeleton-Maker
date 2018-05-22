package Wikicloggy;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import imageOp.skeleton.ASkeletonPrunningOp;
import imageOp.skeleton.ASIMA;
import imageOp.skeleton.Point;


import javax.imageio.ImageIO;


public class Wikicloggy extends JPanel{
	
	BufferedImage img = null;
	int top; // point of top of head
	int bottom; //point of bottom of head
	int right; //point of right of head
	int left; //point of left of head
	ArrayList<Wikicloggy.PartsPoints> pp; // each points with tags
	ArrayList<Point> branch = new ArrayList<Point>(); // branch points of skeleton 
	ArrayList<Point> end = new ArrayList<Point>(); //end points of skeleton
	BufferedImage result_cloggy = null; // result of skelelton image

	class PartsPoints{ // points with tag
		String tag = "none";
		Point point= new Point();
	}	
	
	public Wikicloggy(String title) throws IOException {	
		
		File f = new File("../result/result.png");
		this.img= ImageIO.read(f);
		this.top =0;
		this.bottom =0;
		this.right =0;
		this.left =0;		
		
		if(img==null){
		   System.out.println("No Image\n");
		   return;		
		}
				
		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Wikicloggy wc = new Wikicloggy("result_cloggy");		
		File rect_txt = new File("../result/result.txt");
		
		String[] rect_info = null;
		String text = null;
		int[] rect =new int[4];
		// read head rectangle info and save to other array
		try{
	 	   BufferedReader reader = new BufferedReader(new FileReader(rect_txt));

		   while((text = reader.readLine()) != null){
			rect_info = null;
			rect_info = text.split(" ");
		   }
		}
		catch (FileNotFoundException fnf){
		    System.err.println(fnf);
		    System.exit(1);
		}
		catch (IOException e){
		    System.err.println(e);
		    System.exit(1);
		}
		
		int j=0;
		for(int i =0; i<rect_info.length;i++){
		    if( i % 2 == 1){
			rect[j]=Integer.parseInt(rect_info[i]);
			System.out.println(rect[j]);
			j++;
		    }
		}
	
		//head_box[ top, left, bottom, right]
		wc.top = rect[0]; 
		wc.left = rect[1];
		wc.bottom = rect[2];
		wc.right = rect[3];

		//Skeleton Pruning
		ASkeletonPrunningOp ASIP = new ASkeletonPrunningOp(wc.img);
		wc.result_cloggy = wc.make_skeleton(ASIP,wc.img);
		

		//Take BranchPts from asima
		ASIMA _asima= ASIP.getASIMA();
		wc.branch = _asima.getBranchPoints();
		/*for (int i = 0; i<wc.branch.size();i++){
			System.out.println("Branch num : "+ i+" x : "+wc.branch.get(i).y+" y : "+wc.branch.get(i).x);
			
		}*/

		//Take EndPts from asima
		wc.end = _asima.getEndPoints();
		/*for (int i = 0; i<wc.end.size();i++){
			System.out.println("End num : "+ i+" x : "+wc.end.get(i).y+" y : "+wc.end.get(i).x);
			
		}*/

		//show each pts and skeleton of dog
		
		JFrame frm = new JFrame("dog skeleton");
		ImageIcon ic = new ImageIcon(wc.result_cloggy);
		JLabel iblImage1 = new JLabel(ic);

		frm.add(iblImage1);
		frm.add(wc);
		frm.setVisible(true);
		frm.setSize(wc.result_cloggy.getWidth(),wc.result_cloggy.getHeight());
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	}

	public BufferedImage make_skeleton(ASkeletonPrunningOp ASIP, BufferedImage img){
		
		BufferedImage result_cloggy=null;
		result_cloggy = ASIP.execute();
		
		return result_cloggy;	
	}
	
	//show the points with oval
	public void paint(Graphics g){
		
		g.drawImage((Image)this.result_cloggy,0,0,null);
		for (int i = 0; i<this.branch.size();i++){
			g.setColor(Color.red);
			g.drawOval(this.branch.get(i).y,this.branch.get(i).x,5,5);
		}
		for (int i = 0; i<this.end.size();i++){
			g.setColor(Color.cyan);
			g.drawOval(this.end.get(i).y,this.end.get(i).x,5,5);
		}
		g.setColor(Color.pink);
		g.drawOval(this.left,this.top,5,5);
		g.drawOval(this.left,this.bottom,5,5);
		g.drawOval(this.right,this.top,5,5);
		g.drawOval(this.right,this.bottom,5,5);
	}

	
	
	
	
}
