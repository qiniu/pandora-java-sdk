package com.qiniu.pandora.pipeline.points;

import java.io.Closeable;
import java.io.IOException;
import java.util.Scanner;

/**
 * 从文件迭代读取的懒式迭代
 */
public class FilePointsGenerator extends AbstractPointsGenerator implements Closeable {

    private Scanner scanner;

    public FilePointsGenerator(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNextLine();
    }

    @Override
    public Point next() {
        return Point.fromPointString(scanner.nextLine());
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }
}


