package pl.kernelpanic.dbmonster.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;


public class ScriptReaderIterator
        implements Iterator {
    private LineNumberReader reader;
    private String nextLine;

    public ScriptReaderIterator(String script) {
        this.reader = new LineNumberReader(new StringReader(script));
        prepareNextLine();
    }

    public ScriptReaderIterator(Reader reader) {
        this.reader = new LineNumberReader(new BufferedReader(reader));
        prepareNextLine();
    }

    public ScriptReaderIterator(File file) throws Exception {
        this.reader = new LineNumberReader(new BufferedReader(new FileReader(file)));
        prepareNextLine();
    }


    public void remove() {
    }


    public boolean hasNext() {
        return (this.nextLine != null);
    }


    public Object next() {
        String s = this.nextLine;
        prepareNextLine();
        return s;
    }

    private void prepareNextLine() {
        try {
            this.nextLine = this.reader.readLine();
            if (this.nextLine != null &&
                    "".equals(this.nextLine.trim())) {
                prepareNextLine();
            }
        } catch (Exception e) {
            System.err.println("Could not read line: " + e.getMessage());
        }
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonste\\util\ScriptReaderIterator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */