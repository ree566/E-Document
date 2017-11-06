//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2017.11.01 於 09:32:37 AM CST 
//
package com.advantech.webservice.root;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>
 * anonymous complex type 的 Java 類別.
 *
 * <p>
 * 下列綱要片段會指定此類別中包含的預期內容.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="METHOD">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PART_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="USER_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="USER_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DEPT_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DEPT_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DEPT_DESC_CH" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "method",
    "partno",
    "userno",
    "userid",
    "deptid",
    "deptno",
    "deptdescch"
})
@XmlRootElement(name = "root")
public class ModelResponsorQueryRoot {

    @XmlElement(name = "METHOD", required = true, nillable = true)
    protected ModelResponsorQueryRoot.METHOD method;
    @XmlElement(name = "PART_NO", required = true, nillable = true)
    protected String partno;
    @XmlElement(name = "USER_NO", required = true, nillable = true)
    protected String userno;
    @XmlElement(name = "USER_ID", required = true, nillable = true)
    protected String userid;
    @XmlElement(name = "DEPT_ID", required = true, nillable = true)
    protected String deptid;
    @XmlElement(name = "DEPT_NO", required = true, nillable = true)
    protected String deptno;
    @XmlElement(name = "DEPT_DESC_CH", required = true, nillable = true)
    protected String deptdescch;

    public ModelResponsorQueryRoot() {
        this.method = new ModelResponsorQueryRoot.METHOD();
    }

    /**
     * 取得 method 特性的值.
     *
     * @return possible object is {@link Root.METHOD }
     *
     */
    public ModelResponsorQueryRoot.METHOD getMETHOD() {
        return method;
    }

    /**
     * 設定 method 特性的值.
     *
     * @param value allowed object is {@link Root.METHOD }
     *
     */
    public void setMETHOD(ModelResponsorQueryRoot.METHOD value) {
        this.method = value;
    }

    /**
     * 取得 partno 特性的值.
     *
     * @return possible object is {@link String }
     *
     */
    public String getPARTNO() {
        return partno;
    }

    /**
     * 設定 partno 特性的值.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setPARTNO(String value) {
        this.partno = value;
    }

    /**
     * 取得 userno 特性的值.
     *
     * @return possible object is {@link String }
     *
     */
    public String getUSERNO() {
        return userno;
    }

    /**
     * 設定 userno 特性的值.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setUSERNO(String value) {
        this.userno = value;
    }

    /**
     * 取得 userid 特性的值.
     *
     * @return possible object is {@link String }
     *
     */
    public String getUSERID() {
        return userid;
    }

    /**
     * 設定 userid 特性的值.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setUSERID(String value) {
        this.userid = value;
    }

    /**
     * 取得 deptid 特性的值.
     *
     * @return possible object is {@link String }
     *
     */
    public String getDEPTID() {
        return deptid;
    }

    /**
     * 設定 deptid 特性的值.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setDEPTID(String value) {
        this.deptid = value;
    }

    /**
     * 取得 deptno 特性的值.
     *
     * @return possible object is {@link String }
     *
     */
    public String getDEPTNO() {
        return deptno;
    }

    /**
     * 設定 deptno 特性的值.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setDEPTNO(String value) {
        this.deptno = value;
    }

    /**
     * 取得 deptdescch 特性的值.
     *
     * @return possible object is {@link String }
     *
     */
    public String getDEPTDESCCH() {
        return deptdescch;
    }

    /**
     * 設定 deptdescch 特性的值.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setDEPTDESCCH(String value) {
        this.deptdescch = value;
    }

    /**
     * <p>
     * anonymous complex type 的 Java 類別.
     *
     * <p>
     * 下列綱要片段會指定此類別中包含的預期內容.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class METHOD {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "ID")
        protected String id = "MIMSO.QryPart_Mapping_User";

        /**
         * 取得 value 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getValue() {
            return value;
        }

        /**
         * 設定 value 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * 取得 id 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getID() {
            return id;
        }

        /**
         * 設定 id 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setID(String value) {
            this.id = value;
        }

    }

}
