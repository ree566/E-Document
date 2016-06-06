/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class TxtWriter {

    /*
     http://www.javacodegeeks.com/2015/06/java-programming-tips-best-practices-beginners.html
     FileOutputStream is meant for writing streams of raw bytes such as image
     data. For writing streams of characters, consider using FileWriter.
     */
    private static TxtWriter instance;

    private final String fileLocation;
    private final String fileNameExt;

    private TxtWriter() {
        PropertiesReader p = PropertiesReader.getInstance();
        fileLocation = PropertiesReader.getInstance().getTxtLocation();
//        fileLocation = System.getProperty("user.home") + "\\Desktop\\";         // testing file path 
        fileNameExt = p.getOutputFilenameExt();
        //tomcat 讀取網路磁碟機有問題時，請把路徑改成如下列txtlocation所示(全路徑，而不是N://這種)
        //設定完之後還要把"tomcat服務"的設定從Local System 改為最高權限 Administrator 才能順利讀寫網路磁碟機
        //在編譯時期不會出現錯誤是因為程式又或者是tomcat都是以目前使用者身分(預設Administrator)所開啟
        //所以讀寫暢行無阻是理所當然的(若無照上述設定一定會出現ioexception找不到檔案or存取被拒等問題)
    }

    public static TxtWriter getInstance() {
        if (instance == null) {
            instance = new TxtWriter();
        }
        return instance;
    }

    private void writeTxtWithFullTxtPath(Map map, String filePath) throws IOException {
        if (map == null || map.isEmpty()) {
            return;
        }
        Iterator it = map.keySet().iterator();
        FileWriter file = new FileWriter(filePath, false);
        BufferedWriter fw = new BufferedWriter(file);
        while (it.hasNext()) {
            Object key = it.next();
            fw.append(key.toString());
            fw.append(",");
            fw.append(map.get(key).toString());
            fw.newLine();
        }
        fw.close();
        file.close();
    }

    public void writeTxtWithFileName(Map map, String fileName) throws IOException {
        String filePath = fileLocation + fileName + fileNameExt;
        writeTxtWithFullTxtPath(map, filePath);
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public void txtReset(String txtName) throws FileNotFoundException, IOException {

        String path = fileLocation + txtName + fileNameExt;
        String st = "";
        FileReader fr;
        File inputFile = new File(path);
        if (!inputFile.exists()) {
            inputFile.createNewFile();
        }
        fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        while (br.ready()) {
            st += br.readLine() + " ";
        }
        fr.close();
        br.close();
        if (!"".equals(st.trim())) {
            String[] after = st.split(" ");
            FileWriter file = new FileWriter(path, false);
            BufferedWriter fw = new BufferedWriter(file);
            for (String s : after) {
                fw.write(s.replace(",1", ",0"));
                fw.newLine();
            }
            fw.close();
            file.close();
        }
    }
}
