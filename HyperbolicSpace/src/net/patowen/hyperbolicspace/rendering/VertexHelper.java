package net.patowen.hyperbolicspace.rendering;

import net.patowen.hyperbolicspace.math.Vector2;
import net.patowen.hyperbolicspace.math.Vector3;

public class VertexHelper
{
	public static Vector3[] arrayToVector3(double[][] array)
	{
		Vector3[] output = new Vector3[array.length];
		for (int i=0; i<array.length; i++)
		{
			output[i] = new Vector3(array[i]);
		}
		return output;
	}
	
	public static Vector2[] arrayToVector2(double[][] array)
	{
		Vector2[] output = new Vector2[array.length];
		for (int i=0; i<array.length; i++)
		{
			output[i] = new Vector2(array[i]);
		}
		return output;
	}
}
