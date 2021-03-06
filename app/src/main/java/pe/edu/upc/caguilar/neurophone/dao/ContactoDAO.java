package pe.edu.upc.caguilar.neurophone.dao;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.edu.upc.caguilar.neurophone.model.Contacto;
import pe.edu.upc.caguilar.neurophone.util.DesktopConnection;
import pe.edu.upc.caguilar.neurophone.util.Utility;

/**
 * Created by cagui on 17/02/2017.
 */

public class ContactoDAO {

    /*#############################################################################################*/
    public void actualizarContacto(Contacto objContacto){

        Utility.PrintDebug("ContactoDAO ", "EntroActualizar", null);

        ContentResolver contentResolver  = Utility.currentActivity.getContentResolver();

        String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";

        String[] emailParams = new String[]{objContacto.getId(), ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
        String[] nameParams = new String[]{objContacto.getId(), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
        String[] numberParams = new String[]{objContacto.getId(), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};

        ArrayList<android.content.ContentProviderOperation> ops = new ArrayList<android.content.ContentProviderOperation>();

        ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                .withSelection(where,emailParams)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, objContacto.getEmail())
                .build());

        ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                .withSelection(where,nameParams)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, objContacto.getNombre())
                .build());

        ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                .withSelection(where,numberParams)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, objContacto.getNumero())
                .build());
        try {
            Utility.PrintDebug("ContactoDAO ", "QUERY : " + objContacto.getId() + "#" + objContacto.getNombre() + "#" + objContacto.getNumero() + "#" + objContacto.getEmail() + "#"  , null);
            Utility.PrintDebug("ContactoDAO ", "PRE UPDATE", null);
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
            Utility.PrintDebug("ContactoDAO ", "POST UPDATE", null);
        }catch (Exception e)
        {
            Utility.PrintDebug("ContactoDAO ", "FAIL ACTUALIZAR", null);
            e.printStackTrace();
        }
    }

    /*#############################################################################################*/
    public ArrayList<String> getAllConactIds(){
        ArrayList<String> contactList = new ArrayList<String>();

        Cursor cursor = Utility.currentActivity.managedQuery(ContactsContract.Contacts.CONTENT_URI, null, null, null, "display_name ASC");

        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String nombre = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    contactList.add(""+_id + "##" + nombre );
                    Utility.PrintDebug("ContactoDAO ", _id + "##" + nombre , null);
                }
                while(cursor.moveToNext());
            }
        }

        return contactList;
    }

    /*#############################################################################################*/
    public String obtenerNombreContacto(String numeroTelefono){

        ContentResolver cr = Utility.currentActivity.getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(numeroTelefono));
        Cursor cursor = cr.query(uri, new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

        if (cursor == null) {
            return null;
        }

        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    /*#############################################################################################*/
    public List<Contacto> listarContactos(){

        ContentResolver cr = Utility.currentActivity.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        List<Contacto> lstContactos = new ArrayList<Contacto>();

        if(cursor.moveToFirst())
        {
            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    while (pCur.moveToNext())
                    {
                        Contacto contacto = new Contacto();

                        String contactoID = pCur.getString(pCur.getColumnIndex("_id"));
                        String nombre = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String numero = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String email = "emailDummy@dummy.com"; //Requiere otro query sobre el mismo contact id, devuelve array de emails
                        String image_uri = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                        String foto = "null";
                        Bitmap image_bitmap = null;
                        byte[] bArray = null;
                        try {
                            image_bitmap = MediaStore.Images.Media.getBitmap(Utility.currentActivity.getContentResolver(), Uri.parse(image_uri));
                        }catch (Exception e){
                            image_bitmap = null;
                        }
                        if(image_bitmap!=null){
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            image_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                            bArray = stream.toByteArray();
                            Utility.PrintDebug("ContactoDAO " + bArray.length, Arrays.toString(bArray), null);
                            foto = new String(Base64.encode(bArray,Base64.NO_WRAP)); //NO_CLOSE
                        }
                        contacto.setId(id);
                        contacto.setNombre(nombre);
                        contacto.setNumero(numero);
                        contacto.setEmail(email);
                        contacto.setFoto(foto);
                        lstContactos.add(contacto);
                        break;
                    }
                    pCur.close();
                }
            } while (cursor.moveToNext());
        }

        return lstContactos;
    }

    /*#############################################################################################*/
    public String listarSMS(){

        Uri smsUri = Uri.parse("content://sms"); //"content://sms/inbox"
        String[] smsProjection = new String[] {"_id", "address", "person", "body", "date", "type"};
        String smsSelection =  null;//"address='+51989029987'";
        ContentResolver cr = Utility.currentActivity.getContentResolver();
        Cursor smsCursor = cr.query(smsUri, smsProjection, smsSelection, null, "date desc"); //uri,projection,selection,selectionArgs,sortOrder

        String msgData = "";

        if (smsCursor.moveToFirst()) {
            do {
                String msg = "";
                msg += smsCursor.getString(smsCursor.getColumnIndex("_id"));
                msg += "#;#;" + smsCursor.getString(smsCursor.getColumnIndex("address"));
                msg += "#;#;" + obtenerNombreContacto(smsCursor.getString(smsCursor.getColumnIndex("address")));//smsCursor.getString(smsCursor.getColumnIndex("person"));
                msg += "#;#;" + smsCursor.getString(smsCursor.getColumnIndexOrThrow("body"));
                msg += "#;#;" + smsCursor.getString(smsCursor.getColumnIndexOrThrow("date"));
                msg += "#;#;" + smsCursor.getString(smsCursor.getColumnIndexOrThrow("type"));
                msg += "&;&;";

                msgData += msg;
                Utility.PrintDebug("ContactoDAO", msg , null);

            } while (smsCursor.moveToNext());
        } else {
            // empty box, no SMS
        }

        return msgData;
    }

    /*#############################################################################################*/
    public void eliminarSMS(String ids){

        try {
            ContentResolver cr = Utility.currentActivity.getContentResolver();

            List<String> lstID = Arrays.asList(ids.split("#;#;"));

            for (String id : lstID) {
                Utility.PrintDebug("ContactoDAO ", "Eliminar SMS Inicios id = " + id , null);

                cr.delete(Uri.parse("content://sms/" + id),null,null);
            }


        } catch (Exception e) {
            Utility.PrintDebug("ContactoDAO ", "Exception : " + e.getMessage() , null);
        }
    }
}
