/*
 */
package org.tweb.storage.properties;

import com.typesafe.config.Config;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tweb.translation.entity.TranslationPersister;
import javax.inject.Inject;

import org.tweb.translation.entity.Translation;

/**
 * PropertyFile uses path as id of translation object
 *
 * @author jonas
 */
public class PropertyFile implements TranslationPersister {

    private String path;
    private String language;

    @Inject
    PathUtil pathUtil;

    @Inject
    Config config;

    @Inject
    PropertiesReader propReader;

    @Inject
    PropertiesWriter propWriter;

    public PropertyFile() {
    }

    public void init(String path, String language) {
        this.path = path;
        this.language = language;
    }

    @Override
    public Translation load() {
        Translation translation = new Translation(path, language);
        String filePath = pathUtil.createFilePath(path, language);

        Path path = Paths.get(config.getString("root_path"), filePath);

        try (FileReader fileReader = new FileReader(path.toString());
                BufferedReader reader = new BufferedReader(fileReader)) {

            propReader.addLinesToTranslation(reader.lines(), translation.getProps());

        } catch (IOException ex) {
            Logger.getLogger(PropertyFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return translation;
    }

    @Override
    public void write(Translation translation) {
        String filePath = pathUtil.createFilePath(path, language);
        Path path = Paths.get(config.getString("root_path"), filePath);

        try (FileReader fileReader = new FileReader(path.toString());
                BufferedReader reader = new BufferedReader(fileReader)) {
            propWriter.mergeContent(reader.lines(), translation.getProps());
            fileReader.close();
            
            try (FileWriter fileWriter = new FileWriter(path.toString());
                    BufferedWriter writer = new BufferedWriter(fileWriter)) {
                
                propWriter.writeContent(writer);
                
            } catch (IOException ex) {
                Logger.getLogger(PropertyFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(PropertyFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
