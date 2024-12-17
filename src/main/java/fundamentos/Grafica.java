///////////////////////////////////////////////////////////////////////
//                       Paquete fundamentos                         //
//  Conjunto de clases para hacer entrada/salida sencilla en Java    //
//                                                                   //
//                     Copyright (C) 2005-2016                       //
//                 Universidad de Cantabria, SPAIN                   //
//                                                                   //
// Authors: Michael Gonzalez   <mgh@unican.es>                       //
//          Mariano Benito Hoz <mbenitohoz at gmail dot com>         //
//                                                                   //
// This program is free software; you can redistribute it and/or     //
// modify it under the terms of the GNU General Public               //
// License as published by the Free Software Foundation; either      //
// version 3 of the License, or (at your option) any later version.  //
//                                                                   //
// This program is distributed in the hope that it will be useful,   //
// but WITHOUT ANY WARRANTY; without even the implied warranty of    //
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU //
// General Public License for more details.                          //
///////////////////////////////////////////////////////////////////////

package fundamentos;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

/**  Grafica     por Michael Gonzalez y Mariano Benito. 
 *   Version 3.5. Octubre 2016 <p>

(modificacion de la clase por J M Bishop   July 1999)<p>

Es una clase sencilla que ofrece facilidades para: <p>
- almacenar puntos <p>
- mostrarlos como una grafica de puntos o lineas <p>
Se pueden mostrar varias graficas en la misma ventana <p>
Los ejes se pintan automaticamente <p>

Interfaz:  <p>
 ********   <p>

Basica   <p>
------   <p>
new Grafica ()  Constructor <p>
inserta(x, y)   Inserta un punto en la lista. Los puntos deben estar  <p>
ordenados por el eje X  <p>
pinta()         Muestra los ejes y la grafica; luego, 
espera a que se pulse el boton Aceptar 
y cierra la ventana <p>

Avanzada    <p>
--------    <p>
new Grafica (TituloGrafica, TituloEjeX, TituloEjeY)  <p>
Version del constructor con opciones de titulos <p>
Usar strings vacios si no se necesitan todos <p>

otraGrafica()          Comienza una nueva grafica con los mismos ejes <p>
Se puede usar un unico pinta() para todas  <p>
las graficas. <p>

ponColor(int 0 a 3)   Se puede elegir negro, rosa, azul o rojo <p>
(hay constantes estaticas disponibles para usar <p>
nombres en vez de numeros) <p>

ponSimbolo(boolean)   Pone simbolos en la grafica actual. <p>
el simbolo se deduce del color <p>

ponSimbolo(int 0 a 3)  Pone simbolos en la grafica actual si no hay <p>
colores definidos. Los simbolos disponibles son: <p>
circulo, triangulo, trianguloInvertido, cuadrado <p>
(hay constantes estaticas disponibles para usar <p>
nombres en vez de numeros) <p>

ponLineas(boolean)     Quita o pone la opcion de pintar la grafica  <p>
con lineas. <p>
Normalmente la opcion de lineas esta activa <p>

ponTitulo(String)      Pone el titulo de la grafica actual <p>
@author Michael Gonzalez Harbour <mgh at unican dot es>
@author Mariano Benito Hoz <mbenitohoz at gmail dot com>
@version 3.4 
 */

public class Grafica extends JFrame implements ActionListener {

    /**
     * Constantes estaticas que definen los colores disponibles
     */
    public static final int rojo = 3;
    public static final int azul = 2;
    public static final int rosa = 1;
    public static final int negro = 0;
    /**
     * Constantes estaticas que definen los simbolos disponibles
     */
    public static final int circulo = negro;
    public static final int triangulo = azul;
    public static final int trianguloInvertido = rosa;
    public static final int cuadrado = rojo;

    // Constantes que definen los parametros del dibujo
    private static final int MAX_GRAPHS=4;
    private static final Point lowerLeft = new  Point(50.0,430.0);
    private static final Point upperRight = new Point(600.0,25.0);
    private static final Point plotSize = new   Point(640.0,480.0);
    private static final double symbolRadius  = 4.0;
    private static final int cs = 3; // pixel size of a symbol
    private static Font fontTimes=new Font("Sans",Font.PLAIN,9);
    private static Font fontVerdana=new Font("Verdana",Font.PLAIN,10);
    private static Stroke stroke2= new BasicStroke(2.0f);
    private static Stroke stroke1= new BasicStroke(1.0f);

    private static final long serialVersionUID = 3918001L;
    
    // atributos de la clase
    private String xAxisTitle, yAxisTitle, graphTitle;
    private boolean keys;
    private boolean hasData = false;
    private JButton okButton, closeButton;
    private Watcher okWatcher = new Watcher();
    private PanelDibujos pDibujo;
    private Image imagen;
    private Graphics2D grafica2; 

    private Point min=new Point(Double.MAX_VALUE, Double.MAX_VALUE);
    private Point max=new Point(-Double.MAX_VALUE, -Double.MAX_VALUE);
    private Point scale=new Point(0.0,0.0);
    private Point orig=new Point(0.0,0.0);
    private Point trans=new Point(0.0,0.0);
    
    // Clase privada que define la forma de pintar un panel 
    private class PanelDibujos extends JPanel {

        @Override
        public void paint(Graphics g) {
            // pinta la imagen donde hemos hecho el dibujo
            g.drawImage(imagen,0,0,null);
        }
    }

    // Convertir de unidades de usuario a unidades de pantalla (eje X)
    private double toDeviceX(double userX) {
        return userX*scale.x+trans.x;
    }

    // Convertir de unidades de usuario a unidades de pantalla (eje Y)
    private double toDeviceY(double userY) {
        return userY*scale.y+trans.y;
    }

    // Convertir de unidades de usuario a unidades de pantalla (Puntos)
    private void userToDevice(Point source, Point dest) {
        dest.x=toDeviceX(source.x);
        dest.y=toDeviceY(source.y);
    }

    // Obtener un numero "bonito" en forma de texto
    private String floatImage(double x) {
        if (Math.abs(x)>=1.0E6) {
            return String.format("%.1e",x).trim();
        } else if (Math.abs(x)>=1000.0) {
            return String.format("%.1f",x).trim();
        } else if (Math.abs(x)>=1.0) {
            return String.format("%.1f",x).trim();
        } else if (Math.abs(x)>=0.001) {
            return String.format("%.4f",x).trim();
        } else if (Math.abs(x)==0.0) {
            return String.format("%.1f",x).trim();
        } else {
            return String.format("%.1g",x).trim();
        }
    }

    /**
     * Constructor simple, que deja los titulos de la ventana y de los
     * ejes en blanco.
     */
    public Grafica() {
        super();
        initializeGraph();
        xAxisTitle = "";
        yAxisTitle = "";
        graphTitle = "";
    }

    /**
     * Constructor alternativo, que pone los titulos de la ventana
     * y de los ejes.
     */
    public Grafica(String g, String x, String y) {
        super();
        graphTitle = g;
        initializeGraph();
        xAxisTitle = x;
        yAxisTitle = y;
    }

    // Redibuja las graficas
    private void redraw() {
        repintaImagen(grafica2);
        pDibujo.repaint();
    }

    // Clase sencilla para almacenar la informacion basica del conjunto 
    // de puntos
    private static class Dataset {
        int count;
        int plotType;
        String title;
        boolean colorRequired, symbolRequired, titleRequired, lineRequired;
    }

    private Vector<Point> points;
    private Vector<Dataset> datasets;
    private int currentDataset = -1;

    // Metodo privado que inicializa el grafico
    private void initializeGraph() {
        datasets = new Vector<Dataset>(10, 10);
        otraGrafica();
        points = new Vector<Point>(100, 100);
        keys = false;
        super.setTitle(graphTitle);
        Panel p = new Panel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        okButton = new JButton("Aceptar");
        okButton.addActionListener(this);
        okButton.setEnabled(false);
        p.add(okButton);
        closeButton = new JButton("Cerrar Aplicacion");
        closeButton.addActionListener(this);
        closeButton.setEnabled(true);
        p.add(closeButton);
        addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

        pack();

        pDibujo = new PanelDibujos();
        pDibujo.setPreferredSize
	    (new Dimension((int)plotSize.x, (int)plotSize.y));
        add(pDibujo, BorderLayout.CENTER);
        add(p, BorderLayout.SOUTH);
        pack();

        Dimension size= pDibujo.getSize();
        imagen = pDibujo.createImage(size.width, size.height);
        grafica2= (Graphics2D)imagen.getGraphics();
        grafica2.setColor(Color.white);
        grafica2.fillRect(0, 0, size.width, size.height);

    }

    /**
     *  Permite crear una nueva grafica en la misma ventana.
     */
    public void otraGrafica() throws IndexOutOfBoundsException {
        if (currentDataset>=MAX_GRAPHS-1) {
            Mensaje mens=new Mensaje("Error");
            mens.escribe("Se ha superado el numero maximo de graficas");
            throw new IndexOutOfBoundsException();
        }
        Dataset d = new Dataset();
        d.count = 0;
        d.plotType = negro;
        d.title = "";
        d.symbolRequired = false;
        d.colorRequired = false;
        d.titleRequired = false;
        d.lineRequired = true;
        datasets.add(d);
        currentDataset++;
    }

    /**
     * Pone el color de la grafica al valor indicado por c, que debe ser
     * un entero de cero a tres, o una de las constantes estaticas
     * rojo, azulo, rosa, negro.
     */
    public void ponColor(int c) {
        datasets.elementAt(currentDataset).colorRequired = true;
        datasets.elementAt(currentDataset).plotType = c;
    }

    /**
     * Pone o quita la opcion de simbolos en la grafica.
     * El simbolo se elige en funcion del color.
     */
    public void ponSimbolo(boolean b) {
        datasets.elementAt(currentDataset).symbolRequired = b;
    }

    /**
     * Pone el simbolo de la grafica al valor indicado por c, si no
     * se ha especificado color. Si se ha especificado color,
     * el simbolo se elige en funcion del color. C debe ser un entero
     * entre 0 y 3 o una de las constantes estaticas: cuadrado, triangulo,
     * circulo, trianguloInvertido.
     */
    public void ponSimbolo(int c) {
        datasets.elementAt(currentDataset).symbolRequired = true;
        datasets.elementAt(currentDataset).plotType = c;
    }

    /**
     * Pone el titulo de la grafica
     */
    public void ponTitulo(String s) {
        datasets.elementAt(currentDataset).titleRequired = true;
        datasets.elementAt(currentDataset).title = s;
        keys = true;
    }

    /**
     * Pone o quita la opcion de mostrar la grafica con lineas entre
     * cada punto. Por omision la grafica muestra las lineas.
     */
    public void ponLineas(boolean b) {
        datasets.elementAt(currentDataset).lineRequired = b;
        if (b == false) {
            datasets.elementAt(currentDataset).symbolRequired = true;
        }
    }

    /**
     * Inserta el punto (x,y) en la grafica actual. Los puntos deben
     * darse ordenados por eje X
     */
    public void inserta(double x, double y) {
        points.add(new Point(x, y));
        datasets.elementAt(currentDataset).count++;
        if (x > max.x) {
            max.x = x;
        }
        if (x < min.x) {
            min.x = x;
        }
        if (y > max.y) {
            max.y = y;
        }
        if (y < min.y) {
            min.y = y;
        }
        hasData = true;
    }

    /**
     *  Pinta todas las graficas y espera a que se pulse el boton aceptar
     */
    public void pinta() {
        if (points.size()>=2) {
            redraw();
            setVisible(true);
            okButton.setEnabled(true);
            this.setVisible(true);
            okWatcher.watch();
            this.setVisible(false);
        } else {
            msjError("No hay suficientes puntos para pintar la grafica");
        }
    }

    // Clase simple para guardar unas coordenadas x y
    private static class Point {

        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    //Dibuja un numero
    private void printNumber(double num, double x, double y, 
    boolean vertical, Graphics2D g) 
    {
        int x1=(int)Math.round(x);
        int y1=(int)Math.round(y);
        g.setFont(fontTimes);
        String txt=floatImage(num);
        int w=g.getFontMetrics().stringWidth(txt);
        int h=g.getFontMetrics().getAscent();
        if (vertical) {
            AffineTransform orig = g.getTransform();
            g.rotate(-Math.PI/2);
            g.drawString(txt,-(y1+w/2),x1);
            g.setTransform(orig);
        } else {
            g.drawString(txt,x1-w/2,y1);
        }
    }

    // Dibuja un simbolo
    private void printSymbol(double x, double y,
    int symbol, Graphics2D g) 
    {
        int x1=(int)Math.round(x);
        int y1=(int)Math.round(y);
        switch (symbol) {
            case negro:
            g.drawOval(x1 - cs, y1 - cs, 2 * cs, 2 * cs);
            break;
            case rosa: 
            g.drawPolygon(trix(x1), uptriy(y1), 3);
            break;
            case azul: 
            g.drawPolygon(trix(x1), triy(y1), 3);
            break;
            case rojo: 
            g.drawRect(x1 - cs, y1 - cs, 2 * cs, 2 * cs);
            break;
        }
    }

    // Dibuja la leyenda con los titulos
    private void drawTitles(Graphics2D g) {
        Dataset d;
        double titlePos=lowerLeft.x+20.0;
        g.setFont(fontVerdana);
        int h=g.getFontMetrics().getAscent();

        for (int cd = 0; cd <= currentDataset; cd++) {
            d = datasets.elementAt(cd);
            int w=g.getFontMetrics().stringWidth(d.title);
            if (d.colorRequired) {
                changeColor(g, d.plotType);
            }
            if (d.symbolRequired) {
                printSymbol(titlePos+12.5, plotSize.y - (3*h)/2, d.plotType, g);
            }
            titlePos=titlePos+25.0;
            g.drawString(d.title, (int) Math.round(titlePos), 
			 (int) Math.round(plotSize.y-h));
            titlePos=titlePos+w+20.0;
        }
        g.setColor(Color.black);
    }

    // obtain a nice number that avoids rounding errors near zero
    private double nice (double x, double count) {
        double adjust=x+count*1000.0;
        return adjust -count*1000.0;
    }

    // Dibuja los ejes
    private void drawAxes(Graphics2D g) {
        Point factor=new Point(0.0,0.0);
        Point interval=new Point(0.0,0.0);
        Point number=new Point(0.0,0.0);
        Point step=new Point(0.0,0.0);
        Point bigStep=new Point(0.0,0.0);
        int tickX, tickY, bigTickX, bigTickY;
        
        // Calculate Intervals of X and Y points
        interval.x=max.x-min.x;

        if (Math.abs(interval.x)<Double.MIN_VALUE*100.0) {
            interval.x=1.0;
        }

        interval.y=max.y-min.y;
        if (Math.abs(interval.y)<Double.MIN_VALUE*100.0) {
            interval.y=1.0;
        }

        // Adjust intervals to "nice" numbers:
        // get 2 significant digits (10-99)
        factor.x= Math.pow(10.0,Math.floor
            (Math.log(interval.x)/Math.log(10.0))-1);
        number.x=interval.x/factor.x;
        factor.y= Math.pow(10.0,Math.floor
            (Math.log(interval.y)/Math.log(10.0))-1);
        number.y=interval.y/factor.y;
        int twoDigitsX=(int)Math.round(number.x);
        int twoDigitsY=(int)Math.round(number.y);
        

        // get tick and big_tick by rounding up the interval(from 10
        // to 20 ticks) Value tick big_tick
        if (twoDigitsX>=10 && twoDigitsX<=16) {
            tickX   =1;
            bigTickX=2;
        } else if (twoDigitsX>=17 && twoDigitsX<=20) {
            tickX   =1;
            bigTickX=5;
        } else if (twoDigitsX>=21 && twoDigitsX<=40) {
            tickX   =2;
            bigTickX=5;
        } else if (twoDigitsX>=41 && twoDigitsX<=60) {
            tickX   =5;
            bigTickX=10;
        } else if (twoDigitsX>=61 && twoDigitsX<=99) {
            tickX   =5;
            bigTickX=20;
        } else {
            tickX=(int) Math.round(interval.x/20.0);
            bigTickX=(int) Math.round(interval.x/5.0);
        }

        if (twoDigitsY>=10 && twoDigitsY<=16) {
            tickY   =1;
            bigTickY=2;
        } else if (twoDigitsY>=17 && twoDigitsY<=20) {
            tickY   =1;
            bigTickY=5;
        } else if (twoDigitsY>=21 && twoDigitsY<=40) {
            tickY   =2;
            bigTickY=5;
        } else if (twoDigitsY>=41 && twoDigitsY<=60) {
            tickY   =5;
            bigTickY=10;
        } else if (twoDigitsY>=61 && twoDigitsY<=99) {
            tickY   =5;
            bigTickY=20;
        } else {
            tickY=(int) Math.round(interval.y/20.0);
            bigTickY=(int) Math.round(interval.y/5.0);
        }

        // Calculate step and big step for each tick
        step.x=factor.x*tickX;
        step.y=factor.y*tickY;
        bigStep.x=factor.x*bigTickX;
        bigStep.y=factor.y*bigTickY;

        //  Round up max to next tick
        max.x=Math.ceil(max.x/step.x)*step.x;
        max.y=Math.ceil(max.y/step.y)*step.y;
        

        //  Round down min to previous tick: min=floor(min/tick)*tick
        min.x=Math.floor(min.x/step.x)*step.x;
        min.y=Math.floor(min.y/step.y)*step.y;

        // X axis origin: 
        if (min.y<=0.0 && 0.0<=max.y) {
            orig.y=0.0;
        } else {
            orig.y=min.y;
        }

        // Y axis origin: 
        if (min.x<=0.0 && 0.0<=max.x) {
            orig.x=0.0;
        } else {
            orig.x=min.x;
        }

        //  Configure Drawing area:
        //      full: 640x480, actual plot: 620x460 Plot
        //      Set origin and scale so that min_X, max_X, min_Y, max_Y 
        //      correspond to:
        //           Upper_Right:{20, 460}, Lower_Left=( 620, 20);
        scale.x=(upperRight.x-lowerLeft.x)/(max.x-min.x);
        scale.y=(upperRight.y-lowerLeft.y)/(max.y-min.y);
        trans.x=lowerLeft.x-min.x*scale.x;
        trans.y=lowerLeft.y-min.y*scale.y;

        //  Paint X axis:      
        //  Paint line from  (x_min,orig_y) to (x_max,orig_y)
        g.setColor(Color.black);
        g.setStroke(stroke2);
        g.drawLine((int) Math.round(toDeviceX(min.x)),
		   (int) Math.round(toDeviceY(orig.y)),
		   (int) Math.round(toDeviceX(max.x)), 
		   (int) Math.round(toDeviceY(orig.y)));

        // paint small ticks
        g.setStroke(stroke1);
        double x=min.x;
        double lineLength=8.0;
        while (x<=max.x+(max.x-min.x)/10000.0) {
            g.drawLine((int) Math.round(toDeviceX(x)),
		       (int) Math.round(toDeviceY(orig.y)),
		       (int) Math.round(toDeviceX(x)), 
		       (int) Math.round(toDeviceY(orig.y)+lineLength));
            x=x+step.x;
        }

        g.setFont(fontTimes);
        int h=g.getFontMetrics().getAscent();
        // Paint large ticks with numbers
        g.setStroke(stroke2);
        x=min.x;
        lineLength=12.0;
        while (x<=max.x+(max.x-min.x)/10000.0) {
            g.drawLine((int) Math.round(toDeviceX(x)), 
		       (int) Math.round(toDeviceY(orig.y)),
		       (int) Math.round(toDeviceX(x)), 
		       (int) Math.round(toDeviceY(orig.y)+lineLength));
            printNumber(nice(x,bigStep.x),toDeviceX(x),
			toDeviceY(orig.y)+lineLength+h+1,false,g);
            x=x+bigStep.x;
        }

        // print x axis title
        g.setFont(fontVerdana);
        int w=g.getFontMetrics().stringWidth(xAxisTitle);
        h=g.getFontMetrics().getAscent();
        g.drawString(xAxisTitle,(int) Math.round(plotSize.x-w-5.0),
            (int) Math.round(toDeviceY(orig.y)+lineLength+2*h+3)); 

        //  Paint Y axis:      
        //  Paint line from  (Orig_x,y_min) to (Orig_x,Y_Max)
        g.setStroke(stroke2);
        g.drawLine((int) Math.round(toDeviceX(orig.x)), 
		   (int) Math.round(toDeviceY(min.y)),
		   (int) Math.round(toDeviceX(orig.x)), 
		   (int) Math.round(toDeviceY(max.y)));

        // paint small ticks
        g.setStroke(stroke1);
        double y=min.y;
        lineLength=8.0;
        while (y<=max.y+(max.y-min.y)/10000.0) {
            g.drawLine((int) Math.round(toDeviceX(orig.x)), 
		       (int) Math.round(toDeviceY(y)),
		       (int) Math.round(toDeviceX(orig.x)-lineLength), 
		       (int) Math.round(toDeviceY(y)));
            y=y+step.y;
        }

        // Paint large ticks with numbers
        g.setStroke(stroke2);
        y=min.y;
        lineLength=12.0;
        g.setFont(fontTimes);
        h=g.getFontMetrics().getAscent();

        while (y<=max.y+(max.y-min.y)/10000.0) {
            g.drawLine((int) Math.round(toDeviceX(orig.x)), 
		       (int) Math.round(toDeviceY(y)),
		       (int) Math.round(toDeviceX(orig.x)-lineLength), 
		       (int) Math.round(toDeviceY(y)));
            printNumber(nice(y,bigStep.y),
			toDeviceX(orig.x)-lineLength-h-1.0,
			toDeviceY(y),true,g);
            y=y+bigStep.y;
        }

        // print Y axis title
        g.setFont(fontVerdana);
        w=g.getFontMetrics().stringWidth(yAxisTitle);
        h=g.getFontMetrics().getAscent();
        AffineTransform origTransform = g.getTransform();
        g.rotate(-Math.PI/2);
        g.drawString(yAxisTitle, -(w+5),
		     (int)Math.round(toDeviceX(orig.x)-lineLength-2*h-2)); 
        g.setTransform(origTransform);


        // Print the graph titles
        if (keys) {
            drawTitles(g);
        }
    }

    /**
    Pinta la imagen con las graficas
     */
    public void repintaImagen(Graphics2D g) {
        g.clearRect(0,0,640,480);
        // only paint graphs if there are enough points
        if (points.size()>=2) {
            drawAxes(g);
            plotGraphs(g);
        }
    }

    // Cambiar de color
    private void changeColor(Graphics2D g, int c) {
        //------------------------
        switch (c) {
            case negro: {
                g.setColor(Color.black);
                break;
            }
            case rosa: {
                g.setColor(Color.magenta);
                break;
            }
            case azul: {
                g.setColor(Color.blue);
                break;
            }
            case rojo: {
                g.setColor(Color.red);
                break;
            }
        }
    }

    // Dibuja las graficas
    private void plotGraphs(Graphics2D g) {
        Point p, q;
        Dataset d;
        boolean lastset;
        Color c;

        g.setStroke(stroke2);

        // Loop through each dataset
        /* The points are in one big list, split by the
        counts recorded in each dataset */
        int currentPoint = 0;

        for (int cd = 0; cd <= currentDataset; cd++) {
            d = datasets.elementAt(cd);
            if (d.colorRequired) {
                changeColor(g, d.plotType);
            }
            // Start with the first point in the list
            // for this graph
            if (currentPoint<points.size()) {
                // print the first symbol
                p = points.elementAt(currentPoint);
                if (d.symbolRequired) {
                    printSymbol(toDeviceX(p.x),toDeviceY(p.y), d.plotType, g);
                }
                currentPoint++;

                // Loop through the remaining points as stored in the list
                for (int i = 1; i < d.count; i++) {
                    q = points.elementAt(currentPoint);
                    //x2 = scaleX(q.xCoord);
                    //y2 = scaleY(q.yCoord);

                    // plot the line and/or point
                    if (d.lineRequired) {
                        g.drawLine((int)Math.round(toDeviceX(p.x)), 
				   (int)Math.round(toDeviceY(p.y)),
				   (int)Math.round(toDeviceX(q.x)), 
				   (int)Math.round(toDeviceY(q.y)));
                    }
                    if (d.symbolRequired) {
                        printSymbol((int)Math.round(toDeviceX(q.x)), 
				    (int)Math.round(toDeviceY(q.y)), 
				    d.plotType, g);
                    }
                    p=q;
                    currentPoint++;
                }
            }
        }
    }

    // coordenadas X de un triangulo
    private int[] trix(int p) {
        int[] a = new int[3];
        a[0] = p - cs;
        a[1] = p + cs;
        a[2] = p;
        return a;
    }

    // coordenadas Y de un triangulo
    private int[] triy(int p) {
        int[] a = new int[3];
        a[0] = p + cs;
        a[1] = p + cs;
        a[2] = p - cs;
        return a;
    }

    // coordenadas Y de un triangulo hacia arriba
    private int[] uptriy(int p) {
        int[] a = new int[3];
        a[0] = p - cs;
        a[1] = p - cs;
        a[2] = p + cs;
        return a;
    }

    /**
     *  No usar directamente esta operacion interna de la clase
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            okWatcher.ready();
        } else if (e.getSource() == closeButton) {
            System.exit(0);
        }
    }

    class Watcher {

        private boolean ok;

        Watcher() {
            ok = false;
        }

        synchronized void watch() {
            while (!ok) {
                try {
                    wait(500);
                } catch (InterruptedException e) {
                }
            }
            ok = false;
        }

        synchronized void ready() {
            ok = true;
            notify();
        }
    }

    // Pone un mensaje de error en una ventana
    private void msjError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error",
            JOptionPane.ERROR_MESSAGE);
    }

}
