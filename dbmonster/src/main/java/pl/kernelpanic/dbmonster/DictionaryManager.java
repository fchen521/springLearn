package pl.kernelpanic.dbmonster;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class DictionaryManager {
    private Map dictionaries = new HashMap();


    private Random random = null;


    public final void setRandom(Random rnd) {
        this.random = rnd;
    }


    public final Dictionary getDictionary(String schemaPath, String path) throws Exception {
        File dictFile = new File(path);
        if (!dictFile.isAbsolute()) {
            dictFile = new File(schemaPath, path);
        }
        String key = dictFile.getCanonicalPath();
        if (this.dictionaries.containsKey(key)) {
            return (Dictionary) this.dictionaries.get(key);
        }
        loadDictionary(key);
        return (Dictionary) this.dictionaries.get(key);
    }


    public final Dictionary getDictionary(URL url) throws Exception {
        if (this.dictionaries.containsKey(url.toString())) {
            return (Dictionary) this.dictionaries.get(url.toString());
        }
        loadDictionary(url);
        return (Dictionary) this.dictionaries.get(url.toString());
    }


    private final void loadDictionary(String path) throws Exception {
        File f = new File(path);
        if (!f.exists() || !f.canRead()) {
            throw new Exception("Cannot access dictionary file + " + path);
        }

        InputStream is = null;
        if (path.endsWith(".zip")) {
            ZipFile zf = new ZipFile(f);
            ZipEntry ze = (ZipEntry) zf.entries().nextElement();
            is = zf.getInputStream(ze);
        } else if (path.endsWith(".gz")) {
            is = new GZIPInputStream(new FileInputStream(f));
        } else {
            is = new FileInputStream(f);
        }
        readDictionary(path, is);
    }


    private final void loadDictionary(URL url) throws Exception {
        InputStream is = new GZIPInputStream(url.openStream());
        readDictionary(url.toString(), is);
    }


    private final void readDictionary(String key, InputStream is) throws Exception {
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(is, "UTF-8"));

        String line = null;

        Dictionary dictionary = new Dictionary();
        dictionary.setName(key);
        dictionary.setRandom(this.random);
        while ((line = reader.readLine()) != null) {
            dictionary.addItem(line);
        }
        this.dictionaries.put(key, dictionary);
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\DictionaryManager.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */