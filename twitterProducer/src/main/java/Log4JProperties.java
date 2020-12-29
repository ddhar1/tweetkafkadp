/*
Sets Log4J properties without needing to set classpath log4j properties file
 */

import java.util.Properties;
import org.apache.log4j.Logger;

import org.apache.log4j.PropertyConfigurator;

public class Log4JProperties {


    public Log4JProperties(){

        Properties properties = new Properties();
        properties.setProperty("log4j.rootLogger","INFO,FILE");
        //properties.setProperty("log4j.rootCategory","TRACE");
        //properties.setProperty("log4j.appender.FILE", "org.apache.log4j.RollingFileAppender");
        properties.setProperty("log4j.appender.FILE", "org.apache.log4j.FileAppender  ");

        properties.setProperty("log4j.appender.FILE.File","log/log.out");
        properties.setProperty("log4j.appender.FILE.ImmediateFlush", "true");
        properties.setProperty("log4j.appender.FILE.Threshold", "debug");
        properties.setProperty("log4j.appender.FILE.append", "true");

	  /*
	    properties.setProperty("log4j.appender.stdout",     "org.apache.log4j.ConsoleAppender");
	    properties.setProperty("log4j.appender.stdout.layout",  "org.apache.log4j.PatternLayout");
	    properties.setProperty("log4j.appender.stdout.layout.ConversionPattern","%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");
*/
        properties.setProperty("log4j.appender.FILE.layout",  "org.apache.log4j.PatternLayout");
        properties.setProperty("log4j.appender.FILE.layout.ConversionPattern","%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");

        PropertyConfigurator.configure(properties);

        Logger log = Logger.getLogger(this.getClass().getName());

        log.info("Logger initalized and configured");



    }

}
