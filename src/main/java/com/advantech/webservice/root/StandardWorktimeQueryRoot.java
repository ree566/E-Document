//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2019.01.14 於 09:41:08 AM CST 
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
 *         &lt;element name="STANDARD_WORKTIME">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="STANDARD_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="LINE_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="UNIT_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ITEM_NO" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="STATION_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "standardworktime"
})
@XmlRootElement(name = "root")
public class StandardWorktimeQueryRoot {

    @XmlElement(name = "METHOD", required = true)
    protected StandardWorktimeQueryRoot.METHOD method;
    @XmlElement(name = "STANDARD_WORKTIME", required = true)
    protected StandardWorktimeQueryRoot.STANDARDWORKTIME standardworktime;

    public StandardWorktimeQueryRoot() {
        this.method = new StandardWorktimeQueryRoot.METHOD();
        this.standardworktime = new StandardWorktimeQueryRoot.STANDARDWORKTIME();
    }

    /**
     * 取得 method 特性的值.
     *
     * @return possible object is {@link Root.METHOD }
     *
     */
    public StandardWorktimeQueryRoot.METHOD getMETHOD() {
        return method;
    }

    /**
     * 設定 method 特性的值.
     *
     * @param value allowed object is {@link Root.METHOD }
     *
     */
    public void setMETHOD(StandardWorktimeQueryRoot.METHOD value) {
        this.method = value;
    }

    /**
     * 取得 standardworktime 特性的值.
     *
     * @return possible object is {@link Root.STANDARDWORKTIME }
     *
     */
    public StandardWorktimeQueryRoot.STANDARDWORKTIME getSTANDARDWORKTIME() {
        return standardworktime;
    }

    /**
     * 設定 standardworktime 特性的值.
     *
     * @param value allowed object is {@link Root.STANDARDWORKTIME }
     *
     */
    public void setSTANDARDWORKTIME(StandardWorktimeQueryRoot.STANDARDWORKTIME value) {
        this.standardworktime = value;
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
        protected String id = "WBASSO.QryStandardWorktime";

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
     *         &lt;element name="STANDARD_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="LINE_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="UNIT_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="ITEM_NO" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="STATION_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
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
        "standardid",
        "lineid",
        "unitno",
        "itemno",
        "stationid"
    })
    public static class STANDARDWORKTIME {

        @XmlElement(name = "STANDARD_ID", required = true, nillable = true)
        protected String standardid;
        @XmlElement(name = "LINE_ID", required = true, nillable = true)
        protected String lineid;
        @XmlElement(name = "UNIT_NO", required = true, nillable = true)
        protected String unitno;
        @XmlElement(name = "ITEM_NO", nillable = true)
        protected String itemno;
        @XmlElement(name = "STATION_ID", nillable = true)
        protected byte stationid = -1;

        /**
         * 取得 standardid 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getSTANDARDID() {
            return standardid;
        }

        /**
         * 設定 standardid 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setSTANDARDID(String value) {
            this.standardid = value;
        }

        /**
         * 取得 lineid 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getLINEID() {
            return lineid;
        }

        /**
         * 設定 lineid 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setLINEID(String value) {
            this.lineid = value;
        }

        /**
         * 取得 unitno 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getUNITNO() {
            return unitno;
        }

        /**
         * 設定 unitno 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setUNITNO(String value) {
            this.unitno = value;
        }

        /**
         * 取得 itemno 特性的值.
         *
         */
        public String getITEMNO() {
            return itemno;
        }

        /**
         * 設定 itemno 特性的值.
         *
         */
        public void setITEMNO(String value) {
            this.itemno = value;
        }

        /**
         * 取得 stationid 特性的值.
         *
         */
        public byte getSTATIONID() {
            return stationid;
        }

        /**
         * 設定 stationid 特性的值.
         *
         */
        public void setSTATIONID(byte value) {
            this.stationid = value;
        }

    }

}
