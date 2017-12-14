//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2017.11.27 於 08:53:10 AM CST 
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
 *         &lt;element name="MAT_PROPERTY_VALUE">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ITEM_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="MAT_PROPERTY_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "matpropertyvalue"
})
@XmlRootElement(name = "root")
public class MaterialPropertyValueQueryRoot {

    @XmlElement(name = "METHOD", required = true)
    protected MaterialPropertyValueQueryRoot.METHOD method;
    @XmlElement(name = "MAT_PROPERTY_VALUE", required = true)
    protected MaterialPropertyValueQueryRoot.MATPROPERTYVALUE matpropertyvalue;

    public MaterialPropertyValueQueryRoot() {
        this.method = new MaterialPropertyValueQueryRoot.METHOD();
        this.matpropertyvalue = new MaterialPropertyValueQueryRoot.MATPROPERTYVALUE();
    }

    /**
     * 取得 method 特性的值.
     *
     * @return possible object is {@link Root.METHOD }
     *
     */
    public MaterialPropertyValueQueryRoot.METHOD getMETHOD() {
        return method;
    }

    /**
     * 設定 method 特性的值.
     *
     * @param value allowed object is {@link Root.METHOD }
     *
     */
    public void setMETHOD(MaterialPropertyValueQueryRoot.METHOD value) {
        this.method = value;
    }

    /**
     * 取得 matpropertyvalue 特性的值.
     *
     * @return possible object is {@link Root.MATPROPERTYVALUE }
     *
     */
    public MaterialPropertyValueQueryRoot.MATPROPERTYVALUE getMATPROPERTYVALUE() {
        return matpropertyvalue;
    }

    /**
     * 設定 matpropertyvalue 特性的值.
     *
     * @param value allowed object is {@link Root.MATPROPERTYVALUE }
     *
     */
    public void setMATPROPERTYVALUE(MaterialPropertyValueQueryRoot.MATPROPERTYVALUE value) {
        this.matpropertyvalue = value;
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
     *         &lt;element name="MAT_PROPERTY_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "matpropertyno"
    })
    public static class MATPROPERTYVALUE {

        @XmlElement(name = "ITEM_NO", required = true, nillable = true)
        protected String itemno;
        @XmlElement(name = "MAT_PROPERTY_NO", required = true, nillable = true)
        protected String matpropertyno;

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
        protected String id = "MIMSO.QryMatValue";

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
