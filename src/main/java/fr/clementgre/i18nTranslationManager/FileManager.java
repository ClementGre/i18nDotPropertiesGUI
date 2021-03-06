package fr.clementgre.i18nTranslationManager;

import fr.clementgre.i18nTranslationManager.utils.DialogBuilder;
import fr.clementgre.i18nTranslationManager.utils.StringUtils;
import javafx.application.Platform;

import java.io.*;
import java.nio.charset.StandardCharsets;;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileManager {

    private HashMap<String, Translation> translations = new HashMap<>();
    private FilePanel filePanel;
    public File file;
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
            filePanel.updateStatus();
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
                    translation.addComment(line.trim());
                    continue;
                }

                String key = line.split(Pattern.quote("="))[0];
                String value = StringUtils.removeBeforeNotEscaped(line, "=");


                if(key != null){
                    if(!key.isBlank()){

                        key = key.replaceAll(Pattern.quote("\\n"), "\n");
                        value = value.replaceAll(Pattern.quote("\\n"), "\n").trim();

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

    public boolean updateTranslationKey(String oldKey, String newKey){
        newKey = newKey.replaceAll("\\s+","");
        if(!isLoaded || oldKey.equals(newKey)) return false;

        if(!translations.containsKey(oldKey)){
            if(!filePanel.isSource()) return false;
            System.err.println("Key " + oldKey + " does not exists in " + filePanel.type.name() + " (updateTranslationKey)");
            return false;
        }

        translations.get(oldKey).setKey(newKey);
        translations.put(newKey, translations.get(oldKey));
        translations.remove(oldKey);

        saveTranslations();

        if(filePanel.isSource()){
            filePanel.getWindow().alternativeTranslation.fileManager.updateTranslationKey(oldKey, newKey);
            filePanel.getWindow().targetTranslation.fileManager.updateTranslationKey(oldKey, newKey);
            Platform.runLater(() -> filePanel.translationsListUpdated());
        }
        return true;
    }
    public boolean updateTranslationValue(String key, String newValue){
        if(!isLoaded) return false;
        if(!translations.containsKey(key)){
            translations.put(key, new Translation(new ArrayList<>(), key, newValue));
        }else{
            if(translations.get(key).getValue().equals(newValue)) return false;
            translations.get(key).setValue(newValue);
        }
        saveTranslations();
        return true;
    }
    public boolean updateTranslationComment(String key, List<String> newComments){
        if(!isLoaded) return false;
        if(!translations.containsKey(key)){
            System.err.println("Key " + key + " does not exists in " + filePanel.type.name() + " (updateTranslationValue)");
            return false;
        }
        if(!filePanel.isSource()) System.err.println("updateTranslationComment method must be called on sourceTranslation only");

        translations.get(key).setComments(newComments);
        saveTranslations();

        filePanel.getWindow().alternativeTranslation.saveTranslations();
        filePanel.getWindow().targetTranslation.saveTranslations();
        return true;
    }

    public String addTranslation() {
        String key = "";
        int i = 0;
        while(i == 0 || getTranslation(key) != null){ // check translation do not already exists
            i++;
            key = "unknown." + i;
        }

        translations.put(key, new Translation(new ArrayList<>(), key, ""));
        saveTranslations();
        filePanel.translationsListUpdated();
        return key;

    }
    public void deleteTranslation(String key) {
        if(!isLoaded) return;

        if(translations.containsKey(key)){
            translations.remove(key);

            saveTranslations();

            if(filePanel.isSource()){
                filePanel.getWindow().alternativeTranslation.fileManager.deleteTranslation(key);
                filePanel.getWindow().targetTranslation.fileManager.deleteTranslation(key);
                Platform.runLater(() -> filePanel.translationsListUpdated());
            }
        }

    }

    public void saveTranslations() {
        if(!hasTranslations()) return;
        try {
            saveTranslations(file);
            filePanel.updateStatus();
        } catch (IOException e) {
            e.printStackTrace();
            DialogBuilder.showErrorAlert(filePanel.getWindow(), "Unable to save translation file " + file.getAbsolutePath(), e.getMessage(), false);
        }
    }

    private void saveTranslations(File file) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false));

        HashMap<String, Translation> sourceTranslations = filePanel.getSourceTranslations();

        for(Translation sourceTranslation : sourceTranslations.values().stream().sorted().collect(Collectors.toList())){
            String key = sourceTranslation.getKey();

            for(String comment : sourceTranslations.get(key).getComments()){
                if(comment.isBlank()) continue;
                if(comment.startsWith("#")) writer.write(comment.trim());
                else writer.write("# " + comment.trim());
                writer.newLine();
            }

            if(translations.containsKey(key)){
                writer.write(key + "=" + translations.get(key).getValue().replace("\n", "\\n"));
            }else{
                writer.write(key + "=");
            }

            writer.newLine();
        }

        writer.flush();
        writer.close();


    }

    public HashMap<String, Translation> getSourceComments(){
        return filePanel.getSourceTranslations();
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
