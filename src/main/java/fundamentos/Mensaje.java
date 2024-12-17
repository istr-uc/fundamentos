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

import javax.swing.JOptionPane;

/** Mensaje por Michael Gonzalez y Mariano Benito. Version 3.5. Octubre 2016

    Es una clase simple para mostrar un mensaje y esperar a que
    se pulse un boton<p>

    Interfaz  <p>
    ========  <p>
    new Mensaje ()       - crea el objeto<p>
    new Mensaje (string) - crea el objeto con el titulo indicado<p>
    escribe (string)     - muestra el mensaje y espera a que se pulse el
    boton OK<p>

    @author Michael Gonzalez Harbour <mgh at unican dot es>
    @author Mariano Benito Hoz <mbenitohoz at gmail dot com>
    @version 3.4
*/
public class Mensaje {

    private String titulo;

    /**
     * Constructor del mensaje sin titulo en la ventana.
     */
    public Mensaje() {
        titulo = "";
    }

    /**
     * Constructor del mensaje que establece el titulo.
     *
     * @param titulo Titulo de la ventana.
     */
    public Mensaje(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Muestra el mensaje con el texto que se le pasa como argumento.
     *
     * @param mensaje Mensaje a mostrar al usuario.
     */
    public void escribe(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, titulo,
                                      JOptionPane.INFORMATION_MESSAGE);
    }
}
