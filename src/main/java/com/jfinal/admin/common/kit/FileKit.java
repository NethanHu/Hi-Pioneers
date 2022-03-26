package com.jfinal.admin.common.kit;

import com.jfinal.kit.StrKit;

public class FileKit {

    // 试卷模块的拓展名列表
    private final static String[] paperFileExts = new String[]{"doc", "docx", "pdf", "txt", "md"};

    public static String getExtName(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index != -1 && (index + 1) < fileName.length()) {
            return fileName.substring(index + 1);
        } else {
            return null;
        }
    }

    /**
     * 如下是试卷检验拓展名模块
     * 通过检验试卷文件扩展名，判断是否为支持的试卷文件，支持则返回 true，否则返回 false
     */
    public static boolean isPaperExtName(String fileName) {
        if (StrKit.isBlank(fileName)) {
            return false;
        }
        fileName = fileName.trim().toLowerCase();
        String ext = getExtName(fileName);
        if (ext != null) {
            for (String s : paperFileExts) {
                if (s.equals(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final boolean notPaperExtName(String fileName) {
        return !isPaperExtName(fileName);
    }
}
