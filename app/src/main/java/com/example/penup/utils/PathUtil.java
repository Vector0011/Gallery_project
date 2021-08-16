package com.example.penup.utils;

import java.io.File;

public class PathUtil {
    private static PathUtil INSTANCE = null;
    private PathUtil(){};
    public static synchronized PathUtil getInstance(){
        if(INSTANCE == null){
            INSTANCE = new PathUtil();
        }
        return INSTANCE;
    }
    public String getFolderFromPath(String path){
        boolean endsWithSlash = path.endsWith(File.separator);
        return path.substring(0, path.lastIndexOf(File.separatorChar,
                endsWithSlash ? path.length() - 2 : path.length() - 1));
    }
}
