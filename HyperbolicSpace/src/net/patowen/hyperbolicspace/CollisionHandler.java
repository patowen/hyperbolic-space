package net.patowen.hyperbolicspace;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class CollisionHandler
{
	private static class Wall
	{
		public Vector3 v1, v2, v3; //vertices
		
		public Wall(Vector3 v1, Vector3 v2, Vector3 v3)
		{
			this.v1 = v1;
			this.v2 = v2;
			this.v3 = v3;
		}
	}
	
	private static class Collision
	{
		public double t; //time
		public Vector3 n; //normal
		
		public Collision(double t, Vector3 n)
		{
			this.t = t;
			this.n = n;
		}
		
		public Collision(double t)
		{
			this.t = t;
			this.n = null;
		}
	}
	
	private ArrayList<Wall> walls;
	
	public CollisionHandler()
	{
		walls = new ArrayList<Wall>();
	}
	
	public void addWall(Vector3 v1, Vector3 v2, Vector3 v3)
	{
		walls.add(new Wall(v1, v2, v3));
	}
	
	public double getSphereCollision(double px, double py, double pz, double radius, double pxd, double pyd, double pzd)
	{
		double buffer = 0.001;
		if (pxd == 0 && pyd == 0 && pzd == 0) return 1;
		double pd = Math.sqrt(pxd*pxd + pyd*pyd + pzd*pzd); buffer /= pd; //TODO Euclidean
		pxd *= 1+buffer; pyd *= 1+buffer; pzd *= 1+buffer;
		
		//The minimum and maximum x, y, and z values covered by the player TODO Euclidean
		double pxMin = Math.min(px,px+pxd), pyMin = Math.min(py,py+pyd), pzMin = Math.min(pz,pz+pzd);
		double pxMax = Math.max(px,px+pxd), pyMax = Math.max(py,py+pyd), pzMax = Math.max(pz,pz+pzd);
		
		double tReturn = 1;
		double nx = 0, ny = 0, nz = 0;
		
		//Check for collision with environment walls.
		for (int i=0; i<walls.size(); i+=1)
		{
//			double x1 = cX1.get(i), y1 = cY1.get(i), z1 = cZ1.get(i);
//			double x2 = cX2.get(i), y2 = cY2.get(i), z2 = cZ2.get(i);
//			double x3 = cX3.get(i), y3 = cY3.get(i), z3 = cZ3.get(i);
//			
//			double xMin = min3(x1,x2,x3), yMin = min3(y1,y2,y3), zMin = min3(z1,z2,z3);
//			double xMax = max3(x1,x2,x3), yMax = max3(y1,y2,y3), zMax = max3(z1,z2,z3);
			
//			if (pxMax+radius < xMin || pxMin-radius > xMax) continue;
//			if (pyMax+radius < yMin || pyMin-radius > yMax) continue;
//			if (pzMax+height < zMin || pzMin > zMax) continue;
			
			double t;
			
//			//Interior
//			t = getPTriangleCollision(px1,py1,pz1,pxd,pyd,pzd, radius,height, x1,y1,z1,x2,y2,z2,x3,y3,z3);
//			if (t<tReturn) {tReturn=t; nx=normalX; ny=normalY; nz=normalZ;}
//			//Edges
//			t = getPLineCollision(px1,py1,pz1,pxd,pyd,pzd,radius,height, x1,y1,z1,x2-x1,y2-y1,z2-z1);
//			if (t<tReturn) {tReturn=t; nx=normalX; ny=normalY; nz=normalZ;}
//			t = getPLineCollision(px1,py1,pz1,pxd,pyd,pzd,radius,height, x2,y2,z2,x3-x2,y3-y2,z3-z2);
//			if (t<tReturn) {tReturn=t; nx=normalX; ny=normalY; nz=normalZ;}
//			t = getPLineCollision(px1,py1,pz1,pxd,pyd,pzd,radius,height, x3,y3,z3,x1-x3,y1-y3,z1-z3);
//			if (t<tReturn) {tReturn=t; nx=normalX; ny=normalY; nz=normalZ;}
//			//Vertices
//			t = getPPointCollision(px1,py1,pz1,pxd,pyd,pzd,radius,height, x1,y1,z1);
//			if (t<tReturn) {tReturn=t; nx=normalX; ny=normalY; nz=normalZ;}
//			t = getPPointCollision(px1,py1,pz1,pxd,pyd,pzd,radius,height, x2,y2,z2);
//			if (t<tReturn) {tReturn=t; nx=normalX; ny=normalY; nz=normalZ;}
//			t = getPPointCollision(px1,py1,pz1,pxd,pyd,pzd,radius,height, x3,y3,z3);
//			if (t<tReturn) {tReturn=t; nx=normalX; ny=normalY; nz=normalZ;}
		}
		
		if (tReturn == 1) return 1;
		return Math.max(0,tReturn*(1+buffer)-buffer);
	}
}
