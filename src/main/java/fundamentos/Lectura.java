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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.JOptionPane;

/** Lectura      por Michael Gonzalez y Mariano Benito.
 * Version 3.5. Octubre 2016

 Es una clase sencilla para leer datos introducidos por teclado,
 usando una ventana <p>

 Los valores se introducen en ventanas creadas al efecto<p>

 Los valores leidos pueden ser del tipo String, double u int<p>

 Interfaz  <p>
 ========  <p>
 new Lectura (titulo)  - crea una ventana con su titulo<p>
 println (string)      - muestra un string en la cabecera <p>
 creaEntrada (etiqueta, valor) - crea una caja para leer un valor;<p>
 el valor puede ser double, int o String<p>
 espera (mensaje)      - muestra un mensaje, y espera a que el
 usuario teclee datos y pulse aceptar<p>
 esperaYCierra (mensaje) - igual que espera, pero ademas cierra la <p>
 ventana al aceptar <p>
 espera ()             - espera a que el usuario teclee datos y
 pulse aceptar<p>
 esperaYCierra ()      - igual que espera, pero ademas cierra la <p>
 ventana al aceptar <p>
 leeDouble (label)     - lee un double de la caja con esa etiqueta<p>
 leeInt (label)        - lee un int de la caja con esa etiqueta<p>
 leeString (label)     - lee un String de la caja con esa etiqueta<p>

 * @author Michael Gonzalez Harbour <mgh at unican dot es>
 * @author Mariano Benito Hoz <mbenitohoz at gmail dot com>
 * @version 3.4
 */

public class Lectura extends JFrame implements ActionListener {

    private JTextArea textoSup;
    private JPanel panelTexto, panelCentro;
    private JPanel panelCasillas;
    private JButton BtAceptar, BtCerrar;
    private Border loweredetched;
    private HashMap<String, JTextField> parejas;

    /**
     * Crea una ventana de Lectura.
     * 
     * @param titulo Titulo de la ventana.
     */
    public Lectura(String titulo) {
        super(titulo);
        panelTexto = new JPanel();
        Border margen = BorderFactory.createEmptyBorder(10,10,10,10);
        panelTexto.setBorder(margen);
        textoSup = new JTextArea("", 4, 24);
        loweredetched = 
            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        textoSup.setBorder(loweredetched);
        panelCasillas = new JPanel(new GridLayout(0, 2, 10, 10));
        BtAceptar = new JButton("Aceptar");
        BtCerrar = new JButton("Cerrar aplicacion");
        parejas = new HashMap<String, JTextField>(5);

        inicializa();
    }

    // finaliza la inicializacion del constructor
    private void inicializa() {
        // Ventana
        textoSup.setEditable(false);
        JScrollPane superior = new JScrollPane
            (textoSup,
             JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
             JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        superior.setPreferredSize(new Dimension(480, 100));
        panelTexto.add(superior);
        JScrollPane centro = new JScrollPane
            (panelCasillas,
             JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
             JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        centro.setPreferredSize(new Dimension(480, 300));

        // margen
        Border margen = BorderFactory.createEmptyBorder(0,10,10,10);
        panelCentro=new JPanel();
        panelCentro.add(centro);
        panelCentro.setBorder(margen);


        JPanel inferior = new JPanel
            (new FlowLayout(FlowLayout.CENTER, 2, 0));
        inferior.add(BtAceptar);
        inferior.add(BtCerrar);

        setLayout(new BorderLayout(5, 5));

        add(panelTexto, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);

        pack();

        // Manejadores
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        BtCerrar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        BtAceptar.addActionListener(this);
    }

    /**
     * Espera a que los datos sean introducidos.
     */
    public synchronized void espera() {
        setVisible(true);
        try {
            wait();
        } catch (InterruptedException e) {
        }
    }

    /**
     *  Muestra un mensaje y espera a que los datos sean introducidos
     *
     * @param texto Mensaje a mostrar.
     */
    public synchronized void espera(String texto) {
        println("\n" + texto);
        espera();
    }

    /**
     * Espera a que los datos sean introducidos y cierra la ventana.
     */
    public synchronized void esperaYCierra() {
        espera();
        setVisible(false);
    }

    /**
     * Muestra un mensaje, espera a que los datos sean introducidos, 
     * y cierra la ventana
     * 
     * @param texto Mensaje a mostrar.
     */
    public synchronized void esperaYCierra(String texto) {
        espera(texto);
        setVisible(false);
    }

    /**
     * Muestra un mensaje en la parte superior de la ventana.
     *
     * @param texto Mensaje a mostrar.
     */
    public void println(String texto) {
        textoSup.append("\n" + texto);
        textoSup.setCaretPosition(textoSup.getCaretPosition()+texto.length());
    }

    // Inserta una pareja de etiqueta y casilla de texto
    private void insertaCasilla(String etiqueta, JTextField valor) {
        if (parejas.containsKey(etiqueta)) {
            msjError("La entrada " + etiqueta + " esta repetida");
        } else {
            parejas.put(etiqueta, valor);
            JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            p.add(new JLabel(etiqueta + ":"));
            panelCasillas.add(p);
            p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p.add(valor);
            panelCasillas.add(p);
        }
    }

    /**
     * Crea un campo para leer un String.
     *
     * @param etiqueta Identificador del campo.
     * @param valor Valor por defecto.
     */
    public void creaEntrada(String etiqueta, String valor) {
        JTextField texto = new JTextField(Math.max(15, valor.length()));
        texto.setText(valor);
        insertaCasilla(etiqueta, texto);
    }

    /**
     * Crea un campo para leer un numero entero.
     *
     * @param etiqueta Identificador del campo.
     * @param numero Valor por defecto.
     */
    public void creaEntrada(String etiqueta, int numero) {
        String numeroString = Integer.toString(numero);
        JTextField texto = new JTextField(Math.max(15, numeroString.length()));
        texto.setText(numeroString);
        insertaCasilla(etiqueta, texto);
    }

    /**
     * Crea un campo para leer un numero real.
     *
     * @param etiqueta Identificador del campo.
     * @param real Valor por defecto.
     */
    public void creaEntrada(String etiqueta, double real) {
        String realString = Double.toString(real);
        JTextField texto = new JTextField(Math.max(15, realString.length()));
        texto.setText(realString);
        insertaCasilla(etiqueta, texto);
    }

    /**
     * Lee el String asociado a la etiqueta pasada como argumento.
     *
     * @param etiqueta Identificador del campo.
     * @return String asociado a esa etiqueta.
     * @throws NullPointerException Lanzada si no se encuentra la etiqueta.
     */
    public String leeString(String etiqueta) throws NullPointerException {
        JTextField texto = parejas.get(etiqueta);
        try {
            return texto.getText();
        } catch (NullPointerException e) {
            msjError("La entrada " + etiqueta + " no existe");
            throw e;
        }
    }

    /**
     * Lee el numero real asociado a la etiqueta pasada como argumento.
     *
     * @param etiqueta Identificador del campo.
     * @return Real asociado a la etiqueta.
     * @throws NullPointerException Lanzada si no se encuentra la etiqueta.
     * @throws NumberFormatException Lanzada si el numero no tiene un formato
     * correcto.
     */
    public double leeDouble(String etiqueta) throws NullPointerException,
                                                    NumberFormatException {
        JTextField texto = parejas.get(etiqueta);
        try {
            return Double.valueOf(texto.getText()).doubleValue();
        } catch (NumberFormatException e) {
            msjError("Real en la entrada " + etiqueta + 
                     " con formato incorrecto");
            throw e;
        } catch (NullPointerException e) {
            msjError("La entrada " + etiqueta + " no existe");
            throw e;
        }
    }

    /**
     * Lee el numero entero asociado a la etiqueta pasada como argumento.
     *
     * @param etiqueta Identificador del campo.
     * @return Entero asociado a la etiqueta.
     * @throws NullPointerException Lanzada si no se encuentra la etiqueta.
     * @throws NumberFormatException Lanzada si el numero no tiene un formato
     * correcto.
     */
    public int leeInt(String etiqueta) throws NullPointerException,
                                              NumberFormatException {
        JTextField texto = parejas.get(etiqueta);
        try {
            return Integer.valueOf(texto.getText()).intValue();
        } catch (NumberFormatException e) {
            msjError("Entero en la entrada " + etiqueta + 
                     " con formato incorrecto");
            throw e;
        } catch (NullPointerException e) {
            msjError("La entrada " + etiqueta + " no existe");
            throw e;
        }
    }

    /**
     * No usar este metodo interno
     */
    public synchronized void actionPerformed(ActionEvent e) {
        notify();
    }

    // Pone un mensaje de error en una ventana
    private void msjError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error",
                                      JOptionPane.ERROR_MESSAGE);
    }

}
