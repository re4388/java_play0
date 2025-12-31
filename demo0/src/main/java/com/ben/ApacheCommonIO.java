package com.ben;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class ApacheCommonIO {
    public static void main(String[] args) throws Exception {

//        String s = FileUtils.readFileToString(new File("a1.txt"), StandardCharsets.UTF_8);
//        System.out.println(s);

        //     public static Collection<File> listFilesAndDirs(final File directory, final IOFileFilter fileFilter, final IOFileFilter dirFilter) {


//        IOFileFilter ioFileFilter0 = new IOFileFilter() {
//            @Override
//            public boolean accept(File file) {
//                return true;
//            }
//
//            @Override
//            public boolean accept(File dir, String name) {
//                return true;
//            }
//        };
//
//
//        Collection<File> files = FileUtils.listFilesAndDirs(new File("."), ioFileFilter0, ioFileFilter0);
//        System.out.println(files);


        IOFileFilter filter = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".txt"));
        FileAlterationObserver observer = new FileAlterationObserver(new File("."), filter);

        FileAlterationListener fileAlterationListener = new FileAlterationListener() {
            @Override
            public void onDirectoryChange(File directory) {

            }

            @Override
            public void onDirectoryCreate(File directory) {

            }

            @Override
            public void onDirectoryDelete(File directory) {

            }

            @Override
            public void onFileChange(File file) {
                System.out.println(file.getName() + " is changed");
            }

            @Override
            public void onFileCreate(File file) {

            }

            @Override
            public void onFileDelete(File file) {

            }

            @Override
            public void onStart(FileAlterationObserver observer) {

            }

            @Override
            public void onStop(FileAlterationObserver observer) {

            }
        };
        observer.addListener( fileAlterationListener);
        FileAlterationMonitor monitor = new FileAlterationMonitor(1000L);
        monitor.addObserver(observer);
        monitor.start();
        try { TimeUnit.SECONDS.sleep(20);} catch (Exception e) {throw new RuntimeException(e);}
        monitor.stop();





    }
}