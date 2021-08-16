package com.example.penup.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.penup.interfaces.TaskCompleteCallback;
import com.example.penup.viewmodel.ListFolderViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

public class CopyPasteUtil {
    public static boolean isCopy;
    private static final int BUFF_SIZE = 4096;
    public CopyData copyData;
    private static CopyPasteUtil INSTANCE = null;
    private CopyPasteUtil(){};
    public static synchronized CopyPasteUtil getInstance(){
        if(INSTANCE == null){
            isCopy = false;
            INSTANCE = new CopyPasteUtil();
        }
        return INSTANCE;
    }
    public void copy(ArrayList<String> path, ArrayList<String> name){
        copyData = new CopyData(path,name);
        isCopy = true;
    }
    public boolean paste(Context context, String newLocation, TaskCompleteCallback callback){
        if(!isCopy){
            return false;
        }
        for(int i = 0; i<copyData.size; i++){
            copyFromTo(context,copyData.inputPath.get(i),newLocation,copyData.inputName.get(i),callback);
        }
        isCopy = false;
        return true;
    }
    public void copyFromTo(Context context, String from, String to, String name, TaskCompleteCallback callback){
        File source = FileHelper.getInstance().getFileFromPath(from+'/'+name);
        File target = FileHelper.getInstance().getFileFromPath(to+'/'+name);
        if(FileHelper.getInstance().isExist(target)){
            Log.d("Vector","File "+to+'/'+name+" exist!");
        }else{
            try {
                PipedOutputStream output = new PipedOutputStream();
                PipedInputStream input = new PipedInputStream(output);
                new ReadFileTasker(output, input, from, to, name).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new WriteFileTasker(context, output, input, from, to, name, callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private class ReadFileTasker extends AsyncTask<Void, Integer, Boolean> {
        String fromPath;
        String toPath;
        String name;
        PipedOutputStream output;
        PipedInputStream input;
        ReadFileTasker(PipedOutputStream output, PipedInputStream input, String f, String t, String n){
            fromPath = f;
            toPath = t;
            name = n;
            this.output = output;
            this.input = input;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            try(FileInputStream fileIn = new FileInputStream(fromPath+'/'+name)){
                long totalBytes = copyFromTo(fileIn, output);
            }catch (IOException e){
                e.printStackTrace(System.err);
            }finally {
                try{
                    output.close();
                }catch (IOException e){
                    e.printStackTrace(System.err);
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                Log.d("Vector","Read oke");
            }else{
                Log.d("Vector","Read false");

            }
        }

        private long copyFromTo(InputStream from, OutputStream to) throws IOException{
            byte[] buf = new byte[BUFF_SIZE];
            long total = 0;
            while(true){
                int r = from.read(buf);
                if(r==-1){
                    break;
                }
                to.write(buf,0,r);
                total+=r;
            }
            return total;
        }
    }
    private class WriteFileTasker extends AsyncTask<Void, Integer, Boolean>{
        String fromPath;
        String toPath;
        String name;
        PipedOutputStream output;
        PipedInputStream input;
        Context context;
        int id;
        int totalSize;
        TaskCompleteCallback callback;
        int minSizeNoti;
        WriteFileTasker(Context context, PipedOutputStream output, PipedInputStream input, String f, String t, String n, TaskCompleteCallback callback){
            fromPath = f;
            toPath = t;
            name = n;
            this.output = output;
            this.input = input;
            this.context = context;
            File file = new File(f+'/'+n);
            this.totalSize = (int)(file.length()/1024);
            minSizeNoti = 5000;
            if(totalSize>minSizeNoti){
                this.id = String.format(f+t+n).hashCode();
            }
            this.callback = callback;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            try(FileOutputStream fileOut = new FileOutputStream(toPath+'/'+name)){
                long totalBytes = copyFromTo(input, fileOut);
            }catch (IOException e){
                e.printStackTrace(System.err);
            }finally {
                try{
                    input.close();
                }catch (IOException e){
                    e.printStackTrace(System.err);
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                Log.d("Vector","Write done");
                this.callback.onTaskComplete(toPath+'/'+name);
            }else{
                Log.d("Vector", "Write false");
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
        private long copyFromTo(InputStream from, OutputStream to) throws IOException{
            byte[] buf = new byte[BUFF_SIZE];
            long total = 0;
            int count = 0;
            while(true){
                int r = from.read(buf);
                if(r==-1){
                    break;
                }
                to.write(buf,0,r);
                total+=r;
                count++;
                if(count%50==0){
                    publishProgress((int)(total/1024));
                }
            }
            return total;
        }
    }
    public class CopyData{
        public ArrayList<String> inputPath;
        public ArrayList<String> inputName;
        public int size;
        CopyData(ArrayList<String> inputPath, ArrayList<String> inputName){
            this.inputPath = inputPath;
            this.inputName = inputName;
            this.size = inputName.size();
        }
    }
}
