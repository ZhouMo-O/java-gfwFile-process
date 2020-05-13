package FileProcess;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class processGfwData {
    private List<String> gfwList = new ArrayList<>();
    private List<String> newGfwList = new ArrayList<>();
    private String newGfwData;
    private Logger logger = Logger.getGlobal();

    public processGfwData(List<String> gfwList) {
        this.gfwList = gfwList;

    }

    private void createFile(List<String> StrList) {
        try (OutputStream output = new FileOutputStream("./test.txt")) {
            for (String str : StrList) {
                str = str + "\n";
                output.write(str.getBytes("UTF-8"));
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /** process file */
    public void replace(String ip, int port) {
        String ipReg = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern ipPattern = Pattern.compile(ipReg);
        Matcher ipVerify = ipPattern.matcher(ip);

        if (ipVerify.matches() && port < 65535) {
            String reg = ("(server=/[a-z0-9].*)/(([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})#([0-9]{1,5})");
            Pattern p = Pattern.compile(reg);
            for (String gfwData : gfwList) {
                logger.fine("匹配数据：" + gfwData);
                Matcher m = p.matcher(gfwData);
                if (m.matches()) {
                    logger.fine("匹配成功：" + m.group(0));
                    newGfwData = m.group(1) + "/" + ip + "#" + port;
                    newGfwList.add(newGfwData);
                    logger.fine(newGfwData);
                }
            }
            createFile(newGfwList);

        } else {
            logger.severe("输入的Ip或者端口不合法");
            return;
        }

    }

    public static void main(String[] args) {

    }
}