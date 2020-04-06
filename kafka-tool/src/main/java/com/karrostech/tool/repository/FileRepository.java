package com.karrostech.tool.repository;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FileRepository {

    public void save(String file, String content) {
        try {
            FileUtils.writeStringToFile(new File(file), content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Cannot save to file. {}", e.getMessage());
        }
    }

    public List<String> list(String path, String[] extensions) {
        try {
            Collection<File> files = FileUtils.listFiles(new File(path), extensions, false);
            List<String> fileNames = files.stream().map(f -> f.getName()).collect(Collectors.toList());

            return fileNames;
        } catch (Exception ex) {
            log.error("Cannot list files. {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    public List<String> listV2(String path, String[] extensions) {
        try {
            Collection<File> files = FileUtils.listFiles(new File(path), extensions, true);
            List<String> fileNames = files.stream().map(f -> f.getPath()).collect(Collectors.toList());

            return fileNames;
        } catch (Exception ex) {
            log.error("Cannot list files. {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    public String read(String path) {
        try {
            return FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Cannot read file. {}", e.getMessage());
            return null;
        }
    }

    public void delete(String path) {
        FileUtils.deleteQuietly(new File(path));
    }

    public void zipFolder(String fileName, String folder) throws ZipException {
        ZipFile zipFile = new ZipFile(fileName);
        zipFile.addFolder(new File(folder));
    }
}
