//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2017.11.17 於 11:25:50 AM CST 
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
 *         &lt;element name="MATERIAL_FLOW">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="UNIT_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="MF_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="ITEM_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="ITEM_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="FLOW_RULE_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="STATUS" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
public class MaterialFlowQueryRoot {

    @XmlElement(name = "METHOD", required = true)
    protected MaterialFlowQueryRoot.METHOD method;
    @XmlElement(name = "MATERIAL_FLOW", required = true)
    protected MaterialFlowQueryRoot.MATERIALFLOW materialflow;

    public MaterialFlowQueryRoot() {
        this.method = new MaterialFlowQueryRoot.METHOD();
        this.materialflow = new MaterialFlowQueryRoot.MATERIALFLOW();
    }

    public MaterialFlowQueryRoot(String unitno, String itemno) {
        this.method = new MaterialFlowQueryRoot.METHOD();
        this.materialflow = new MaterialFlowQueryRoot.MATERIALFLOW(unitno, itemno);
    }

    /**
     * 取得 method 特性的值.
     *
     * @return possible object is {@link Root.METHOD }
     *
     */
    public MaterialFlowQueryRoot.METHOD getMETHOD() {
        return method;
    }

    /**
     * 設定 method 特性的值.
     *
     * @param value allowed object is {@link Root.METHOD }
     *
     */
    public void setMETHOD(MaterialFlowQueryRoot.METHOD value) {
        this.method = value;
    }

    /**
     * 取得 materialflow 特性的值.
     *
     * @return possible object is {@link Root.MATERIALFLOW }
     *
     */
    public MaterialFlowQueryRoot.MATERIALFLOW getMATERIALFLOW() {
        return materialflow;
    }

    /**
     * 設定 materialflow 特性的值.
     *
     * @param value allowed object is {@link Root.MATERIALFLOW }
     *
     */
    public void setMATERIALFLOW(MaterialFlowQueryRoot.MATERIALFLOW value) {
        this.materialflow = value;
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
     *         &lt;element name="UNIT_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="MF_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="ITEM_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="ITEM_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="FLOW_RULE_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="STATUS" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "unitno",
        "mfid",
        "itemid",
        "itemno",
        "flowruleid",
        "status"
    })
    public static class MATERIALFLOW {

        @XmlElement(name = "UNIT_NO", required = true, nillable = true)
        protected String unitno;
        @XmlElement(name = "MF_ID", nillable = true)
        protected byte mfid = -1;
        @XmlElement(name = "ITEM_ID", nillable = true)
        protected byte itemid = -1;
        @XmlElement(name = "ITEM_NO", required = true, nillable = true)
        protected String itemno;
        @XmlElement(name = "FLOW_RULE_ID", nillable = true)
        protected byte flowruleid = -1;
        @XmlElement(name = "STATUS", required = true)
        protected String status = "";

        public MATERIALFLOW() {
        }

        public MATERIALFLOW(String unitno, String itemno) {
            this.unitno = unitno;
            this.itemno = itemno;
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
         * 取得 mfid 特性的值.
         *
         */
        public byte getMFID() {
            return mfid;
        }

        /**
         * 設定 mfid 特性的值.
         *
         */
        public void setMFID(byte value) {
            this.mfid = value;
        }

        /**
         * 取得 itemid 特性的值.
         *
         */
        public byte getITEMID() {
            return itemid;
        }

        /**
         * 設定 itemid 特性的值.
         *
         */
        public void setITEMID(byte value) {
            this.itemid = value;
        }

        /**
         * 取得 itemno 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getITEMNO() {
            return itemno;
        }

        /**
         * 設定 itemno 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setITEMNO(String value) {
            this.itemno = value;
        }

        /**
         * 取得 flowruleid 特性的值.
         *
         */
        public byte getFLOWRULEID() {
            return flowruleid;
        }

        /**
         * 設定 flowruleid 特性的值.
         *
         */
        public void setFLOWRULEID(byte value) {
            this.flowruleid = value;
        }

        /**
         * 取得 status 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getSTATUS() {
            return status;
        }

        /**
         * 設定 status 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setSTATUS(String value) {
            this.status = value;
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
        protected String id = "INITSO.QryMaterialFlow";

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
