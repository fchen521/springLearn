package pl.kernelpanic.dbmonster.schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public final class SchemaUtil {
    public static final String DTD = "-//kernelpanic.pl//DBMonster Database Schema DTD 1.1//EN";
    public static final String DTD_URL = "http://dbmonster.kernelpanic.pl/dtd/dbmonster-schema-1.1.dtd";
    public static final String PROJECT = "project";
    public static final String SCHEMA_WRAPPER = "schema";
    public static final String TABLE = "table";
    public static final String COLUMN = "column";
    public static final String KEY = "key";
    public static final String KEY_GENERATOR = "key_generator";
    public static final String DATA_GENERATOR = "data_generator";
    private static Map exclusions = new HashMap();


    public static final String CRLF = System.getProperty("line.separator");

    static {
        exclusions.put("project", new String[]{"fileName", "properties", "jdbcViaProperties"});

        exclusions.put("schema", new String[]{"fileName", "schema"});

        exclusions.put("table", new String[]{"key", "schema"});

        exclusions.put("key", new String[]{"table", "generator"});

        exclusions.put("column", new String[]{"table", "generator", "value"});

        exclusions.put("key_generator", new String[]{"key"});

        exclusions.put("data_generator", new String[]{"column"});
    }


    public static final Schema loadSchema(String fileName, Log log) throws Exception {
        return loadSchema(fileName, log, null);
    }


    public static final Schema loadSchema(String fileName, Log log, ClassLoader classloader) throws Exception {
        File f = new File(fileName);
        FileInputStream fis = new FileInputStream(f);
        Schema schema = loadSchema(fis, log, classloader);

        String homePath = f.getParent();
        schema.setHome(homePath);
        return schema;
    }


    public static final Schema loadSchema(URL url, Log log) throws Exception {
        return loadSchema(url.openStream(), log);
    }


    public static final Schema loadSchema(InputStream is, Log log) throws Exception {
        return loadSchema(is, log, null);
    }


    public static final Schema loadSchema(InputStream is, Log log, ClassLoader classloader) throws Exception {
            ErrorHandler errorHandler = new ErrorHandler() {
            public final void warning(SAXParseException exception) throws SAXException {
                throw exception;
            }


            public final void error(SAXParseException exception) throws SAXException {
                throw exception;
            }


            public final void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }
        };

        URL url = SchemaUtil.class.getClassLoader().getResource("dbmonster-schema-1.1.dtd");

        Digester digester = new Digester();
        if (log != null) {
            digester.setLogger(log);
        }
        digester.register("-//kernelpanic.pl//DBMonster Database Schema DTD 1.1//EN", url.toString());
        if (classloader != null) {
            digester.setClassLoader(classloader);
        } else {
            digester.setUseContextClassLoader(true);
        }

        digester.setErrorHandler(errorHandler);
        digester.setValidating(true);

        digester.addObjectCreate("dbmonster-schema", Schema.class);
        digester.addCallMethod("dbmonster-schema/name", "setName", 0);
        digester.addObjectCreate("*/table", Table.class);
        digester.addSetProperties("*/table");

        digester.addObjectCreate("*/table/key", Key.class);
        digester.addSetProperties("*/table/key");
        digester.addFactoryCreate("*/table/key/generator", new KeyGeneratorFactory());

        digester.addSetProperty("*/table/key/generator/property", "name", "value");

        digester.addSetNext("*/table/key/generator", "setGenerator", pl.kernelpanic.dbmonster.generator.KeyGenerator.class.getName());

        digester.addSetNext("*/table/key", "setKey", Key.class.getName());
        digester.addSetNext("*/table", "addTable", Table.class.getName());
        digester.addObjectCreate("*/table/column", Column.class);
        digester.addSetProperties("*/table/column");
        digester.addSetNext("*/table/column", "addColumn", Column.class.getName());

        digester.addFactoryCreate("*/table/column/generator", new GeneratorFactory());

        digester.addSetProperties("*/table/column/generator");
        digester.addSetNext("*/table/column/generator", "setGenerator", pl.kernelpanic.dbmonster.generator.DataGenerator.class.getName());

        digester.addSetProperty("*/table/column/generator/property", "name", "value");


        return (Schema) digester.parse(is);
    }


    public static final List validateSchema(Schema schema) {
        List errors = new ArrayList();
        String name = schema.getName();
        if (name == null || "".equals(name)) {
            errors.add("Schema has no name.");
        }
        List tables = schema.getTables();
        if (tables.isEmpty()) {
            errors.add("Schema " + name + " must have at least one table.");
        }
        for (int i = 0; i < tables.size(); i++) {
            Table t = (Table) tables.get(i);
            List tableErrors = validateTable(t);
            if (tableErrors != null) {
                errors.addAll(tableErrors);
            }
        }
        if (errors.isEmpty()) {
            return null;
        }
        return errors;
    }


    public static final List validateTable(Table table) {
        List errors = new ArrayList();
        String name = table.getName();
        if (name == null || "".equals(name)) {
            errors.add("Table has no name!");
        }
        if (table.getKey() == null && table.getColumns().isEmpty()) {
            errors.add("Table " + name + " must have a key or at least one column.");
        }

        Key key = table.getKey();
        if (key != null) {
            List keyErrors = validateKey(key);
            if (keyErrors != null) {
                errors.addAll(keyErrors);
            }
        }
        List columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Column c = (Column) columns.get(i);
            List columnErrors = validateColumn(c);
            if (columnErrors != null) {
                errors.addAll(columnErrors);
            }
        }
        if (errors.isEmpty()) {
            return null;
        }
        return errors;
    }


    public static final List validateKey(Key key) {
        List errors = new ArrayList();
        if (key.getGenerator() == null) {
            errors.add("Primary key for table " + key.getTable().getName() + " has no generator");
        }


        if (errors.isEmpty()) {
            return null;
        }
        return errors;
    }


    public static final List validateColumn(Column column) {
        List errors = new ArrayList();
        String name = column.getName();
        if (name == null || "".equals(name)) {
            errors.add("One column in table " + column.getTable().getName() + " has no name.");
        }

        if (column.getGenerator() == null) {
            errors.add("Column " + name + " in table " + column.getTable().getName() + " has no generator.");
        }

        if (errors.isEmpty()) {
            return null;
        }
        return errors;
    }


    public static final List getProperties(Object object) {
        List retList = new ArrayList();
        try {
            Class clazz = object.getClass();
            Method[] methods = clazz.getMethods();
            Map map = new HashMap();

            for (int i = 0; i < methods.length; i++) {
                Method m = methods[i];
                String mName = m.getName();
                if ((mName.startsWith("get") || mName.startsWith("set")) &&
                        Modifier.isPublic(m.getModifiers())) {
                    map.put(mName, mName);
                }
            }


            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                if (name.startsWith("get")) {
                    String getter = name;
                    String setter = "s" + getter.substring(1);
                    String method = getter.substring(3);
                    char ch = method.charAt(0);
                    method = Character.toLowerCase(ch) + method.substring(1);
                    if (map.containsKey(setter) &&
                            !isHidden(object, method)) {
                        retList.add(method);
                    }
                }

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        Collections.sort(retList);
        return retList;
    }


    public static final boolean isHidden(Object object, String name) {
        String[] excl = null;
        if (object instanceof Table) {
            excl = (String[]) exclusions.get("table");
        } else if (object instanceof Key) {
            excl = (String[]) exclusions.get("key");
        } else if (object instanceof pl.kernelpanic.dbmonster.generator.KeyGenerator) {
            excl = (String[]) exclusions.get("key_generator");
        } else if (object instanceof Column) {
            excl = (String[]) exclusions.get("column");
        } else {
            excl = (String[]) exclusions.get("data_generator");
        }

        for (int i = 0; i < excl.length; i++) {
            if (name.equals(excl[i])) {
                return true;
            }
        }
        return false;
    }


    public static final void serializeSchema(Writer writer, Schema schema) throws Exception {
        writer.write("<?xml version=\"1.0\"?>");
        writer.write(CRLF);
        writer.write("<!DOCTYPE dbmonster-schema PUBLIC \"");
        writer.write("-//kernelpanic.pl//DBMonster Database Schema DTD 1.1//EN");
        writer.write("\" \"");
        writer.write("http://dbmonster.kernelpanic.pl/dtd/dbmonster-schema-1.1.dtd");
        writer.write("\">");
        writer.write(CRLF);
        writer.write("<dbmonster-schema>");
        writer.write(CRLF);
        writer.write("  <name>");
        writer.write(schema.getName());
        writer.write("</name>");
        writer.write(CRLF);
        Iterator it = schema.getTables().iterator();
        while (it.hasNext()) {
            Table table = (Table) it.next();
            serializeTable(writer, table);
        }
        writer.write("</dbmonster-schema>");
        writer.write(CRLF);
        writer.flush();
    }


    public static final void serializeTable(Writer writer, Table table) throws Exception {
        writer.write("  <table name=\"");
        writer.write(table.getName());
        writer.write("\" rows=\"");
        writer.write(String.valueOf(table.getRows()));
        writer.write("\">");
        writer.write(CRLF);
        if (table.getKey() != null) {
            serializeKey(writer, table.getKey());
        }
        Iterator it = table.getColumns().iterator();
        while (it.hasNext()) {
            Column column = (Column) it.next();
            serializeColumn(writer, column);
        }
        writer.write("  </table>");
        writer.write(CRLF);
    }


    public static final void serializeKey(Writer writer, Key key) throws Exception {
        writer.write("    <key databaseDefault=\"");
        writer.write(String.valueOf(key.getDatabaseDefault()));
        writer.write("\">");
        writer.write(CRLF);
        serializeGenerator(writer, key.getGenerator());
        writer.write("    </key>");
        writer.write(CRLF);
    }


    public static final void serializeColumn(Writer writer, Column column) throws Exception {
        writer.write("    <column name=\"");
        writer.write(column.getName());
        writer.write("\" databaseDefault=\"");
        writer.write(String.valueOf(column.getDatabaseDefault()));
        writer.write("\">");
        writer.write(CRLF);
        serializeGenerator(writer, column.getGenerator());
        writer.write("    </column>");
        writer.write(CRLF);
    }


    public static final void serializeGenerator(Writer writer, Object generator) throws Exception {
        writer.write("      <generator type=\"");
        writer.write(generator.getClass().getName());
        writer.write("\">");
        writer.write(CRLF);
        List properties = getProperties(generator);
        Iterator it = properties.iterator();
        while (it.hasNext()) {
            String property = (String) it.next();
            writer.write("        <property name=\"");
            writer.write(property);
            writer.write("\" value=\"");
            String value = BeanUtils.getProperty(generator, property);
            if (value != null) {
                writer.write(value);
            }
            writer.write("\"/>");
            writer.write(CRLF);
        }
        writer.write("      </generator>");
        writer.write(CRLF);
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\schema\SchemaUtil.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */