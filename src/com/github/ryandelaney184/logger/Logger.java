package com.github.ryandelaney184.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The Logger class is used to log info and error messages to the user as well as
 * store the logs into the most recent log file. This should not be shared
 * between objects. This will output the name of the object that created it. All
 * logs will output the following.
 * <br>
 * [ClassName][time][Type]: Message<br><br>
 *
 * @author Ryan Delaney
 * @version 1.0
 * @since 1.8
 *
 * ---------------------------------- Change Log ----------------------------------|
 * 1.0                                                                             |
 *      - Added all key components                                                 |
 *                                                                                 |
 *      - Created a constructor that will check to see                             |
 *        if the log folder and log file have been created                         |
 *        and if not create them.                                                  |
 *                                                                                 |
 *      - Added the write(msg) method which will write to the log file             |
 *        if it exist and then return the message so it can be outputted           |
 *        by the OutputStream.                                                     |
 *                                                                                 |
 *      - Added the log(msg) method which will log info to the developer, and      |
 *        the other developers working on a project.                               |
 *                                                                                 |
 *      - Added the error(msg) method which log error messages to the developer.   |
 *                                                                                 |
 * 1.1                                                                             |
 *      - Added the end() method which will write to the most recent log file that |
 *        the end of the log has been reached.                                     |
 * --------------------------------------------------------------------------------|
 */
public final class Logger {

    //------------------------------------
    // Attributes
    //------------------------------------

    // This stores a reference to the Class Object
    // of the object that created it.
    private Class<?> parentClass;

    // This is used to format the time for the log
    // output this will output the hours:minutes:seconds
    // and if it is AM or PM.
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss a");

    // This stores the location of the current log file, and will
    // create the log file if it doesn't exist.
    private File logFileDir;

    ///////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////

    public Logger(Class<?> parentClass){

        // This will store the Class object of the object that created this.
        this.parentClass = parentClass;

        // This will format the date so it can be used as the log file name.
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M_d_y");

        // This will place the log folder inside the working directory, and will use the correct
        // file separator so on Linux and Mac / , Window \.
        File logFolder = new File(String.join("",
                System.getProperty("user.dir"),System.getProperty("file.separator"),
                "log"));

        // This will see if the log folder exists and if not create it. If it can't this will
        // kill the program because
        if(!logFolder.exists()){
            if(!logFolder.mkdirs()){
                System.err.println("Sorry could not create the log folder");
                System.exit(1);
            }
        }

        // This will place the log file inside the log folder and will name the log file
        // the current date.
        logFileDir = new File(String.join("",
                logFolder.getAbsolutePath(),System.getProperty("file.separator"),
                dateFormat.format(LocalDateTime.now()),
                ".log"));

        // This will check to see if the recent log file has been created if not create
        // it.
        if(!logFileDir.exists()){
            try{
                if(!logFileDir.createNewFile()){
                    System.err.println("Sorry could not create log file");
                    System.exit(1);
                }
            }catch (IOException e){
                System.err.println(e.getMessage());
            }
        }

        write("/--------------------------------- Start of Log ---------------------------------\\");
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * The write() method will attempt to write to the most recent log file
     * and will then return the message so the OutputStream can output the message.
     *
     * @param msg the info or error message you want the user to see.
     *
     * @return the message so it can be outputted by the OutputStream.
     */
    private String write(String msg){

        // This will check to see if the most recent log file has been created
        // if it has then write to the file if it has not just output the message
        // to the OutputStream so the developer can see the message.
        if(logFileDir.exists()){
            try(FileWriter writer = new FileWriter(logFileDir,true)){
                writer.write(String.join("",msg,"\n"));
            }catch (IOException e){
                System.err.println(e.getMessage());
            }
        }
        return msg;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * The info() method will output a message to the developer. This will attempt
     * to write the log into the most recent log file if it can't it will just output
     * the message.
     *
     * @param msg the message you want the developer to see.
     */
    public void info(String msg){
        // This will format the output as the following
        // [className][hh:mm:ss][INFO]: msg
        String output = String.join("",
                "["+ parentClass.getSimpleName(),"]",
                "["+timeFormat.format(LocalDateTime.now()),"]",
                "[INFO]: ", msg);

        // This will attempt to write the output to the most recent
        // log file if not it will just output the message to the
        // user.
        System.out.println(write(output));
    }

    /**
     * The error method will attempt to write to the most recent log file then will
     * output the message to the developers.
     *
     * @param msg the message you want the developer to see.
     */
    public void error(String msg){
        // This will format the output as the following
        // [className][hh:mm:ss][INFO]: msg
        String output = String.join("",
                "["+ parentClass.getSimpleName(),"]",
                "["+timeFormat.format(LocalDateTime.now()),"]",
                "[ERROR]: ", msg);

        // This will attempt to write the output to the most recent
        // log file if not it will just output the message to the
        // user.
        System.err.println(write(output));
    }

    /**
     * This will write to the most recent log file that the logging session has ended for
     * that program.
     */
    public void end(){
        write("\\--------------------------------- End of Log ---------------------------------/\n");
    }
}
