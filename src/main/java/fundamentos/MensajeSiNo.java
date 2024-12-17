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

/**
 * MensajeSiNo por Mariano Benito Hoz. Version 3.5. Octubre 2016.<p>
 * Mensaje simple que hace una pregunta y devuelve true o false.<p>

 Interfaz  <p>
 ========  <p>
 new MensajeSiNo ()       - crea el objeto<p>
 new MensajeSiNo (string) - crea el objeto con el titulo indicado<p>
 pregunta (string)     - muestra la pregunta y espera a que se pulse el
 boton Si o No<p>

 @author Mariano Benito Hoz <mbenitohoz at gmail dot com>
 @version 3.4
*/

public class MensajeSiNo {

    private String titulo;

    /**
     * Constructor del mensajeSiNo.
     */
    public MensajeSiNo() {
        titulo = "";
    }

    /**
     * Constructor que establece el titulo del mensaje de pregunta.
     *
     * @param titulo Titulo de la ventana.
     */
    public MensajeSiNo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Pregunta al usuario y devuelve un booleano con la respuesta proporcionada
     * por este.
     *
     * @param mensaje Mensaje a mostrar.
     * @return Respuesta del usuario.
     */
    public boolean pregunta(String mensaje) {
        int pulsado = JOptionPane.showConfirmDialog
            (null, mensaje, titulo,
             JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        return pulsado == 0;
    }
}
