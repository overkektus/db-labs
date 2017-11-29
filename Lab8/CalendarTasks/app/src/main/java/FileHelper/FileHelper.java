package FileHelper;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import category.CategoryList;
import es.dmoral.toasty.Toasty;
import task.TaskList;

/**
 * Created by Egor on 20.10.2017.
 */

public class FileHelper {

    private final static String FILENAME_CATEGORY = "category.json";
    Type typeTokenCategoryList = new TypeToken<CategoryList>() {}.getType();

    private static final int REQUEST_PERMISSION_WRITE = 1001;

    private static boolean  permissionGranted;

    public static boolean isPermissionGranted() {
        return permissionGranted;
    }

    public static void setPermissionGranted(boolean permissionGranted) {
        FileHelper.permissionGranted = permissionGranted;
    }

    private BufferedWriter bw;
    private static FileReader fr;

    public static String ReadJSON(File fileDir, String FILENAME) {
        String data = null;
        File file = new File(fileDir, FILENAME);
        char buf[] = new char[(int) file.length()];
        try {
            fr = new FileReader(file);
            fr.read(buf);
            data = new String(buf);
            Log.d("FileHelper(ReadJSON)", "файл успешно прочитан " + data);
        }
        catch (IOException e) {
            Log.d("FileHelper(ReadJSON)", "не удалось прочитать файл " + FILENAME + " ошибка: " + e.getMessage());
        }
        Log.d("FileHelper(ReadJSON)", new String(buf));
        return data;
    }

    public static boolean WriteJSON(File fileDir, String FILENAME, Object obj) {
        FileOutputStream fos = null;
        try {
            File file = new File(fileDir, FILENAME);
            fos = new FileOutputStream(file);
            String json = new Gson().toJson(obj);
            fos.write(json.getBytes());
        }
        catch(IOException e) {
            Log.d("FileHelper(WriteJSON)", "не удалось записать файл " + FILENAME + " ошибка: " + e.getMessage());
            return false;
        }
        finally {
            try {
                if(fos != null)
                    fos.close();
            }
            catch(IOException e){
                Log.d("FileHelper(WriteJSON)", "не удалось записать файл " + FILENAME +
                        " ошибка: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    public static boolean fileExist(File filesDir, String FILENAME) {
        boolean rc = false;
        File f = new File(filesDir, FILENAME);
        if (rc = f.exists()) {
            Log.d("Log_02", "Файл " + FILENAME + " существует");
        } else {
            Log.d("Log_02", "Файл " + FILENAME + " не найден");
        }
        return rc;
    }

    /*
    public static void WriteLine(TaskList taskList, File filesDir, String FILENAME) {

        File file = new File(filesDir, FILENAME);
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bufferWriter = new BufferedWriter(fw);
            bufferWriter.write(str);
            bufferWriter.close();
            Log.d("Log_02", "успешно записан");
        }
        catch (IOException e) {
            Log.d("Log_02", e.getMessage());
        }

        try {
            File file = new File(filesDir, FILENAME);
            JAXBContext jaxbContext = JAXBContext.newInstance(TaskList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(taskList, file);
            jaxbMarshaller.marshal(taskList, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    public Document loadXml() throws IOException, SAXException {
        DocumentBuilderFactory xmlfry1 = DocumentBuilderFactory.newInstance();
        Document doc;
        DocumentBuilder xmlbld = null;
        File f = null;
        try {
            xmlbld = xmlfry1.newDocumentBuilder();
            f = FileHelper.getExternalPath();
            FileReader fr = new FileReader(f);
            char[] buf = new char[(int) f.length()];
            fr.read(buf);
            String s = new String(buf);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc = xmlbld.parse(f);
        return doc;
    }

    */

    // проверяем, доступно ли внешнее хранилище для чтения и записи
    public static boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return  Environment.MEDIA_MOUNTED.equals(state);
    }
    // проверяем, доступно ли внешнее хранилище хотя бы только для чтения
    public static boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return  (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

}
