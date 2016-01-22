package fr.ihm.mi.gestuel;

import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Template 
{
	String name;
	Stroke s;

	public Template(String n, Stroke s)
	{
		name = n;
		this.s = s;
	}
	
	public double getDistance(Stroke str)
	{
		double dist = 0.0;
		
		for(int i=0;i<s.size();i++)
		{
			Point2D.Double pTemplate = s.getPoint(i);
			Point2D.Double p = str.getPoint(i);
			dist+=p.distance(pTemplate);
		}
		
		return dist;
	}
	
	public void write(PrintWriter out)
	{
		out.print(name);
		out.print(" ");
		for(int i=0;i<s.size();i++)
		{
			Point2D.Double p = s.getPoint(i);
			out.print(String.valueOf(p.getX()));
			out.print(":");
			out.print(String.valueOf(p.getY()));
			out.print(" ");
		}
		out.println("");
	}
	
	public static Template read(String line)
	{
		StringTokenizer tok = new StringTokenizer(line, " ");
		String n = tok.nextToken();
		Stroke s = new Stroke();
		while(tok.hasMoreTokens())
		{
			StringTokenizer tok2 = new StringTokenizer(tok.nextToken(), ":");
			double x = Double.parseDouble(tok2.nextToken());
			double y = Double.parseDouble(tok2.nextToken());
			s.addPoint(new Point2D.Double(x,y));
		}
		return new Template(n, s);
	}
}
