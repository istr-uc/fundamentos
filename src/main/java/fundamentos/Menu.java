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
import java.util.HashSet;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.JOptionPane;

/** Menu por Michael Gonzalez y Mariano Benito. Version 3.5. Octubre 2016

    Es una clase sencilla que ofrece facilidades para crear un menu
    de opciones, y poder elegir una de ellas.<p>

    Interfaz  <p>
    ========  <p>
    new Menu (title)      - crea el objeto Menu con su titulo<p>
    println (string)      - muestra un string en la cabecera<p>
    insertaOpcion (nombre, codigo)   - crea un boton con el nombre
    y codigo indicado <p>
    int leeOpcion ()      - espera a que se pulse un boton,
    y retorna el codigo del boton pulsado.<p>
    int leeOpcion (mensaje) - muestra un mensaje, espera a que se pulse un
    boton, y retorna el codigo del boton pulsado.<p>
    void cierra()         - cierra la ventana del menu.<p>

    @author Michael Gonzalez Harbour <mgh at unican dot es>
    @author Mariano Benito Hoz <mbenitohoz at gmail dot com>
    @version 3.4
*/

public class Menu extends JFrame implements ActionListener {

    private JTextArea textoSup;
    private JPanel panelTexto;
    private JPanel panelBotones;
    private JScrollPane superior;
    private JScrollPane centro;
    private JButton BtCerrar;
    private Vector<Interna> botones;
    private HashSet<String> identificadores;
    private int presionado;

    // Clase que relaciona cada boton con su nombre y su codigo
    private class Interna {

        String nombre;
        int codigo;
        JButton boton;

        private Interna(String nombre, int codigo) {
            this.nombre = nombre;
            this.codigo = codigo;
            this.boton = new JButton(nombre);
        }
    }

    /**
     * Crea una ventana para mostrar el menu de botones.
     *
     * @param titulo Titulo de la ventana.
     */
    public Menu(String titulo) {
        super(titulo);
        panelTexto = new JPanel();
        Border margen = BorderFactory.createEmptyBorder(10,10,10,10);
        panelTexto.setBorder(margen);
        textoSup = new JTextArea("", 4, 24);
        Border loweredetched = 
            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        textoSup.setBorder(loweredetched);
        panelBotones = new JPanel(new GridLayout(0, 1, 10, 10));
        BtCerrar = new JButton("Cerrar aplicacion");
        botones = new Vector<Interna>(5);
        identificadores = new HashSet<String>(5);

        inicializa();
    }

    // Finaliza las inicializaciones del constructor
    private void inicializa() {
        // Ventana
        textoSup.setEditable(false);
        superior = new JScrollPane
            (textoSup,
             JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
             JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        superior.setPreferredSize(new Dimension(460, 80));
        panelTexto.add(superior);
        centro = new JScrollPane
            (panelBotones,
             JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
             JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        centro.setPreferredSize(new Dimension(480, 300));

        setLayout(new BorderLayout(5, 5));

        add(panelTexto, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(BtCerrar, BorderLayout.SOUTH);

        pack();

        // Manejadores
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        BtCerrar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        //setVisible(true);
    }

    /**
     * Espera a que los datos sean introducidos.
     *
     * @return Codigo del boton pulsado por el usuario.
     * @throws NullPointerException Lanzada si no hay ninguna opcion insertada.
     */
    public synchronized int leeOpcion() throws NullPointerException {
        if (identificadores.size() == 0) {
            msjError("No hay ninguna opcion insertada");
            throw new NullPointerException();
        } else {
            setVisible(true);
            try {
                wait();
            } catch (InterruptedException e) {
            }
            return presionado;
        }
    }

    /**
     * Espera a que los datos sean introducidos y muestra un mensaje.
     *
     * @param texto Mensaje a mostrar.
     * @return Opcion pulsada.
     */
    public synchronized int leeOpcion(String texto) {
        println(texto);
        return leeOpcion();
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

    /**
     * Cierra la ventana del menu.
     */
    public void cierra() {
        setVisible(false);
    }

    /**
     * Inserta un boton con el texto clave y asociandole el codigo pasado como
     * argumento.
     *
     * @param clave String a mostrar en el boton.
     * @param codigo Codigo asociado al boton.
     */
    public void insertaOpcion(String clave, int codigo) {
        if (identificadores.contains(clave)) {
            msjError("El boton " + clave + " esta repetido");
        } else {
            Interna nuevo = new Interna(clave, codigo);
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
            nuevo.boton.addActionListener(this);
            p.add(nuevo.boton);
            panelBotones.add(p);
            identificadores.add(nuevo.nombre);
            botones.add(nuevo);
        }
    }

    /**
     * No usar ese metodo interno
     */
    public synchronized void actionPerformed(ActionEvent e) {
        for (Interna i : botones) {
            if (e.getSource() == i.boton) {
                presionado = i.codigo;
                notify();
            }
        }
    }

    // Pone un mensaje de error en una ventana
    private void msjError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error",
                                      JOptionPane.ERROR_MESSAGE);
    }

}
