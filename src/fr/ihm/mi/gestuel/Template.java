package fr.ihm.mi.gestuel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Template {

    String name;
    Stroke s;

    public Color[] color = {Color.yellow, Color.blue, Color.ORANGE, Color.red};

    public Template(String n, Stroke s) {
        name = n;
        this.s = s;
    }

    public double getDistance(Stroke str) {
        double dist = 10e9;

        for (int i = 0; i < s.size(); i++) {
            Point2D.Double pTemplate = s.getPoint(i);
            Point2D.Double p = str.getPoint(i);
            dist += p.distance(pTemplate);
        }
        System.out.println("Dist = " + dist);
        return dist;
    }

    public void write(PrintWriter out) {
        out.print(name);
        out.print(" ");
        for (int i = 0; i < s.size(); i++) {
            Point2D.Double p = s.getPoint(i);
            out.print(String.valueOf(p.getX()));
            out.print(":");
            out.print(String.valueOf(p.getY()));
            out.print(" ");
        }
        out.println("");
    }

    public void dessinerStroke(Graphics2D g, int i) {
        Iterator<Point2D.Double> iterator = s.listePoint.iterator();
        Point2D.Double point;

        g.setColor(color[i]);

        while (iterator.hasNext()) {
            point = iterator.next();
            g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            g.drawLine((int) point.getX(), (int) point.getY(), (int) point.getX(), (int) point.getY());
        }
    }

    public static Template read(String line) {
        StringTokenizer tok = new StringTokenizer(line, " ");
        String n = tok.nextToken();
        Stroke s = new Stroke();
        while (tok.hasMoreTokens()) {
            StringTokenizer tok2 = new StringTokenizer(tok.nextToken(), ":");
            double x = Double.parseDouble(tok2.nextToken());
            double y = Double.parseDouble(tok2.nextToken());
            s.addPoint(new Point2D.Double(x, y));
        }
        return new Template(n, s);
    }
}
