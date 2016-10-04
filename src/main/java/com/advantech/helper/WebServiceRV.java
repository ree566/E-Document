/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.entity.TestLineTypeUser;
import com.advantech.entity.TestLineTypeUsers;
import com.advantech.entity.User;
import java.io.StringWriter;
import static java.lang.System.out;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.RvResponse;
import org.tempuri.Service;
import org.tempuri.ServiceSoap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Wei.Cheng
 */
public class WebServiceRV {

    private final String queryString;
    private final URL url;//webservice位置(放在專案中，因為url無法讀取，裏頭標籤衝突)
    private static final Logger log = LoggerFactory.getLogger(WebServiceRV.class);

    private static WebServiceRV instance;

    private WebServiceRV() {
        queryString = "<root>"
                + "<METHOD ID='ETLSO.QryProductionKanban4Test'/>"
                + "<KANBANTEST>"
                + "<STATION_ID>4,122,124,11,3,5,6,32,30,134,151,04,105</STATION_ID>"
                + "</KANBANTEST>"
                + "</root>";
        url = this.getClass().getClassLoader().getResource("wsdl/Service.wsdl");
    }

    public static WebServiceRV getInstance() {
        if (instance == null) {
            instance = new WebServiceRV();
        }
        return instance;
    }

    //Get data from WebService
    private List<Object> getWebServiceData(String queryString) throws Exception {
        Service service = new Service(url);
        ServiceSoap port = service.getServiceSoap();
        RvResponse.RvResult result = port.rv(queryString);
        return result.getAny();
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public Document getKanbanUsers(String queryString) throws Exception {
        List data = getWebServiceData(queryString);
        return ((Element) data.get(1)).getOwnerDocument();
    }

    public List<String> getKanbanUsersForString() throws Exception {
        return this.getKanbanUsersForString(queryString);
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public List<String> getKanbanUsersForString(String queryString) throws Exception {
        List list = new ArrayList();//ws = WebService
        List data = getWebServiceData(queryString);
        for (Object obj : data) {
            Document doc = ((Element) obj).getOwnerDocument();
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            list.add(sw.toString());
            sw.close();
        }

        return list;
    }

    public Document getKanbanUserInHistory(String jobnumber) throws Exception {
        String today = getToday();
        String str = "<root><METHOD ID='WMPSO.QryWorkManPowerCard001'/><WORK_MANPOWER_CARD><WORK_ID>-1</WORK_ID><LINE_ID>-1</LINE_ID><STATION_ID>-1</STATION_ID><FACTORY_NO></FACTORY_NO><UNIT_NO></UNIT_NO>"
                + "<USER_NO>" + jobnumber + "</USER_NO>"
                + "<CARD_FLAG>1</CARD_FLAG>"
                + "<START_DATE>" + today + "</START_DATE>"
                + "<END_DATE>" + today + "</END_DATE>"
                + "</WORK_MANPOWER_CARD></root>";
        return this.getKanbanUsers(str);
    }

    public String getKanbanWorkId(String jobnumber) throws Exception {
        Document doc = this.getKanbanUserInHistory(jobnumber);
        String childTagName = "WORK_ID";
        Element rootElement = doc.getDocumentElement();
        String requestQueueName = getString(childTagName, rootElement);
        return requestQueueName;
    }

    private String getString(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }

        return null;
    }

    public List<TestLineTypeUser> getKanbanUsersForXml() {
        try {
            List l = this.getWebServiceData(queryString);
            Document doc = ((Element) l.get(1)).getOwnerDocument();

            //Unmarshal the data into javaObject.
            JAXBContext jc = JAXBContext.newInstance(TestLineTypeUsers.class);
            Unmarshaller u = jc.createUnmarshaller();

            //Skip the <diffgr:diffgram> tag, read root tag directly.
            Node node = (Node) doc.getFirstChild().getFirstChild();
            TestLineTypeUsers users = (TestLineTypeUsers) u.unmarshal(node);
            return users.getQryData();
        } catch (Exception ex) {
            log.error(ex.toString());
            return new ArrayList();
        }
    }

    public User getMESUser(String jobnumber) {
        try {
            String str = "<root><METHOD ID='PLBSO.QryLogion'/><USER_INFO><USER_NO>"
                    + jobnumber
                    + "</USER_NO><PASSWORD></PASSWORD><STATUS>A</STATUS></USER_INFO></root>";
            List l = this.getWebServiceData(str);
            Document doc = ((Element) l.get(1)).getOwnerDocument();

            //Unmarshal the data into javaObject.
            JAXBContext jc = JAXBContext.newInstance(User.class);
            Unmarshaller u = jc.createUnmarshaller();

            //Skip the <diffgr:diffgram> tag, read QryData tag directly.
            Node node = (Node) doc.getFirstChild().getFirstChild().getFirstChild();
            User user = (User) u.unmarshal(node);
            return user;
        } catch (Exception ex) {
            log.error(ex.toString());
            return null;
        }
    }

    private String getToday() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sdFormat.format(new Date());
    }

    public static void main(String arg[]) {

        out.print(WebServiceRV.getInstance().getToday());
    }

}
