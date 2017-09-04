//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2017.09.04 於 11:23:15 AM CST 
//


package com.advantech.webservice.spe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>anonymous complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
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
 *         &lt;element name="MATERIAL_FLOW">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="MF_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ITEM_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="FLOW_RULE_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="FLOW_SEQ" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="ITEM_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="UNIT_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="UPDATE_FLOW_FLAG" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "materialflow"
})
@XmlRootElement(name = "root")
public class FlowRoot {

    @XmlElement(name = "METHOD", required = true)
    protected FlowRoot.METHOD method;
    @XmlElement(name = "MATERIAL_FLOW", required = true)
    protected FlowRoot.MATERIALFLOW materialflow;

    /**
     * 取得 method 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Root.METHOD }
     *     
     */
    public FlowRoot.METHOD getMETHOD() {
        return method;
    }

    /**
     * 設定 method 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link Root.METHOD }
     *     
     */
    public void setMETHOD(FlowRoot.METHOD value) {
        this.method = value;
    }

    /**
     * 取得 materialflow 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Root.MATERIALFLOW }
     *     
     */
    public FlowRoot.MATERIALFLOW getMATERIALFLOW() {
        return materialflow;
    }

    /**
     * 設定 materialflow 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link Root.MATERIALFLOW }
     *     
     */
    public void setMATERIALFLOW(FlowRoot.MATERIALFLOW value) {
        this.materialflow = value;
    }


    /**
     * <p>anonymous complex type 的 Java 類別.
     * 
     * <p>下列綱要片段會指定此類別中包含的預期內容.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="MF_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="ITEM_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="FLOW_RULE_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="FLOW_SEQ" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="ITEM_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="UNIT_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="UPDATE_FLOW_FLAG" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "mfid",
        "itemno",
        "flowruleid",
        "flowseq",
        "itemid",
        "unitno",
        "updateflowflag"
    })
    public static class MATERIALFLOW {

        @XmlElement(name = "MF_ID", required = true)
        protected String mfid;
        @XmlElement(name = "ITEM_NO", required = true)
        protected String itemno;
        @XmlElement(name = "FLOW_RULE_ID", required = true)
        protected String flowruleid;
        @XmlElement(name = "FLOW_SEQ")
        protected byte flowseq;
        @XmlElement(name = "ITEM_ID", required = true)
        protected String itemid;
        @XmlElement(name = "UNIT_NO", required = true)
        protected String unitno;
        @XmlElement(name = "UPDATE_FLOW_FLAG", required = true)
        protected String updateflowflag;

        /**
         * 取得 mfid 特性的值.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMFID() {
            return mfid;
        }

        /**
         * 設定 mfid 特性的值.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMFID(String value) {
            this.mfid = value;
        }

        /**
         * 取得 itemno 特性的值.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getITEMNO() {
            return itemno;
        }

        /**
         * 設定 itemno 特性的值.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setITEMNO(String value) {
            this.itemno = value;
        }

        /**
         * 取得 flowruleid 特性的值.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFLOWRULEID() {
            return flowruleid;
        }

        /**
         * 設定 flowruleid 特性的值.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFLOWRULEID(String value) {
            this.flowruleid = value;
        }

        /**
         * 取得 flowseq 特性的值.
         * 
         */
        public byte getFLOWSEQ() {
            return flowseq;
        }

        /**
         * 設定 flowseq 特性的值.
         * 
         */
        public void setFLOWSEQ(byte value) {
            this.flowseq = value;
        }

        /**
         * 取得 itemid 特性的值.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getITEMID() {
            return itemid;
        }

        /**
         * 設定 itemid 特性的值.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setITEMID(String value) {
            this.itemid = value;
        }

        /**
         * 取得 unitno 特性的值.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUNITNO() {
            return unitno;
        }

        /**
         * 設定 unitno 特性的值.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUNITNO(String value) {
            this.unitno = value;
        }

        /**
         * 取得 updateflowflag 特性的值.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUPDATEFLOWFLAG() {
            return updateflowflag;
        }

        /**
         * 設定 updateflowflag 特性的值.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUPDATEFLOWFLAG(String value) {
            this.updateflowflag = value;
        }

    }


    /**
     * <p>anonymous complex type 的 Java 類別.
     * 
     * <p>下列綱要片段會指定此類別中包含的預期內容.
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
        protected String id;

        /**
         * 取得 value 特性的值.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * 設定 value 特性的值.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * 取得 id 特性的值.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
            return id;
        }

        /**
         * 設定 id 特性的值.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setID(String value) {
            this.id = value;
        }

    }

}
