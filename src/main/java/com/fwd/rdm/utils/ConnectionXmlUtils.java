package com.fwd.rdm.utils;

import com.fwd.rdm.data.domain.ConnectionProperties;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocument;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 12:13 2018/11/10
 */
public class ConnectionXmlUtils {

    private static final String FILE_CONFIG_DIR = ".myrdm";
    private static final String FILE_CONFIG_NAME = "connection.xml";

    private static String getFullFileName() {
        String userHomeDir = System.getProperty("user.home");
        return userHomeDir + File.separator + FILE_CONFIG_DIR + File.separator + FILE_CONFIG_NAME;
    }

    public static List<ConnectionProperties> load() {
        String fullFileName = getFullFileName();
        File configFile = new File(fullFileName);
        if (!configFile.exists()) {
            return Collections.emptyList();
        }
        try {
            List<ConnectionProperties> resultList = new ArrayList<>();
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(configFile);
            if (document == null) {
                return Collections.emptyList();
            }
            Element rootElement = document.getRootElement();
            if (rootElement == null) {
                return Collections.emptyList();
            }
            Iterator conPropertiesIterator = rootElement.elementIterator("connectionProperties");
            while (conPropertiesIterator.hasNext()) {
                Element conPropertiesElement = (Element) conPropertiesIterator.next();
                Iterator attributeIterator = conPropertiesElement.elementIterator();
                ConnectionProperties connectionProperties = new ConnectionProperties();
                while (attributeIterator.hasNext()) {
                    Element attributeElement = (Element) attributeIterator.next();
                    String name = attributeElement.getName();
                    Object data = attributeElement.getData();
                    if ("id".equalsIgnoreCase(name) && !StringUtils.isEmpty(data)) {
                        connectionProperties.setId(Long.valueOf(String.valueOf(data)));
                    } else if ("name".equalsIgnoreCase(name)) {
                        connectionProperties.setName(String.valueOf(data == null ? "" : data));
                    } else if ("ip".equalsIgnoreCase(name)) {
                        connectionProperties.setIp(String.valueOf(data == null ? "" : data));
                    } else if ("port".equalsIgnoreCase(name) && !StringUtils.isEmpty(data)) {
                        connectionProperties.setPort(Integer.valueOf(String.valueOf(data)));
                    } else if ("auth".equalsIgnoreCase(name)) {
                        connectionProperties.setAuth(String.valueOf(data == null ? "" : data));
                    } else if ("orderNo".equalsIgnoreCase(name)) {
                        connectionProperties.setOrderNo(Integer.valueOf(String.valueOf(data == null ? 0 : data)));
                    }
                }
                resultList.add(connectionProperties);
            }
            return resultList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void save(List<ConnectionProperties> list) throws IOException {
        List<ConnectionProperties> writeList = new ArrayList<>(list);
        writeList.sort(Comparator.comparingInt(ConnectionProperties::getOrderNo));
        Document document = new DefaultDocument();
        Element rootElement = document.addElement("root");
        for (int i = 0; i < writeList.size(); i++) {
            ConnectionProperties connectionProperties = writeList.get(i);
            Element conElement = rootElement.addElement("connectionProperties");
            Element id = conElement.addElement("id");
            id.setText(getString(connectionProperties.getId()));
            Element name = conElement.addElement("name");
            name.setText(getString(connectionProperties.getName()));
            Element ip = conElement.addElement("ip");
            ip.setText(getString(connectionProperties.getIp()));
            Element port = conElement.addElement("port");
            port.setText(getString(connectionProperties.getPort()));
            Element auth = conElement.addElement("auth");
            auth.setText(getString(connectionProperties.getAuth()));
            Element sort = conElement.addElement("orderNo");
            sort.setText(getString(i));
        }
        String fullFileName = getFullFileName();
        File file = new File(fullFileName);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        }
        if (file.exists()) {
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), OutputFormat.createPrettyPrint());
            writer.write(document);
            writer.close();
        }
    }

    private static String getString(Object object) {
        return object == null ? "" : String.valueOf(object);
    }
}
