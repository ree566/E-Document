/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

import com.advantech.model.CellLineType;
import com.advantech.model.PassStation;
import com.advantech.model.PassStationRecords;
import com.advantech.model.TestRecord;
import com.advantech.model.TestRecords;
import com.advantech.model.UserOnMes;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
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

    private final URL url;//webservice位置(放在專案中，因為url無法讀取，裏頭標籤衝突)
    private static final Logger log = LoggerFactory.getLogger(WebServiceRV.class);

    private static WebServiceRV instance;

    private WebServiceRV() {
        url = this.getClass().getClassLoader().getResource("wsdl/Service.wsdl");
    }

    public static WebServiceRV getInstance() {
        if (instance == null) {
            instance = new WebServiceRV();
        }
        return instance;
    }

    //Get data from WebService
    private List<Object> getWebServiceData(String queryString) {
        Service service = new Service(url);
        ServiceSoap port = service.getServiceSoap();
        RvResponse.RvResult result = port.rv(queryString);
        return result.getAny();
    }

    private Document getWebServiceDataForDocument(String queryString) {
        List data = getWebServiceData(queryString);
        return ((Node) data.get(1)).getOwnerDocument();
    }

    private List getKanbanUsers() {
        String queryString = "<root>"
                + "<METHOD ID='ETLSO.QryProductionKanban4Test'/>"
                + "<KANBANTEST>"
                + "<STATION_ID>4,122,124,11,3,5,6,32,30,134,151,04,105</STATION_ID>"
                + "</KANBANTEST>"
                + "</root>";
        return this.getWebServiceData(queryString);
    }

    public List<String> getKanbanUsersForString() throws TransformerException, IOException {
        List list = new ArrayList();//ws = WebService
        List data = getKanbanUsers();
        for (Object obj : data) {
            Document doc = ((Node) obj).getOwnerDocument();
            try (StringWriter sw = new StringWriter()) {
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.transform(new DOMSource(doc), new StreamResult(sw));
                list.add(sw.toString());
            }
        }

        return list;
    }

    public String getKanbanWorkId(String jobnumber) {
        String today = getToday();
        String queryString = "<root><METHOD ID='WMPSO.QryWorkManPowerCard001'/><WORK_MANPOWER_CARD><WORK_ID>-1</WORK_ID><LINE_ID>-1</LINE_ID><STATION_ID>-1</STATION_ID><FACTORY_NO></FACTORY_NO><UNIT_NO></UNIT_NO>"
                + "<USER_NO>" + jobnumber + "</USER_NO>"
                + "<CARD_FLAG>1</CARD_FLAG>"
                + "<START_DATE>" + today + "</START_DATE>"
                + "<END_DATE>" + today + "</END_DATE>"
                + "</WORK_MANPOWER_CARD></root>";

        Document doc = this.getWebServiceDataForDocument(queryString);
        String childTagName = "WORK_ID";
        Element rootElement = doc.getDocumentElement();
        String requestQueueName = getString(childTagName, rootElement);
        return requestQueueName;
    }

    public String getModelnameByPo(String po) {
        String queryString = "<root><METHOD ID='WIPSO.QryWipAtt001'/><WIP_ATT><WIP_NO>"
                + po
                + "</WIP_NO><ITEM_NO></ITEM_NO></WIP_ATT></root>";
        Document doc = this.getWebServiceDataForDocument(queryString);
        String childTagName = "ITEM_NO";
        Element rootElement = doc.getDocumentElement();
        String requestQueueName = getString(childTagName, rootElement);
        return requestQueueName;
    }

    public UserOnMes getMESUser(String jobnumber) {
        try {
            String queryString = "<root><METHOD ID='PLBSO.QryLogion'/><USER_INFO><USER_NO>"
                    + jobnumber
                    + "</USER_NO><PASSWORD></PASSWORD><STATUS>A</STATUS></USER_INFO></root>";

            List l = this.getWebServiceData(queryString);
            Document doc = ((Node) l.get(1)).getOwnerDocument();
            //Skip the <diffgr:diffgram> tag, read QryData tag directly.
            Node node = doc.getFirstChild().getFirstChild().getFirstChild();

            Object o = this.unmarshalFromList(node, UserOnMes.class);

            return o == null ? null : (UserOnMes) o;
        } catch (Exception ex) {
            log.error(ex.toString());
            return null;
        }
    }

    public List<PassStation> getPassStationRecords(String po, String type) {
        String stations;
        if (CellLineType.BAB.toString().equals(type)) {
            stations = "'2','20'";
        } else if (CellLineType.PKG.toString().equals(type)) {
            stations = "'53','28'";
        } else {
            return new ArrayList();
        }

        try {
            String queryString
                    = "<root><METHOD ID='ETLSO.QryT_SnPassTime001'/><WIP_INFO><WIP_NO>"
                    + po
                    + "</WIP_NO><UNIT_NO></UNIT_NO><LINE_ID></LINE_ID><STATION_ID>"
                    + stations
                    + "</STATION_ID></WIP_INFO></root>";

            List l = this.getWebServiceData(queryString);
            Document doc = ((Node) l.get(1)).getOwnerDocument();
            //Skip the <diffgr:diffgram> tag, read QryData tag directly.
            Node node = doc.getFirstChild().getFirstChild();

            Object o = this.unmarshalFromList(node, PassStationRecords.class);
            return o == null ? new ArrayList() : ((PassStationRecords) o).getQryData();
        } catch (Exception ex) {
            log.error(ex.toString());
            return new ArrayList();
        }
    }

    public List<TestRecord> getTestLineTypeRecords() {
        try {
            List l = this.getKanbanUsers();
            Document doc = ((Node) l.get(1)).getOwnerDocument();
            //Skip the <diffgr:diffgram> tag, read QryData tag directly.
            Node node = doc.getFirstChild().getFirstChild();

            Object o = this.unmarshalFromList(node, TestRecords.class);

            return o == null ? new ArrayList() : ((TestRecords) o).getQryData();
        } catch (Exception ex) {
            log.error(ex.toString());
            return new ArrayList();
        }
    }

    private Object unmarshalFromList(Node node, Class clz) throws JAXBException {

        //Unmarshal the data into javaObject.
        JAXBContext jc = JAXBContext.newInstance(clz);
        Unmarshaller u = jc.createUnmarshaller();

        return node == null ? null : u.unmarshal(node);
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

    private String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
