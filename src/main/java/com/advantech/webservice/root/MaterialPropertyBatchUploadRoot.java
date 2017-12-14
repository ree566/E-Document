//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2017.11.27 於 03:57:05 PM CST 
//
package com.advantech.webservice.root;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="MAT_VALUE">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ITEM_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="MAT_VALUE_DETAIL" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="MAT_PROPERTY_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="VALUE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="AFF_VALUE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="AFF_PRO_TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="MEMO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
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
    "matvalue"
})
@XmlRootElement(name = "root")
public class MaterialPropertyBatchUploadRoot {

    @XmlElement(name = "METHOD", required = true)
    protected MaterialPropertyBatchUploadRoot.METHOD method;
    @XmlElement(name = "MAT_VALUE", required = true)
    protected MaterialPropertyBatchUploadRoot.MATVALUE matvalue;

    public MaterialPropertyBatchUploadRoot() {
        this.method = new MaterialPropertyBatchUploadRoot.METHOD();
        this.matvalue = new MaterialPropertyBatchUploadRoot.MATVALUE();
    }

    /**
     * 取得 method 特性的值.
     *
     * @return possible object is {@link Root.METHOD }
     *
     */
    public MaterialPropertyBatchUploadRoot.METHOD getMETHOD() {
        return method;
    }

    /**
     * 設定 method 特性的值.
     *
     * @param value allowed object is {@link Root.METHOD }
     *
     */
    public void setMETHOD(MaterialPropertyBatchUploadRoot.METHOD value) {
        this.method = value;
    }

    /**
     * 取得 matvalue 特性的值.
     *
     * @return possible object is {@link Root.MATVALUE }
     *
     */
    public MaterialPropertyBatchUploadRoot.MATVALUE getMATVALUE() {
        return matvalue;
    }

    /**
     * 設定 matvalue 特性的值.
     *
     * @param value allowed object is {@link Root.MATVALUE }
     *
     */
    public void setMATVALUE(MaterialPropertyBatchUploadRoot.MATVALUE value) {
        this.matvalue = value;
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
     *         &lt;element name="ITEM_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="MAT_VALUE_DETAIL" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="MAT_PROPERTY_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="VALUE" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="AFF_VALUE" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="AFF_PRO_TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="MEMO" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "itemno",
        "matvaluedetail"
    })
    public static class MATVALUE {

        @XmlElement(name = "ITEM_NO", required = true)
        protected String itemno;
        @XmlElement(name = "MAT_VALUE_DETAIL")
        protected List<MaterialPropertyBatchUploadRoot.MATVALUE.MATVALUEDETAIL> matvaluedetail;

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
         * Gets the value of the matvaluedetail property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the matvaluedetail property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMATVALUEDETAIL().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Root.MATVALUE.MATVALUEDETAIL }
         *
         *
         */
        public List<MaterialPropertyBatchUploadRoot.MATVALUE.MATVALUEDETAIL> getMATVALUEDETAIL() {
            if (matvaluedetail == null) {
                matvaluedetail = new ArrayList<MaterialPropertyBatchUploadRoot.MATVALUE.MATVALUEDETAIL>();
            }
            return this.matvaluedetail;
        }

        public void setMATVALUEDETAIL(List<MaterialPropertyBatchUploadRoot.MATVALUE.MATVALUEDETAIL> matvaluedetail) {
            this.matvaluedetail = matvaluedetail;
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
         *         &lt;element name="MAT_PROPERTY_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="VALUE" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="AFF_VALUE" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="AFF_PRO_TYPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="MEMO" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
            "matpropertyno",
            "value",
            "affvalue",
            "affprotype",
            "memo"
        })
        public static class MATVALUEDETAIL {

            @XmlElement(name = "MAT_PROPERTY_NO", required = true, nillable = true)
            protected String matpropertyno;
            @XmlElement(name = "VALUE", required = true, nillable = true)
            protected String value = "";
            @XmlElement(name = "AFF_VALUE", required = true, nillable = true)
            protected String affvalue = "";
            @XmlElement(name = "AFF_PRO_TYPE", required = true, nillable = true)
            protected String affprotype = "";
            @XmlElement(name = "MEMO", required = true, nillable = true)
            protected String memo = "";

            public MATVALUEDETAIL() {
            }

            /**
             * 取得 matpropertyno 特性的值.
             *
             * @return possible object is {@link String }
             *
             */
            public String getMATPROPERTYNO() {
                return matpropertyno;
            }

            /**
             * 設定 matpropertyno 特性的值.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setMATPROPERTYNO(String value) {
                this.matpropertyno = value;
            }

            /**
             * 取得 value 特性的值.
             *
             * @return possible object is {@link String }
             *
             */
            public String getVALUE() {
                return value;
            }

            /**
             * 設定 value 特性的值.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setVALUE(String value) {
                this.value = value;
            }

            /**
             * 取得 affvalue 特性的值.
             *
             * @return possible object is {@link String }
             *
             */
            public String getAFFVALUE() {
                return affvalue;
            }

            /**
             * 設定 affvalue 特性的值.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setAFFVALUE(String value) {
                this.affvalue = value;
            }

            /**
             * 取得 affprotype 特性的值.
             *
             * @return possible object is {@link String }
             *
             */
            public String getAFFPROTYPE() {
                return affprotype;
            }

            /**
             * 設定 affprotype 特性的值.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setAFFPROTYPE(String value) {
                this.affprotype = value;
            }

            /**
             * 取得 memo 特性的值.
             *
             * @return possible object is {@link String }
             *
             */
            public String getMEMO() {
                return memo;
            }

            /**
             * 設定 memo 特性的值.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setMEMO(String value) {
                this.memo = value;
            }

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
        protected String id = "MIMSO.TxMatValue002";

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
