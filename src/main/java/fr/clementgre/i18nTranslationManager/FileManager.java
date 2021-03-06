package fr.clementgre.i18nTranslationManager;

import fr.clementgre.i18nTranslationManager.utils.DialogBuilder;
import fr.clementgre.i18nTranslationManager.utils.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.regex.Pattern;

public class FileManager {

    private HashMap<String, Translation> translations = new HashMap<>();
    private FilePanel filePanel;
    private File file;
    private boolean isLoaded = false;

    public FileManager(FilePanel filePanel) {
        this.filePanel = filePanel;

    }

    public void updateFile(File file){
        this.file = file;
        translations.clear();
        if(file == null){
            isLoaded = false;
            filePanel.translationsListUpdated();
        }else{
            updateTranslations();
        }

    }

    public void updateTranslations(){
        translations = new HashMap<>();
        try{
            loadTranslations(file);
            isLoaded = true;
            filePanel.translationsListUpdated();
        }catch(IOException e){
            e.printStackTrace();
            DialogBuilder.showErrorAlert(filePanel.getWindow(), "Unable to load translation file " + file.getAbsolutePath(), e.getMessage(), false);
            isLoaded = false;
            filePanel.translationsListUpdated();
        }
    }

    private void loadTranslations(File file) throws IOException {
        this.file = file;

        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStream = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStream);

        Translation translation = new Translation();

        String line; int i = 0;
        while((line = reader.readLine()) != null){

            if(!line.isBlank()){
                if(line.startsWith("#")){
                    translation.addComment(line);
                    continue;
                }

                String key = line.split(Pattern.quote("="))[0];
                String value = StringUtils.removeBeforeNotEscaped(line, "=");


                if(key != null){
                    if(!key.isBlank() && !value.isBlank()){

                        key = key.replaceAll(Pattern.quote("\\n"), "\n");
                        value = value.replaceAll(Pattern.quote("\\n"), "\n");

                        translation.setKey(key);
                        translation.setValue(value);
                        translations.put(key, translation);
                        translation = new Translation();
                        i++;
                    }
                }

            }
        }
        reader.close();
        inputStream.close();
        fileInputStream.close();


    }


    public HashMap<String, Translation> getTranslations() {
        return translations;
    }

    public Translation getTranslation(String key){
        return translations.get(key);
    }

    public boolean hasTranslations() {
        return isLoaded;
    }
}
