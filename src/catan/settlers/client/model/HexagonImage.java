package catan.settlers.client.model;


import org.minueto.MinuetoColor;
import org.minueto.image.MinuetoImage;

import catan.settlers.client.view.GameFrame;

public class HexagonImage extends MinuetoImage{
	private MinuetoImage hexImage;
	private static int BORDERS =50; //default number of pixels for the border
	final static int HEXSIZE = 50;
	final static int BSIZE = 19; //changes size of the board
	final static int SCRSIZE = HEXSIZE * (BSIZE + 1) + BORDERS *3;
	
	
	public static boolean notAlternateVertex = true; // if this is true it means that the coordinate (0,0) are coordinates of the first vertex.
	//if it is false it means that the coordinate are coordinates of the top left rectangle.
	
	private static int s = 0; //length of side
	private static int t = 0; //short side of the 30 degree triangle
	private static int r = 0; //radius -centre to middle of each side
	private static int h = 0; //distance between centres of two neighbor hexes
	
	public static enum HexType {
		WOOD,
		BRICK,
		SHEEP,
		WHEAT,
		ORE,
		DESERT,
		WATER
	};
	//private HexType type;
	private MinuetoColor color;
	
	public HexagonImage(HexType htype){
		System.out.println(drawCoordinates(0,0));
		HexagonImage.setHeight(HEXSIZE);
		HexagonImage.setSide(HEXSIZE);
		System.out.println(drawCoordinates(0,0));
		hexImage = new MinuetoImage(SCRSIZE,SCRSIZE);
		hexImage.drawPolygon(MinuetoColor.BLACK, drawCoordinates(0,0));
	}
	
	public MinuetoColor getColor(){
		return color;
	}
	
	public void setColor(MinuetoColor color){
		this.color = color;
	}
	
	public static void setSide(int side){
		s=side;
		t=(int) (s/2);
		r = (int) (s * 0.8660254037844);
		h = 2* r;
	}
	
	public static void setHeight(int height){
		h = height; //distance between two centres, or the size of the coordinate
		r = h/2; // radius of the inscribed circle
		t= (int) (h/1.73205); //short side is (h/2)/cos30 = (h/2)/(sqrt(3)/2 = h/sqrt(3)
		s= (int) (r/1.73205); //side length is (h/2) tan30 = h/2/sqrt(3) = radius/sqrt(3)
		
	}
	
	public static void setBorders(int b){
		BORDERS = b;
	}
	
	public int[] drawCoordinates(int x0, int y0){
		int x = x0 + BORDERS;
		int y = y0 + BORDERS;
		
		int[] coordinates;
		
		if(notAlternateVertex){
			//notAlternateVertex = false;
			coordinates = new int[] {x,y,x+s,y,x+s+t,y+r,x+s,y+r+r,x,y+r+r,x-t,y+r}; //the top left vertex being at (x,y) This means part of vertex is cut
		}
		else{
			//notAlternateVertex = true;
			coordinates = new int[] {x+t,y,x+s+t,y,x+s+t+t,y+r,x+s+t,y+r+r,x+t,y+r+r,x,y+r};
		}

		return coordinates;	
	}
	
	public MinuetoImage getHexImage(){
		return hexImage;
	}
}

