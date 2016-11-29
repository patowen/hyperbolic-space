package net.patowen.hyperbolicspace.math;

public class Vector31
{
	public double x;
	public double y;
	public double z;
	public double w;
	
	public Vector31() {
		x = 0; y = 0; z = 0; w = 1;
	}
	
	public Vector31(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector31(Vector31 v) {
		this(v.x, v.y, v.z, v.w);
	}
	
	public static Vector31 makePoincare(Vector3 v) {
		double dist = 1.0 - v.x*v.x - v.y*v.y - v.z*v.z;
		if (dist <= 0) {
			throw new IllegalArgumentException("Magnitude must be less than 1: " + Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z));
		}
		double factor = 2/dist;
		return new Vector31(v.x*factor, v.y*factor, v.z*factor, factor-1);
	}
	
	public static Vector31 makeOrtho(Vector3 v) {
		double dist = v.x*v.x + v.y*v.y + v.z*v.z;
		return new Vector31(v.x, v.y, v.z, Math.sqrt(dist + 1));
	}
	
	public static Vector31 makeOrthogonal(Vector31 v1, Vector31 v2, Vector31 v3) {
		double x = v1.y*v2.z*v3.w + v1.z*v2.w*v3.y + v1.w*v2.y*v3.z - v1.y*v2.w*v3.z - v1.z*v2.y*v3.w - v1.w*v2.z*v3.y;
		double y = -(v1.x*v2.z*v3.w + v1.z*v2.w*v3.x + v1.w*v2.x*v3.z - v1.x*v2.w*v3.z - v1.z*v2.x*v3.w - v1.w*v2.z*v3.x);
		double z = v1.x*v2.y*v3.w + v1.y*v2.w*v3.x + v1.w*v2.x*v3.y - v1.x*v2.w*v3.y - v1.y*v2.x*v3.w - v1.w*v2.y*v3.x;
		double w = -(v1.x*v2.y*v3.z + v1.y*v2.z*v3.x + v1.z*v2.x*v3.y - v1.x*v2.z*v3.y - v1.y*v2.x*v3.z - v1.z*v2.y*v3.x);
		return new Vector31(x, y, z, w);
	}
	
	public void reset() {
		x = 0; y = 0; z = 0; w = 1;
	}
	
	public boolean normalizeAsPoint() {
		double dist = w*w - x*x - y*y - z*z;
		if (dist > 1e-6) {
			double factor = 1.0/Math.sqrt(dist);
			x *= factor;
			y *= factor;
			z *= factor;
			w *= factor;
			return true;
		}
		return false;
	}
	
	public boolean normalizeAsDirection() {
		double dist = x*x + y*y + z*z - w*w;
		if (dist > 1e-6) {
			double factor = 1.0/Math.sqrt(dist);
			x *= factor;
			y *= factor;
			z *= factor;
			w *= factor;
			return true;
		}
		return false;
	}
	
	public void rotate(Vector3 v, double theta) {
		double xx = v.x, yy = v.y, zz = v.z;
		double c = Math.cos(theta), s = Math.sin(theta);
		
		double xNew = x*(xx*xx+(1-xx*xx)*c) + y*(xx*yy-xx*yy*c-zz*s) + z*(xx*zz-xx*zz*c+yy*s);
		double yNew = x*(xx*yy-xx*yy*c+zz*s) + y*(yy*yy+(1-yy*yy)*c) + z*(yy*zz-yy*zz*c-xx*s);
		double zNew = x*(xx*zz-xx*zz*c-yy*s) + y*(yy*zz-yy*zz*c+xx*s) + z*(zz*zz+(1-zz*zz)*c);
		
		x = xNew; y = yNew; z = zNew;
	}
	
	public Vector31 times(double c) {
		return new Vector31(x*c, y*c, z*c, w*c);
	}
	
	public Vector31 plusMultiple(Vector31 v, double c) {
		return new Vector31(x+v.x*c, y+v.y*c, z+v.z*c, w+v.w*c);
	}
	
	public double squared() {
		return -x*x - y*y - z*z + w*w;
	}
	
	public double dot(Vector31 v) {
		return -x*v.x - y*v.y - z*v.z + w*v.w;
	}
	
	public Vector31 perpendicular(Vector31 v) {
		// The result of this dot any perpendicular vector should be 0
		return times(dot(v)).plusMultiple(v, -1);
	}
	
	public Vector3 getPoincare() {
		return new Vector3(x/(w+1), y/(w+1), z/(w+1));
	}
	
	public Vector3 getOrtho() {
		return new Vector3(x, y, z);
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("(").append(x).append(", ").append(y).append(", ").append(z).append(", ").append(w).append(")");
		
		return str.toString();
	}
}
