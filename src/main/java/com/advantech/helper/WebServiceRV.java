/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.entity.TestLineTypeUser;
import com.advantech.entity.TestLineTypeUsers;
import com.google.gson.Gson;
import java.io.StringWriter;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
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

    private final String sParam;
    private final URL url;//webservice位置(放在專案中，因為url無法讀取，裏頭標籤衝突)
    private static final Logger log = LoggerFactory.getLogger(WebServiceRV.class);

    private static WebServiceRV instance;

    private WebServiceRV() {
        sParam = "<root>"
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
    private List<Object> getWebServiceData() throws Exception {
        Service service = new Service(url);
        ServiceSoap port = service.getServiceSoap();
        RvResponse.RvResult result = port.rv(sParam);
        return result.getAny();
    }

    @SuppressWarnings("ConvertToTryWithResources")
    public List<String> getXMLString() throws Exception {
        List list = new ArrayList();//ws = WebService
        List data = getWebServiceData();
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

//    public JSONArray getKanbantestUsers() {
//        try {
//            List<String> list = getXMLString();
//            JSONObject xmlJSONObj = XML.toJSONObject(list.toString());
//            return xmlJSONObj.getJSONObject("diffgr:diffgram").getJSONObject("root").getJSONArray("QryData");
//        } catch (JSONException e) {
////            log.error(e.toString());
//            return new JSONArray();//Break if KanbanService is in error.
//        } catch (Exception e) {
//            log.error(e.toString());
//            return new JSONArray();
//        }
//    }

    public List<TestLineTypeUser> getKanbantestUsers() {
        try {
            List l = this.getWebServiceData();
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

    public static void main(String arg[]) {

    }

    private JSONArray putJ(JSONArray j) {
        return j.put(3);
    }
}
