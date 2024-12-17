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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.JOptionPane;

/** CajaTexto por Michael Gonzalez y Mariano Benito. 
 *  Version 3.5. Octubre 2016<p>

    Es una clase sencilla para leer datos introducidos por teclado,
    usando una ventana de texto multilinea<p>

    Los valores leidos son del tipo String, entero o real<p>

    Interfaz simple <p>
    ==============  <p>
    new CajaTexto (String titulo,int filas,int columnas)   <p>
    - crea una ventana con su titulo y el numero de
    filas y columnas indicado
    (medidos en caracteres)<p>
    void espera()         - espera a que el usuario teclee datos y pulse
    aceptar<p>
    boolean hayMas()      - retorna un booleano indicando si hay mas
    lineas por leer o n <p>
    String leeString ()   - retorna un String que corresponde a la linea
    actual <p>
    void avanzaLinea()    - avanza a la siguiente linea del texto <p>

    Interfaz avanzada <p>
    =================  <p>
    void esperaYCierra()  - espera a que el usuario teclee datos y pulse
    aceptar, cerrando ademas la ventana<p>
    void reinicia()       - se coloca en la primera linea del texto<p>
    int leeInt()          - retorna un entero que corresponde a la linea
    actual <p>
    int leeInt(int pos)   - retorna un entero que es el que ocupa la posicion
    pos en la linea actual, que contiene varios
    enteros separados por espacios en blanco;
    el primer entero corresponde a pos=0<p>
    double leeDouble()    - retorna un real que corresponde a la linea
    actual <p>
    double leeDouble(int pos)
    - retorna un numero real que es el que ocupa la
    posicion pos en la linea actual, que contiene
    varios numeros reales separados por espacios
    en blanco; el primer numero corresponde a pos=0<p>
    void borra()          - borra el texto de la caja de texto <p>
    void println(String s)- anade al final del texto una linea <p>

    @author Michael Gonzalez Harbour <mgh at unican dot es>
    @author Mariano Benito Hoz <mbenitohoz at gmail dot com>
    @version 3.4
*/

public class CajaTexto extends JFrame implements ActionListener {

    private JButton BtAceptar, BtCerrar;
    private JPanel panelTexto;
    private JTextArea areaTexto;
    private boolean cierro;
    private String textoEscrito;
    private int indice;  // puntero de lectura del texto

    /**
     * Crea la ventana.
     *
     * @param titulo Titulo de la ventana.
     * @param filas Filas que tendra el texto.
     * @param columnas Columnas que tendra el texto.
     */
    public CajaTexto(String titulo, int filas, int columnas) {
        super(titulo);
        cierro = true;
        textoEscrito = "";
        indice = -1;
        BtAceptar = new JButton("Aceptar");
        BtCerrar = new JButton("Cerrar Aplicacion");
        panelTexto = new JPanel();
        Border margen = BorderFactory.createEmptyBorder(10,10,10,10);
        panelTexto.setBorder(margen);
        areaTexto = new JTextArea
            (filas, Math.max(columnas, titulo.length() + 9));
        Border loweredetched = 
            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        areaTexto.setBorder(loweredetched);
        panelTexto.add(areaTexto);
        inicializa();
    }

    private void inicializa() {
        // Ventana
        // Panel botones.
        JPanel inferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        inferior.add(BtAceptar);
        inferior.add(BtCerrar);

        add(inferior, BorderLayout.SOUTH);
        JScrollPane barras = new JScrollPane
            (panelTexto,
             ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
             ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(barras, BorderLayout.CENTER);
        pack();
        // Manejadores
        BtAceptar.addActionListener(this);
        BtCerrar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Espera a la pulsacion de aceptar sin cerrar la ventana.
     */
    public synchronized void espera() {
        setVisible(true);
        cierro = false;
        try {
            wait();
        } catch (InterruptedException e) {
        }
        indice = -1;
    }

    /**
     * Se coloca en la primera linea del texto
     */
    public void reinicia() {
        indice=-1; // antes del primer caracter
    }


    /**
     * Espera a la pulsacion de aceptar y cierra la ventana.
     */
    public synchronized void esperaYCierra() {
        setVisible(true);
        cierro = true;
        try {
            wait();
        } catch (InterruptedException e) {
        }
        indice = -1;
    }

    /**
     * Anade una linea al final del texto.
     *
     * @param linea Texto de la linea a anadir.
     */
    public void println(String linea) {
        areaTexto.append(linea + "\n");
    }

    /**
     * Borra el contenido del area de texto.
     */
    public void borra() {
        areaTexto.setText(null);
        textoEscrito = "";
        indice = -1;
    }

    /**
     * Retorna un booleano indicando si hay mas lineas por leer o no.
     * @return Hay mas lineas?
     */
    public boolean hayMas() {
        int finLinea = textoEscrito.indexOf('\n', indice + 1);
        return !((finLinea == -1) && indice >= textoEscrito.length() - 1);
    }

    /**
     * Retorna el entero que corresponde a la linea actual.
     * @return Entero escrito en la linea actual.
     */
    public int leeInt() {
        String linea = "";
        try {
            linea = leeString();
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            msjError("Entero con formato incorrecto en la linea de texto: "
                     + linea);
            throw e;
        } catch (NullPointerException e) {
            msjError("No hay mas lineas");
            throw e;
        }
    }

    /**
     * Retorna un entero que es el que ocupa la posicion pos en la linea
     * actual, que contiene varios enteros separados por espacios en
     * blanco; el primer entero corresponde a pos=0.
     * @param pos Indice de la columnas donde se encuentra el entero.
     * @return Entero leido de la columna pos
     */
    public int leeInt(int pos) {
        String linea = "";
        String palabra[];
        try {
            linea = leeString();
            palabra = linea.split("\\s+", 0);
            return Integer.parseInt(palabra[pos]);
        } catch (NumberFormatException e) {
            msjError("Entero con formato incorrecto en la linea de texto: "
                     + linea);
            throw e;
        } catch (NullPointerException e) {
            msjError("No hay mas lineas");
            throw e;
        } catch (ArrayIndexOutOfBoundsException e) {
            msjError("No hay " + (pos + 1) + " numeros en la linea");
            throw e;
        }
    }

    /**
     * Retorna el numero real que corresponde a la linea actual.
     * @return Numero real de la linea actual.
     */
    public double leeDouble() {
        String linea = "";
        try {
            linea = leeString();
            return Double.parseDouble(linea);
        } catch (NumberFormatException e) {
            msjError
                ("Numero real con formato incorrecto en la linea de texto: "
                 + linea);
            throw e;
        } catch (NullPointerException e) {
            msjError("No hay mas lineas");
            throw e;
        }
    }

    /**
     * Retorna un numero real que es el que ocupa la posicion pos en la linea
     * actual, que contiene varios numeros reales separados por espacios en
     * blanco; el primer numero corresponde a pos=0.
     * @param pos Indice del double a leer.
     * @return Double escrito en la columna pos
     */
    public double leeDouble(int pos) {
        String linea = "";
        String palabra[];
        try {
            linea = leeString();
            palabra = linea.split("\\s+", 0);
            return Double.parseDouble(palabra[pos]);
        } catch (NumberFormatException e) {
            msjError
                ("Numero real con formato incorrecto en la linea de texto: "
                 + linea);
            throw e;
        } catch (NullPointerException e) {
            msjError("No hay mas lineas");
            throw e;
        } catch (ArrayIndexOutOfBoundsException e) {
            msjError("No hay " + (pos + 1) + " numeros en la linea");
            throw e;
        }
    }

    /**
     * Avanza a la siguiente linea del texto.
     */
    public void avanzaLinea() {
        int finLinea = textoEscrito.indexOf('\n', indice + 1);
        if (finLinea == -1) {
            // ultima linea
            if (indice < textoEscrito.length() - 1) {
                // todavia queda texto
                indice = textoEscrito.length();
            } else {
                // no quedan mas lineas
                msjError("No hay mas lineas");
                throw new NullPointerException();
            }
        } else {
            // encontrado salto de linea
            indice = finLinea;
        }
    }

    /**
     * Retorna el String que corresponde a la linea actual.
     * @return Linea actual.
     */
    public String leeString() {
        String linea;
        int finLinea = textoEscrito.indexOf('\n', indice + 1);
        if (finLinea == -1) {
            // ultima linea
            if (indice < textoEscrito.length() - 1) {
                // todavia queda texto
                linea = textoEscrito.substring
                    (indice + 1, textoEscrito.length());
            } else {
                // no quedan mas lineas
                linea = null;
            }
        } else {
            // encontrado salto de linea
            if (finLinea > 0 && textoEscrito.charAt(finLinea - 1) == '\r') {
                //quitar el "retorno de carro (\r)"
                linea = textoEscrito.substring(indice + 1, finLinea - 1);
            } else {
                //La linea no tiene retorno de carro
                linea = textoEscrito.substring(indice + 1, finLinea);
            }
        }
        return linea;
    }

    /**
     * No usar directamente esta operacion interna de la clase
     */
    public synchronized void actionPerformed(ActionEvent e) {
        textoEscrito = areaTexto.getText();
        setVisible(!cierro);
        notify();
    }

    // Pone un mensaje de error en una ventana
    private void msjError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error",
                                      JOptionPane.ERROR_MESSAGE);
    }

}
