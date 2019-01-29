package com.amydegregorio.niofile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FilesExamples {
   private static final String TEXT_FILE_NAME = "exampleInput.txt";
   private static final String OUT_FILE_NAME = "exampleOutput.txt";
   private static final String JPG_FILE_NAME = "coffee.jpg";
   private static final String COPY_TEXT_NAME = "copiedText.txt";
   private static final String COPY_JPG_NAME = "secondCup.jpg";
   private static final String PROJECT_DIR = "/Users/AMD/Data/Eclipse/Blog-Examples/JavaBasics-NIOFile";

   public static void main(String[] args) {
      FilesExamples ex = new FilesExamples();
      try {
         ex.displayFileInformation();
         ex.walk();
         ex.read();
         ex.write();
         ex.copy();
      } catch (IOException e) {
         System.err.println(String.format("IO Exception %s", e.getMessage()));
      }
   }
   
   public void displayFileInformation() throws IOException {
      System.out.println("--Displaying some file information--");
      Path path = FileSystems.getDefault().getPath(TEXT_FILE_NAME);
      System.out.println(String.format("Does file %s exist? %b", TEXT_FILE_NAME, Files.exists(path)));
      System.out.println(String.format("Does file %s not exist? %b", TEXT_FILE_NAME, Files.notExists(path)));
      System.out.println(String.format("Is %s a directory? %b", TEXT_FILE_NAME, Files.isDirectory(path)));
      System.out.println(String.format("Is %s a regular file? %b", TEXT_FILE_NAME, Files.isRegularFile(path)));
      System.out.println(String.format("%s permissions (rwx): %b %b %b", TEXT_FILE_NAME, Files.isReadable(path), Files.isWritable(path), Files.isExecutable(path)));
      FileTime lastModifiedTime = Files.getLastModifiedTime(path);
      System.out.println(String.format("%s was last modified on %s", TEXT_FILE_NAME, new java.util.Date(lastModifiedTime.toMillis()).toString()));
      System.out.println(String.format("Size of %s: %d bytes", TEXT_FILE_NAME, Files.size(path)));
      System.out.println(String.format("%s content type is %s", TEXT_FILE_NAME, Files.probeContentType(path)));
      System.out.println(String.format("%s content type is %s", JPG_FILE_NAME, Files.probeContentType(FileSystems.getDefault().getPath(JPG_FILE_NAME))));
      System.out.println();
   }
   
   public void walk() throws IOException {
      System.out.println("--Demonstrating walk functionality--");
      Path path = FileSystems.getDefault().getPath(PROJECT_DIR);
      Files.walk(path).forEach(System.out::println);
      System.out.println();
   }
   
   public void read() throws IOException {
      System.out.println("--Reading From files--");
      System.out.println("Using 'lines'");
      Path path = FileSystems.getDefault().getPath(TEXT_FILE_NAME);
      try (Stream<String> lines = Files.lines(path)) {
         lines.forEach(System.out::println);
      }
      System.out.println();
      
      System.out.println("Using 'readAllLines'");
      Files.readAllLines(path).stream().forEach(System.out::println);
      System.out.println();
      
      System.out.println("Using 'newBufferedReader'");
      try (BufferedReader br = Files.newBufferedReader(path)) {
         String line = "";
         while ((line = br.readLine()) != null) {
            System.out.println(line);
         }
      }
      System.out.println();
      
      Path binPath = FileSystems.getDefault().getPath(JPG_FILE_NAME);
      
      System.out.println("Using 'readAllBytes'");
      byte[] coffeeBytes = Files.readAllBytes(binPath);
      System.out.println(String.format("Binary file %s contains %d bytes", JPG_FILE_NAME, coffeeBytes.length));
      System.out.println();
      
      System.out.println("Using 'newInputStream'");
      try (InputStream is = Files.newInputStream(binPath)) {
         byte[] input = new byte[1024];
         int counter = 0;
         while (is.read(input) >= 0) {
            //Normally we'd do some useful work on our input bytes
            counter++;
         }
         System.out.println(String.format("Read all the bytes in %d times through our loop", counter));
      }
      
      System.out.println();
   }
   
   public void write() throws IOException {
      System.out.println("--Writing to files--");
      Path path = FileSystems.getDefault().getPath(OUT_FILE_NAME);
      List<String> lines = new ArrayList<String>();
      lines.add("This is a line of text.");
      lines.add("This is another line of uninspired text.");
      lines.add("I could go on...");
      
      Files.write(path, lines);
      
      String text = "This is a line to append";
      Files.write(path, text.getBytes(), StandardOpenOption.APPEND);
      
      try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
         bw.write("This line was appended using a BufferedWriter retrieved from Files");
      }
      System.out.println();
   }
   
   public void copy() throws IOException {
      System.out.println("--Copying files--");
      Path textSource = FileSystems.getDefault().getPath(TEXT_FILE_NAME);
      Path textDest = FileSystems.getDefault().getPath(COPY_TEXT_NAME);
      Path binSource = FileSystems.getDefault().getPath(JPG_FILE_NAME);
      Path binDest = FileSystems.getDefault().getPath(COPY_JPG_NAME);
      Path targetText = Files.copy(textSource, textDest, StandardCopyOption.REPLACE_EXISTING);
      System.out.println(String.format("Copied %s to %s", TEXT_FILE_NAME, targetText.toString()));
      Path targetBin = Files.copy(binSource, binDest, StandardCopyOption.REPLACE_EXISTING);
      System.out.println(String.format("Copied %s to %s", JPG_FILE_NAME, targetBin.toString()));
      System.out.println();
   }

}
