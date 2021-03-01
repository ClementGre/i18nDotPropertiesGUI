package fr.clementgre.i18nTranslationManager;

import fr.clementgre.i18nTranslationManager.utils.StringUtils;

import javax.xml.transform.Transformer;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileManager {

    private List<Translation> translations = new ArrayList<>();
    private FilePanel filePanel;
    private File file;

    public FileManager(FilePanel filePanel) {
        this.filePanel = filePanel;

    }


    public void loadFile(File file) throws IOException {
        this.file = file;

        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStream = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStream);

        Translation translation = new Translation();

        String line; int i = 0;
        while((line = reader.readLine()) != null){

            if(!line.isBlank()){
                if(line.startsWith("#")) translation.addComment(line);

                String key = line.split(Pattern.quote("="))[0];
                String value = StringUtils.removeBeforeNotEscaped(line, "=");



            }
        }
        reader.close();
        inputStream.close();
        fileInputStream.close();


    }



}
