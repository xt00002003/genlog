package com.dark.utils;

import com.hadoop.compression.lzo.LzoCodec;
import org.anarres.lzo.LzoAlgorithm;
import org.anarres.lzo.LzoCompressor;
import org.anarres.lzo.LzoLibrary;
import org.anarres.lzo.LzoOutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.compress.CompressionInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *  java 操作hdfs示例
 */
public class OperatingFiles {

    //initialization
    static Configuration conf = new Configuration();
    static FileSystem hdfs;
    static {
        String path =Thread.currentThread().getContextClassLoader().getResource("").getPath();
        conf.addResource(new Path(path + "core-site.xml"));
        conf.addResource(new Path(path + "hdfs-site.xml"));
        try {
            hdfs = FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //create a direction
    public void createDir(String dir) throws IOException {
        Path path = new Path(dir);
        hdfs.mkdirs(path);
        System.out.println("new dir \t" + conf.get("fs.default.name") + dir);
    }
    public boolean exists(String dir) throws IOException {
        Path path = new Path(dir);
        boolean result=hdfs.exists(path);
        System.out.println("dir \t" + conf.get("fs.default.name") + dir+" is or not exists:"+result);
        return result;

    }

    //copy from local file to HDFS file
    public void copyFile(String localSrc, String hdfsDst) throws IOException{
        Path src = new Path(localSrc);
        Path dst = new Path(hdfsDst);
        hdfs.copyFromLocalFile(src, dst);

        //list all the files in the current direction
        FileStatus files[] = hdfs.listStatus(dst);
        System.out.println("Upload to \t" + conf.get("fs.default.name") + hdfsDst);
        for (FileStatus file : files) {
            System.out.println(file.getPath());
        }
    }

    //create a new file
    public void createFile(String fileName, String fileContent) throws IOException {
        Path dst = new Path(fileName);
        byte[] bytes = fileContent.getBytes();
        FSDataOutputStream output = hdfs.create(dst);
        output.write(bytes);
        System.out.println("new file \t" + conf.get("fs.default.name") + fileName);
    }

    /**
     * 使用lzo压缩算法去生成压缩文件.
     * @param fileName
     * @param fileContent
     * @throws IOException
     */
    public void createLzoCompressionFile(String fileName, String fileContent) throws IOException {

        OutputStream out = new FileOutputStream(new File("/data/tmp/123.lzo"));
        LzoAlgorithm algorithm = LzoAlgorithm.LZO1X;
        LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(algorithm, null);
        LzoOutputStream stream = new LzoOutputStream(out, compressor, 256);
        stream.write("我是中国人".getBytes("UTF-8"));
        stream.close();
    }

    public void readLzoCompressionFile(String path) throws IOException{
        FSDataInputStream hdfsInStream = hdfs.open(new Path(path));
        LzoCodec codec = new LzoCodec();
        codec.setConf(conf);
        CompressionInputStream compressionInputStream=codec.createInputStream(hdfsInStream);
        byte[] ioBuffer = new byte[1024];
        int readLen = compressionInputStream.read(ioBuffer);
        while(readLen!=-1)
        {
            System.out.write(ioBuffer, 0, readLen);
            readLen = compressionInputStream.read(ioBuffer);
        }
        hdfsInStream.close();
        hdfs.close();
    }

    public void readFile(String path) throws IOException{
        FSDataInputStream hdfsInStream = hdfs.open(new Path(path));


        byte[] ioBuffer = new byte[1024];
        int readLen = hdfsInStream.read(ioBuffer);
        while(readLen!=-1)
        {
            System.out.write(ioBuffer, 0, readLen);
            readLen = hdfsInStream.read(ioBuffer);
        }
        hdfsInStream.close();
        hdfs.close();
    }


    //list all files
    public void listFiles(String dirName) throws IOException {
        Path f = new Path(dirName);
        FileStatus[] status = hdfs.listStatus(f);
        System.out.println(dirName + " has all files:");
        for (int i = 0; i< status.length; i++) {
            System.out.println(status[i].getPath().toString());
        }
    }

    //judge a file existed? and delete it!
    public void deleteFile(String fileName) throws IOException {
        Path f = new Path(fileName);
        boolean isExists = hdfs.exists(f);
        if (isExists) {	//if exists, delete
            boolean isDel = hdfs.delete(f,true);
            System.out.println(fileName + "  delete? \t" + isDel);
        } else {
            System.out.println(fileName + "  exist? \t" + isExists);
        }
    }

    public static void main(String[] args) throws IOException {
        OperatingFiles ofs = new OperatingFiles();
//        System.out.println("\n=======create dir=======");
//        String dir = "/databee-test-log/2016-09-20";
        String dir = "/sparkHistoryLogs";
        ofs.createDir(dir);
//        File databeeLogDir=new File("/data/log/databee/log/2016-09-20");
//        File[] logs=databeeLogDir.listFiles();
//        String localPath="/data/log/databee/log/2016-09-20/";
//        for (File log:logs){
//            System.out.println("\n======= file  name:======="+localPath+log.getName());
//            ofs.copyFile(localPath+log.getName(), dir);
//        }
//        System.out.println("\n=======copy file=======");
//        String src = "/data/docs/hello.lzo";
//        ofs.copyFile(src, dir);
//        System.out.println("\n=======create a file=======");
//        String fileContent = "Hello, world! Just a test.";
//        ofs.createFile(dir+"/word.txt", fileContent);
//        fileContent = "Hello,lzo compression  world! Just a test.";
//        ofs.createLzoCompressionFile("lzo--2.lzo", fileContent);
//        ofs.listFiles("hdfs://localhost:9000/test/");
//        ofs.readLzoCompressionFile("hdfs://localhost:9000/sdk-log/FlumeData.1473238673324.lzo");
    }

}

