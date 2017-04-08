package pe.edu.upc.caguilar.neurophone.helper;

import java.util.List;

import pe.edu.upc.caguilar.neurophone.dao.ContactoDAO;
import pe.edu.upc.caguilar.neurophone.model.Contacto;
import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

/**
 * Created by cagui on 7/04/2017.
 */

public class ContactoHelper {

    /*#############################################################################################*/
    public static void EditarContacto(String texto) {

        try {

            Contacto objContacto = new Contacto();

            String[] arrayTexto = texto.split("#;#;");
            objContacto.setId(arrayTexto[0]);
            objContacto.setNombre(arrayTexto[1]);
            objContacto.setNumero(arrayTexto[2]);
            objContacto.setEmail(arrayTexto[3]);

            ContactoDAO contactoDAO = new ContactoDAO();
            contactoDAO.actualizarContacto(objContacto);

        }catch (Exception e){
            Utility.PrintDebug("Catch",e.getMessage(),null);
        }
    }

    /*#############################################################################################*/
    public static void ObtenerContactos(){

        try {

            ContactoDAO contactoDAO = new ContactoDAO();

            List<Contacto> lstContactos = contactoDAO.listarContactos();

            String cadenaEnviar = "";

            for(int i=0; i<lstContactos.size(); i++){ //alContacts.size()
                if(i==25)
                    break;
                cadenaEnviar = cadenaEnviar + lstContactos.get(i).getId() + "#;#;" + lstContactos.get(i).getNombre() + "#;#;" + lstContactos.get(i).getNumero() + "#;#;" + lstContactos.get(i).getEmail() + "#;#;" + lstContactos.get(i).getFoto() + "&;&;";
            }

            DesktopConnection.SendMessage(cadenaEnviar);

        }catch (Exception e){
            Utility.PrintDebug("Catch",e.getMessage(),null);
        }
    }
}
