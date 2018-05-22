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

import Wikicloggy.PartsPoints;


import javax.imageio.ImageIO;

/* Be sure that branch points/ end points (x,y) coordinate is swapped
   I mean y is x and x is y 
	@@@@@@ NOT SKELETON POINTS @@@@@*/


public class Wikicloggy extends JPanel{
	
	BufferedImage img = null;
	int top; // point of top of head
	int bottom; //point of bottom of head
	int right; //point of right of head
	int left; //point of left of head
	ArrayList<PartsPoints> pp = new ArrayList<PartsPoints>(); // each points with tags
	ArrayList<Point> branch = new ArrayList<Point>(); // branch points of skeleton 
	ArrayList<Point> end = new ArrayList<Point>(); //end points of skeleton
	ArrayList<Point> skeleton = new ArrayList<Point>(); //skeleton points 
	BufferedImage result_cloggy = null; // result of skelelton image
	
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
		wc.skeleton = _asima.getSkeletonPoints();
		for (int i = 0; i<wc.skeleton.size();i++){
			PartsPoints tmp_ppt = new PartsPoints();
			tmp_ppt.setPoint(wc.skeleton.get(i).y,wc.skeleton.get(i).x);
			wc.pp.add(tmp_ppt);
		}			

		for (int i = 0; i<wc.skeleton.size();i++){
			if ((wc.pp.get(i).y < wc.bottom) && (wc.pp.get(i).y > wc.top) && (wc.pp.get(i).x > wc.left) && (wc.pp.get(i).x < wc.right)){
				wc.pp.get(i).setTag("head");	
				System.out.println(wc.pp.get(i).tag);	
			}
			System.out.println(wc.pp.get(i).x + " "+wc.pp.get(i).y +" "+wc.pp.get(i).tag);
		}	
		System.out.println("TopLeft : ("+wc.left+","+wc.top+") "+"TopRight : ("+wc.right+","+wc.top+")"+ "BottomLeft : ("+wc.left+","+wc.bottom+")"+ "BottomRight : ("+wc.right+","+wc.bottom+")"); 
		for (int i = 0; i<wc.skeleton.size();i++){
			if (wc.pp.get(i).tag.equals("head")){
				System.out.println("HEAD / x :" + wc.pp.get(i).y + " y:" + wc.pp.get(i).x);			
			}
		}			
		


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
		g.setColor(Color.red);
		for (int i = 0; i<this.branch.size();i++){
			
			g.drawOval(this.branch.get(i).y,this.branch.get(i).x,5,5);
		}
		g.setColor(Color.cyan);
		for (int i = 0; i<this.end.size();i++){
			
			g.drawOval(this.end.get(i).y,this.end.get(i).x,5,5);
		}
		g.setColor(Color.orange);
		for (int i = 0; i<this.pp.size();i++){
			if(this.pp.get(i).tag.equals("head")){			
				g.drawOval(this.pp.get(i).x,this.pp.get(i).y,5,5);
			}		
		}
		g.setColor(Color.pink);
		g.drawOval(this.left,this.top,5,5);
		g.drawOval(this.left,this.bottom,5,5);
		g.drawOval(this.right,this.top,5,5);
		g.drawOval(this.right,this.bottom,5,5);
	}

	
	
	
	
}
