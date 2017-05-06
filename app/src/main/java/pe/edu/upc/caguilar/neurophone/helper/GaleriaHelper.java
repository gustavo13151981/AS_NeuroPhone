package pe.edu.upc.caguilar.neurophone.helper;

import android.app.Activity;

import java.util.List;

import pe.edu.upc.caguilar.neurophone.dao.ContactoDAO;
import pe.edu.upc.caguilar.neurophone.dao.GaleriaDAO;
import pe.edu.upc.caguilar.neurophone.model.Contacto;
import pe.edu.upc.caguilar.neurophone.model.Imagen;
import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

public class GaleriaHelper {

    private static Activity act = Utility.currentActivity;

    public static void ObtenerImagenes(){

        try {

            GaleriaDAO galeriaDAO = new GaleriaDAO();

            List<Imagen> lstImagen  = galeriaDAO.listarImagenes();

            String cadenaEnviar = "";

            for(int i=0; i<lstImagen.size(); i++){ //alContacts.size()
                cadenaEnviar = cadenaEnviar + lstImagen.get(i).getNombre() + "#;#;" + lstImagen.get(i).getFoto() + "&;&;";
            }

            DesktopConnection.SendMessage(cadenaEnviar);

        }catch (Exception e){
            Utility.PrintDebug("Catch",e.getMessage(),null);
        }
    }
}
