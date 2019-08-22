package pl.kernelpanic.dbmonster;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import pl.kernelpanic.dbmonster.connection.DBCPConnectionProvider;
import pl.kernelpanic.dbmonster.schema.Schema;
import pl.kernelpanic.dbmonster.schema.SchemaUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;


public class Launcher {
    private Properties properties = new Properties();


    private Options options = null;

    public Launcher(){
        options = new Options();
    }


    public static void main(String[] args) {
        try {
            (new Launcher()).run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public final void run(String[] args) throws Exception {
        CommandLine line = this.prepareCommandLine(args);
        if (line.getOptions().length == 0 || line.hasOption('h')) {
            HelpFormatter hf = new HelpFormatter();
            System.out.println("dbMonster v." + DBMonster.getVersion());
            hf.printHelp("java pl.kernelpanic.dbmonster.Launcher \n[-s file1 file2 ...] [-c configfile] [-h] \n[-f file] [-l file] \n[--grab] [-t name1 name2 ...] [-o output file]", this.options);


            return;
        }


        loadProperties();

        if (line.hasOption('c')) {
            readProperties(new File(line.getOptionValue('c')));
        }

        exportProperties();

        DBCPConnectionProvider connProvider = new DBCPConnectionProvider();

        if (line.hasOption("grab")) {
            SchemaGrabber grabber = new SchemaGrabber();
            grabber.setProperties(this.properties);
            grabber.setConnectionProvider(connProvider);
            if (line.hasOption('o')) {
                grabber.setOutputFile(line.getOptionValue('o'));
            }
            if (line.hasOption('t')) {
                String[] names = line.getOptionValues('t');
                if (names != null) {
                    for (int i = 0; i < names.length; i++) {
                        grabber.addTable(names[i]);
                    }
                }
            }
            grabber.doTheJob();

            return;
        }
        ProgressMonitor progressMonitor = null;
        String progressMonitorClass = this.properties.getProperty("dbmonster.progress.monitor", null);
        if (progressMonitorClass != null) {
            try {
                Class clazz = Class.forName(progressMonitorClass);
                progressMonitor = (ProgressMonitor) clazz.newInstance();
            } catch (Exception e) {
                System.err.println("Unable to instanciate progress monitor class: " + e.getMessage());
            }
        }


        DBMonster dbm = new DBMonster();
        if (progressMonitor != null) {
            dbm.setProgressMonitor(progressMonitor);
        }
        connProvider.setLogger(dbm.getLogger());
        dbm.setConnectionProvider(connProvider);
        dbm.setProperties(this.properties);

        if (line.hasOption('f')) {
            dbm.setPreScript(new File(line.getOptionValue('f')));
        }

        if (line.hasOption('l')) {
            dbm.setPostScript(new File(line.getOptionValue('l')));
        }

        String[] schemas = line.getOptionValues('s');
        if (schemas != null) {
            for (int i = 0; i < schemas.length; i++) {
                Schema schema = SchemaUtil.loadSchema(schemas[i], dbm.getLogger());

                dbm.addSchema(schema);
            }
            dbm.doTheJob();
        }
    }


    private final void loadProperties() throws Exception {
        String userHome = System.getProperty("user.dir");
        File f = new File(userHome, "dbmonster.properties");
        readProperties(f);
        f = new File("dbmonster.properties");
        readProperties(f);
    }


    private final void readProperties(File file) throws Exception {
        if (file.exists() && file.canRead() && file.isFile()) {
            InputStream fis = new BufferedInputStream(new FileInputStream(file));

            this.properties.load(fis);
        }
    }


    private final void exportProperties() {
        Enumeration<?> names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String key = (String) names.nextElement();
            if (key.startsWith("dbmonster.jdbc.")) {
                System.setProperty(key, this.properties.getProperty(key));
            }
        }
    }


    private final CommandLine prepareCommandLine(String[] args) throws Exception {
        DefaultParser posixParser = new DefaultParser();
        Option conf = Option.builder("c")
                .numberOfArgs(1)
                .argName("config file")
                .desc("use additional configuration file")
                .longOpt("config")
                .build();

        //Option conf = OptionBuilder.withArgName("config file").hasArg().withDescription("use additional configuration file").withLongOpt("config").create('c');

        Option schema = Option.builder("s")
                .numberOfArgs(1)
                .argName("schema file[s]")
                .desc("schema files")
                .longOpt("schema")
                .build();

        Option help = Option.builder("h")
                .numberOfArgs(1)
                .desc("display help (this screen)")
                .longOpt("help")
                .build();


        Option grab = Option.builder()
                .numberOfArgs(1)
                .desc("start Schema Grabber only")
                .longOpt("grab")
                .build();

        Option tables = Option.builder("t")
                .numberOfArgs(1)
                .desc("specify tables you want to grab")
                .argName("table[s] name[s]")
                .longOpt("tables")
                .build();

        Option output = Option.builder("o")
                .numberOfArgs(1)
                .desc("output file for Schema Grabber")
                .argName("output file")
                .longOpt("out")
                .build();


        //Option output = OptionBuilder.withArgName("output file").hasArg().withDescription("output file for Schema Grabber").withLongOpt("out").create('o');

        Option prescript = Option.builder("f")
                .numberOfArgs(1)
                .desc("execute statements from this file before data generation")
                .argName("pre-generation script")
                .longOpt("first")
                .build();

       // Option prescript = OptionBuilder.withArgName("pre-generation script").hasArg().withDescription("execute statements from this file before data generation").withLongOpt("first").create('f');

        Option postscript = Option.builder("l")
                .numberOfArgs(1)
                .desc("execute statements from this file after data generation")
                .argName("post-generation script")
                .longOpt("last")
                .build();

        //Option postscript = OptionBuilder.withArgName("post-generation script").hasArg().withDescription("execute statements from this file after data generation").withLongOpt("last").create('l');


        this.options.addOption(conf);
        this.options.addOption(schema);
        this.options.addOption(help);
        this.options.addOption(prescript);
        this.options.addOption(postscript);
        this.options.addOption(grab);
        this.options.addOption(tables);
        this.options.addOption(output);

        return posixParser.parse(this.options, args);
    }
}