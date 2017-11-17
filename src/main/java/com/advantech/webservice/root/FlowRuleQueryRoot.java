//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2017.11.17 於 11:24:44 AM CST 
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
 *         &lt;element name="FLOW_RULE">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="FLOW_RULE_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="UNIT_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="FLOW_RULE_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="FLOW_TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "flowrule"
})
@XmlRootElement(name = "root")
public class FlowRuleQueryRoot {

    @XmlElement(name = "METHOD", required = true)
    protected FlowRuleQueryRoot.METHOD method;
    @XmlElement(name = "FLOW_RULE", required = true)
    protected FlowRuleQueryRoot.FLOWRULE flowrule;

    public FlowRuleQueryRoot() {
        this.method = new FlowRuleQueryRoot.METHOD();
        this.flowrule = new FlowRuleQueryRoot.FLOWRULE();
    }

    public FlowRuleQueryRoot(String unitno, String flowrulename) {
        this.method = new FlowRuleQueryRoot.METHOD();
        this.flowrule = new FlowRuleQueryRoot.FLOWRULE(unitno, flowrulename);
    }

    /**
     * 取得 method 特性的值.
     *
     * @return possible object is {@link Root.METHOD }
     *
     */
    public FlowRuleQueryRoot.METHOD getMETHOD() {
        return method;
    }

    /**
     * 設定 method 特性的值.
     *
     * @param value allowed object is {@link Root.METHOD }
     *
     */
    public void setMETHOD(FlowRuleQueryRoot.METHOD value) {
        this.method = value;
    }

    /**
     * 取得 flowrule 特性的值.
     *
     * @return possible object is {@link Root.FLOWRULE }
     *
     */
    public FlowRuleQueryRoot.FLOWRULE getFLOWRULE() {
        return flowrule;
    }

    /**
     * 設定 flowrule 特性的值.
     *
     * @param value allowed object is {@link Root.FLOWRULE }
     *
     */
    public void setFLOWRULE(FlowRuleQueryRoot.FLOWRULE value) {
        this.flowrule = value;
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
     *         &lt;element name="FLOW_RULE_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="UNIT_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="FLOW_RULE_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="FLOW_TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "flowruleid",
        "unitno",
        "flowrulename",
        "flowtype"
    })
    public static class FLOWRULE {

        @XmlElement(name = "FLOW_RULE_ID", nillable = true)
        protected byte flowruleid = -1;
        @XmlElement(name = "UNIT_NO", required = true, nillable = true)
        protected String unitno;
        @XmlElement(name = "FLOW_RULE_NAME", required = true, nillable = true)
        protected String flowrulename;
        @XmlElement(name = "FLOW_TYPE", required = true)
        protected String flowtype = "";

        public FLOWRULE() {
        }

        public FLOWRULE(String unitno, String flowrulename) {
            this.unitno = unitno;
            this.flowrulename = flowrulename;
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
         * 取得 flowrulename 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getFLOWRULENAME() {
            return flowrulename;
        }

        /**
         * 設定 flowrulename 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setFLOWRULENAME(String value) {
            this.flowrulename = value;
        }

        /**
         * 取得 flowtype 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getFLOWTYPE() {
            return flowtype;
        }

        /**
         * 設定 flowtype 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setFLOWTYPE(String value) {
            this.flowtype = value;
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
        protected String id = "INITSO.QryFlowRule001";

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
