package FileProcess;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class processGfwData {
    private List<String> gfwList = new ArrayList<>();
    private List<String> newGfwList = new ArrayList<>();
    private List<String> searchList = new ArrayList<>();
    private String newGfwData;
    private Logger logger = Logger.getGlobal();
    private String fileName;
    private String server;
    private String ipset;

    public processGfwData(List<String> gfwList, String fileName) {
        this.gfwList = gfwList;
        this.fileName = fileName;
    }

    /** 写入文件-全局替换 */
    private void writeFile(List<String> StrList) {
        try (OutputStream output = new FileOutputStream(fileName)) {
            for (String str : StrList) {
                str = str + "\n";
                output.write(str.getBytes("UTF-8"));
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /** 新增一条 */
    public void addOne(String domain, String ip, int port) {
        logger.info("user input: " + domain + "/" + ip + ":" + +port);
        PrintWriter out = null;

        try {
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "utf-8"), true);
            server = "server=/" + domain + "/" + ip + "#" + port + "\n";
            ipset = "ipset=/" + domain + "/gfwlist" + "\n";
            out.write(server);
            out.write(ipset);
            logger.info("write is successful Data：\n" + server + ipset);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        } finally {
            if (out != null) {
                out.close();
                logger.info("file is closed");
            }
        }
    }

    /** 删除一条 */
    public void deleteOne(String domain) {
        logger.info("user input domain:" + domain);
        String searchReg = ("((server|ipset)=/" + domain + ")/.*");
        for (String str : gfwList) {
            Pattern searchPattern = Pattern.compile(searchReg);
            Matcher domainverify = searchPattern.matcher(str);
            if (!domainverify.matches()) {
                searchList.add(str);
            }
        }
        writeFile(searchList);
    }

    /** process file */
    public void replace(String ip, int port) {
        logger.info("user input:" + ip + "\n" + port);

        String ipReg = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern ipPattern = Pattern.compile(ipReg);
        Matcher ipVerify = ipPattern.matcher(ip);

        if (ipVerify.matches() && port < 65535) {
            String serverReg = ("(server=/[a-z0-9].*)/(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})#([0-9]{1,5})");
            String ipsetReg = ("(ipset=/[a-z0-9].*/[a-z0-9].*)");
            Pattern p = Pattern.compile(serverReg);
            Pattern ipsetP = Pattern.compile(ipsetReg);

            for (String gfwData : gfwList) {
                logger.fine("匹配数据：" + gfwData);

                Matcher m = p.matcher(gfwData);
                if (m.matches()) {
                    logger.fine("匹配成功：" + m.group(0));
                    newGfwData = m.group(1) + "/" + ip + "#" + port;/** 拼接新的字符串 */
                    newGfwList.add(newGfwData); /** 把心的字符串添加到list中去 */
                    logger.fine(newGfwData);
                }

                Matcher ipsetM = ipsetP.matcher(gfwData);
                if (ipsetM.matches()) {
                    logger.fine("匹配成功：" + ipsetM.group(0));
                    newGfwData = ipsetM.group(0);
                    newGfwList.add(newGfwData); /** 把心的字符串添加到list中去 */
                    logger.fine(ipsetM.group(1));
                }
            }

            writeFile(newGfwList);/** 把处理好的数据交给writefile处理。 */
        } else {
            logger.severe("输入的Ip或者端口不合法");
            return;
        }
    }

    public static void main(String[] args) {

    }
}