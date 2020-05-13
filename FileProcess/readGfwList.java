package FileProcess;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class readGfwList {
    private final String filePath;
    private List<String> gfwList = new ArrayList<>();
    private final Logger logger = Logger.getGlobal();

    /** save filepath */
    public readGfwList(final String filePath) {
        this.filePath = filePath;
        logger.setLevel(Level.INFO);/** 设置log lever */
    }

    /** read file */
    private void readFile() {
        final File gfwFile = new File(this.filePath);
        try {
            if (gfwFile.exists() && gfwFile.isFile()) {
                logger.info("read file:" + gfwFile.getAbsolutePath());
                final Scanner gfwData = new Scanner(Paths.get(gfwFile.getAbsolutePath()), "UTF-8");
                while (gfwData.hasNextLine()) {
                    gfwList.add(gfwData.nextLine());
                }
                logger.info("file read completed");
            } else {
                logger.severe("Not find file " + gfwFile.getAbsolutePath());
            }
        } catch (final Exception e) {
            logger.severe("read File error:" + e.getMessage());
        }
    }

    public List<String> getFileData() {
        readFile();
        return this.gfwList;
    }

    public static void main(final String[] args) {
        final readGfwList read = new readGfwList("dnsmasq_gfwlist_ipset.conf");
        List<String> gfwList = read.getFileData();

        processGfwData processGfw = new processGfwData(gfwList, "test.conf");
        processGfw.replace("127.0.110.1", 5353);
        processGfw.addOne("domain", "127.110.1", 1234);
        processGfw.deleteOne("030buy.com");
    }
}
