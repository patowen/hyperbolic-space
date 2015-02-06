package net.patowen.hyperbolicspace;

public class MathHelper
{
	public static double asinh(double x)
	{
		return Math.log(x + Math.sqrt(1+x*x));
	}
	
	public static double acosh(double x)
	{
		return Math.log(x + Math.sqrt(x*x-1));
	}
	
	public static double sqr(double x)
	{
		return x*x;
	}
}
