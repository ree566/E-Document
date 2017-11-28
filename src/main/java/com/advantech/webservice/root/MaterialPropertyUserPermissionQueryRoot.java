//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2017.11.27 於 08:55:46 AM CST 
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
 *         &lt;element name="MAT_PROPERTY_USER">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="USER_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "matpropertyuser"
})
@XmlRootElement(name = "root")
public class MaterialPropertyUserPermissionQueryRoot {

    @XmlElement(name = "METHOD", required = true)
    protected MaterialPropertyUserPermissionQueryRoot.METHOD method;
    @XmlElement(name = "MAT_PROPERTY_USER", required = true)
    protected MaterialPropertyUserPermissionQueryRoot.MATPROPERTYUSER matpropertyuser;

    public MaterialPropertyUserPermissionQueryRoot() {
        this.method = new MaterialPropertyUserPermissionQueryRoot.METHOD();
        this.matpropertyuser = new MaterialPropertyUserPermissionQueryRoot.MATPROPERTYUSER();
    }

    /**
     * 取得 method 特性的值.
     *
     * @return possible object is {@link Root.METHOD }
     *
     */
    public MaterialPropertyUserPermissionQueryRoot.METHOD getMETHOD() {
        return method;
    }

    /**
     * 設定 method 特性的值.
     *
     * @param value allowed object is {@link Root.METHOD }
     *
     */
    public void setMETHOD(MaterialPropertyUserPermissionQueryRoot.METHOD value) {
        this.method = value;
    }

    /**
     * 取得 matpropertyuser 特性的值.
     *
     * @return possible object is {@link Root.MATPROPERTYUSER }
     *
     */
    public MaterialPropertyUserPermissionQueryRoot.MATPROPERTYUSER getMATPROPERTYUSER() {
        return matpropertyuser;
    }

    /**
     * 設定 matpropertyuser 特性的值.
     *
     * @param value allowed object is {@link Root.MATPROPERTYUSER }
     *
     */
    public void setMATPROPERTYUSER(MaterialPropertyUserPermissionQueryRoot.MATPROPERTYUSER value) {
        this.matpropertyuser = value;
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
     *         &lt;element name="USER_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "userno",
        "matpropertyno"
    })
    public static class MATPROPERTYUSER {

        @XmlElement(name = "USER_NO", required = true, nillable = true)
        protected String userno;
        @XmlElement(name = "MAT_PROPERTY_NO", required = true, nillable = true)
        protected String matpropertyno;

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
        protected String id = "MIMSO.QryMatPropertyUser002";

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
