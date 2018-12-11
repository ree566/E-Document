//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2018.12.10 於 01:29:14 PM CST 
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
 *         &lt;element name="STANDARD_WORKTIME_REASON">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="REASON_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="STATUS_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "standardworktimereason"
})
@XmlRootElement(name = "root")
public class StandardWorkReasonQueryRoot {

    @XmlElement(name = "METHOD", required = true)
    protected StandardWorkReasonQueryRoot.METHOD method;
    @XmlElement(name = "STANDARD_WORKTIME_REASON", required = true)
    protected StandardWorkReasonQueryRoot.STANDARDWORKTIMEREASON standardworktimereason;

    public StandardWorkReasonQueryRoot() {
        this.method = new StandardWorkReasonQueryRoot.METHOD();
        this.standardworktimereason = new StandardWorkReasonQueryRoot.STANDARDWORKTIMEREASON();
    }

    /**
     * 取得 method 特性的值.
     *
     * @return possible object is {@link Root.METHOD }
     *
     */
    public StandardWorkReasonQueryRoot.METHOD getMETHOD() {
        return method;
    }

    /**
     * 設定 method 特性的值.
     *
     * @param value allowed object is {@link Root.METHOD }
     *
     */
    public void setMETHOD(StandardWorkReasonQueryRoot.METHOD value) {
        this.method = value;
    }

    /**
     * 取得 standardworktimereason 特性的值.
     *
     * @return possible object is {@link Root.STANDARDWORKTIMEREASON }
     *
     */
    public StandardWorkReasonQueryRoot.STANDARDWORKTIMEREASON getSTANDARDWORKTIMEREASON() {
        return standardworktimereason;
    }

    /**
     * 設定 standardworktimereason 特性的值.
     *
     * @param value allowed object is {@link Root.STANDARDWORKTIMEREASON }
     *
     */
    public void setSTANDARDWORKTIMEREASON(StandardWorkReasonQueryRoot.STANDARDWORKTIMEREASON value) {
        this.standardworktimereason = value;
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
        protected String id = "WBASSO.QryStandardWorkReason";

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
     *         &lt;element name="REASON_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="STATUS_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "reasonno",
        "statusno"
    })
    public static class STANDARDWORKTIMEREASON {

        @XmlElement(name = "REASON_NO", required = true)
        protected String reasonno;
        @XmlElement(name = "STATUS_NO", required = true)
        protected String statusno = "A";

        /**
         * 取得 reasonno 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getREASONNO() {
            return reasonno;
        }

        /**
         * 設定 reasonno 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setREASONNO(String value) {
            this.reasonno = value;
        }

        /**
         * 取得 statusno 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getSTATUSNO() {
            return statusno;
        }

        /**
         * 設定 statusno 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setSTATUSNO(String value) {
            this.statusno = value;
        }

    }

}
