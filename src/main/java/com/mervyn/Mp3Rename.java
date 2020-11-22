package com.mervyn;

import com.mpatric.mp3agic.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class Mp3Rename {

    private static Logger logger = LogManager.getLogger(Mp3Rename.class);

    public static void main(String[] args) throws InvalidDataException, IOException, UnsupportedTagException {
        File dir = new File(System.getProperty("user.dir"));
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("mp3");
            }
        });
        for (File file : files) {
            rename(file);
        }
    }

    private static void rename(File file) throws InvalidDataException, IOException, UnsupportedTagException {
        String filename = file.getName();
        Mp3File mp3file = new Mp3File(filename);
        String artist = null;
        String title = null;
        if (mp3file.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            artist = id3v1Tag.getArtist();
            title = id3v1Tag.getTitle();
        }
        if (mp3file.hasId3v2Tag() && (StringUtils.isAllBlank(artist, title))) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            artist = id3v2Tag.getArtist();
            title = id3v2Tag.getTitle();
        }
        if (StringUtils.isNotBlank(artist) && StringUtils.isNotBlank(title)) {
            file.renameTo(new File(artist + " - " + title + filename.substring(filename.lastIndexOf("."))));
            logger.info(filename + " -> " + artist + " - " + title + filename.substring(filename.lastIndexOf(".")));
        } else {
            logger.info(filename + " 信息不正确： " + artist + " - " + title);
        }
    }
}
