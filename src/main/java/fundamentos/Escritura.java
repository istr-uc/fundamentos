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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/** Escritura      por Michael Gonzalez y Mariano Benito.
 * Version 3.5. Octubre 2016

 Es una clase sencilla que permite escribir varios valores en una ventana
 Los valores pueden ser int, double o String<p>
 
 Interfaz<p>
 ========<p>
 
 new Escritura (titulo) - crea el objeto con su titulo<p>
 insertaValor (etiqueta, valor) - crea una caja con la etiqueta
 y valor indicados<p>
 espera()  - espera a que se pulse el boton OK y cierra la ventana <p>
 @author Michael Gonzalez Harbour <mgh at unican dot es>
 @author Mariano Benito Hoz <mbenitohoz at gmail dot com>
 @version 3.4
*/

public class Escritura extends JFrame implements ActionListener {

    private JPanel panelCasillas,panelCentro;
    private JButton BtAceptar, BtCerrar;
    private Border loweredetched;

    /**
     * Crea una ventana de Escritura.
     *
     * @param titulo Titulo de la ventana.
     */
    public Escritura(String titulo) {
        super(titulo);
        panelCasillas = new JPanel(new GridLayout(0, 2, 10, 10));
        BtAceptar = new JButton("Aceptar");
        BtCerrar = new JButton("Cerrar aplicacion");

        inicializa();
    }

    // finaliza la inicializacion del constructor
    private void inicializa() {
        // borde resaltado
        loweredetched = 
            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        // Ventana
        JScrollPane centro = new JScrollPane
            (panelCasillas,
             JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
             JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        centro.setPreferredSize(new Dimension(480, 300));

        // margen
        Border margen = BorderFactory.createEmptyBorder(10,10,10,10);
        panelCentro=new JPanel();
        panelCentro.add(centro);
        panelCentro.setBorder(margen);

        JPanel inferior = new JPanel (new FlowLayout(FlowLayout.CENTER, 2, 0));

        inferior.add(BtAceptar);
        inferior.add(BtCerrar);

        setLayout(new BorderLayout(5, 5));

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
     * Espera a que los datos sean mostrados.
     */
    public synchronized void espera() {
        setVisible(true);
        try {
            wait();
        } catch (InterruptedException e) {
        }
    }

    // Inserta una casilla compuesta por etiqueta y valor, en la ventana
    private void insertaCasilla(String clave, JTextField valor) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.add(new JLabel(clave + ":"));
        panelCasillas.add(p);
        p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        valor.setBorder(loweredetched);
        p.add(valor);
        panelCasillas.add(p);
    }

    /**
     * Muestra una pareja de etiqueta - valor para un valor de tipo String.
     *
     * @param etiqueta Texto Identificativo.
     * @param valor Valor a mostrar.
     */
    public void insertaValor(String etiqueta, String valor) {
        JTextField texto = new JTextField(Math.max(15, valor.length()));
        texto.setText(valor);
        texto.setEditable(false);
        insertaCasilla(etiqueta, texto);
    }

    /**
     * Muestra una pareja de etiqueta - valor para un valor de tipo entero.
     *
     * @param etiqueta Texto Identificativo.
     * @param numero Valor a mostrar.
     */
    public void insertaValor(String etiqueta, int numero) {
        String numeroString = Integer.toString(numero);
        JTextField texto = new JTextField(Math.max(15, numeroString.length()));
        texto.setText(numeroString);
        texto.setEditable(false);
        insertaCasilla(etiqueta, texto);
    }

    /**
     * Muestra una pareja de etiqueta - valor para un valor de tipo Real.
     *
     * @param etiqueta Texto Identificativo.
     * @param real Valor a mostrar.
     */
    public void insertaValor(String etiqueta, double real) {
        String realString = Double.toString(real);
        JTextField texto = new JTextField(Math.max(15, realString.length()));
        texto.setText(realString);
        texto.setEditable(false);
        insertaCasilla(etiqueta, texto);
    }

    /**
     * No usar este metodo interno
     */
    public synchronized void actionPerformed(ActionEvent e) {
        notify();
        setVisible(false);
    }
}
