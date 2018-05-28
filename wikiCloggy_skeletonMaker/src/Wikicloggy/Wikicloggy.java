package Wikicloggy;

import java.lang.Thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Component;

import java.io.*;

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
	BufferedImage save_image = null;
	JFrame frm =null;
	String file_name =null;
	int top; // point of top of head
	int bottom; //point of bottom of head
	int right; //point of right of head
	int left; //point of left of head
	ArrayList<PartsPoints> pp = new ArrayList<PartsPoints>(); // each points with tags
	ArrayList<Point> branch = new ArrayList<Point>(); // branch points of skeleton 
	ArrayList<Point> end = new ArrayList<Point>(); //end points of skeleton
	ArrayList<Point> skeleton = new ArrayList<Point>(); //skeleton points 
	BufferedImage result_cloggy = null; // result of skelelton image
	
	/*
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
		}*/
	public Wikicloggy(String title,String filepath) throws IOException {	
		
		File f =new File(filepath);
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

	
	public static ArrayList<HashMap<String,String>> getPhotoList(String path){
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		File dirFile = new File(path);
		if(dirFile.exists() && dirFile.isDirectory()){
			File[] fileList =dirFile.listFiles();
			for (File tempFile : fileList) {
				if(tempFile.isFile() && tempFile.length() > 0){
					String tempPath = tempFile.getParent();
					String fileFullName = tempFile.getName();
					String onlyFileName = fileFullName.toLowerCase().substring(0,fileFullName.lastIndexOf("."));
					HashMap<String, String> photo = new HashMap<String,String>();
					photo.put("fullname",fileFullName);
					photo.put("filename",onlyFileName);
					list.add(photo);				
				}
			}
		}
		return list;
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//File f = new File(args[0]);
		ArrayList<HashMap<String,String>> list = getPhotoList(args[0]);
		for(int i =0; i<list.size();i++){
			System.out.println(list.get(i).get("fullname"));
			Wikicloggy wc = new Wikicloggy("result_cloggy","../exiting/"+list.get(i).get("fullname"));
			wc.file_name = list.get(i).get("filename");		
			//File rect_txt = new File("../result/result.txt");
		
			//Skeleton Pruning
			ASkeletonPrunningOp ASIP = new ASkeletonPrunningOp(wc.img);
			wc.result_cloggy = wc.make_skeleton(ASIP,wc.img);
		

			//wc.setHeadBox(rect_txt);
			wc.getEachskeletonInfo(ASIP);
			wc.makeSkeletonTextFile();			
		
		
			//show each pts and skeleton of dog
			wc.frm = new JFrame("dog skeleton");
			ImageIcon ic = new ImageIcon(wc.result_cloggy);
			JLabel iblImage1 = new JLabel(ic);

			wc.frm.add(iblImage1);
			wc.frm.add(wc);
												
			wc.frm.setSize(wc.result_cloggy.getWidth(),wc.result_cloggy.getHeight()+20);
			wc.frm.setVisible(true);
				
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){}
						
			wc.SaveScreenShot(wc.frm,"../final2/exciting/"+wc.file_name);
			wc.frm.dispose();
		}
		
	
	}

	public void getEachskeletonInfo(ASkeletonPrunningOp ASIP){
		//Take BranchPts from asima
		ASIMA _asima= ASIP.getASIMA();
		this.branch = _asima.getBranchPoints();
		/*for (int i = 0; i<wc.branch.size();i++){
			System.out.println("Branch num : "+ i+" x : "+wc.branch.get(i).y+" y : "+wc.branch.get(i).x);
			
		}*/

		//Take EndPts from asima
		this.end = _asima.getEndPoints();
		/*for (int i = 0; i<wc.end.size();i++){
			System.out.println("End num : "+ i+" x : "+wc.end.get(i).y+" y : "+wc.end.get(i).x);
			
		}*/
		this.skeleton = _asima.getSkeletonPoints();
		for (int i = 0; i<this.skeleton.size();i++){
			PartsPoints tmp_ppt = new PartsPoints();
			tmp_ppt.setPoint(this.skeleton.get(i).y,this.skeleton.get(i).x);
			this.pp.add(tmp_ppt);
		}			

		for (int i = 0; i<this.skeleton.size();i++){
			if ((this.pp.get(i).y < this.bottom) && (this.pp.get(i).y > this.top) && (this.pp.get(i).x > this.left) && (this.pp.get(i).x < this.right)){
				this.pp.get(i).setTag("head");	
			}
			//System.out.println(this.pp.get(i).x + " "+this.pp.get(i).y +" "+this.pp.get(i).tag);
		}		
	}

	public void makeSkeletonTextFile(){
		 try {
      			////////////////////////////////////////////////////////////////
      			BufferedWriter out = new BufferedWriter(new FileWriter("../final/"+this.file_name+".txt"));
			for (int i = 0; i<this.skeleton.size();i++){
				out.write(Integer.toString(this.pp.get(i).x));
				out.write(" ");
				out.write(Integer.toString(this.pp.get(i).y));
				out.write(" ");
				out.write(this.pp.get(i).tag);
				out.newLine();
			}	
			
			out.close();
		}
		catch(IOException e){
			System.err.println(e);
			System.exit(1);	
		}

	}
	public void setHeadBox(File rect_txt){
	
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
		this.file_name = rect_info[rect_info.length-1];
		int j=0;
		for(int i =0; i<rect_info.length;i++){
		    if( i % 2 == 1){
			rect[j]=Integer.parseInt(rect_info[i]);
			j++;
		    }
		}
	
		//head_box[ top, left, bottom, right]
		this.top = rect[0]; 
		this.left = rect[1];
		this.bottom = rect[2];
		this.right = rect[3];

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

	public static BufferedImage getScreenShot(Component component){
		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(),BufferedImage.TYPE_INT_RGB);
		component.paint(image.getGraphics());
		return image;
	}	
	
	public static void SaveScreenShot( Component component, String filename){
		BufferedImage img = getScreenShot(component);
		try{
			ImageIO.write(img,"png",new File(filename));
		}
		catch(IOException e){
			System.out.println(""+e.toString());
		}	
	}
	
}
