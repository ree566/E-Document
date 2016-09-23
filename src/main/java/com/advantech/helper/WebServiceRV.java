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
import java.util.ArrayList;
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

/**
 *
 * @author Wei.Cheng
 */
public class WebServiceRV {

    private final String queryKanbanUsers;
    private final URL url;//webservice位置(放在專案中，因為url無法讀取，裏頭標籤衝突)
    private static final Logger log = LoggerFactory.getLogger(WebServiceRV.class);

    private static WebServiceRV instance;

    private WebServiceRV() {
        queryKanbanUsers = "<root>"
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

    public List<String> queryForXml() throws Exception {
        return this.queryForXml(queryKanbanUsers);
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public List<String> queryForXml(String queryString) throws Exception {
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

    public List<TestLineTypeUser> getKanbantestUser() {
        try {
            List l = this.getWebServiceData(queryKanbanUsers);
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
            String queryString = "<root><METHOD ID='PLBSO.QryLogion'/><USER_INFO><USER_NO>"
                    + jobnumber
                    + "</USER_NO><PASSWORD></PASSWORD><STATUS>A</STATUS></USER_INFO></root>";
            List l = this.getWebServiceData(queryString);
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

    public static void main(String arg[]) {
        String str = "<root><METHOD ID='PLBSO.QryLogion'/><USER_INFO><USER_NO>A-7976</USER_NO><PASSWORD></PASSWORD><STATUS>A</STATUS></USER_INFO></root>";
        List<String> l;
        try {
            l = WebServiceRV.getInstance().queryForXml(str);
            for (String st : l) {
                out.println(st);
            }
        } catch (Exception ex) {
            out.println(ex);
        }

    }

}
