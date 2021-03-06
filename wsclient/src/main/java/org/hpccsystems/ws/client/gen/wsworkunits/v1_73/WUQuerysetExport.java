/**
 * WUQuerysetExport.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.hpccsystems.ws.client.gen.wsworkunits.v1_73;

public class WUQuerysetExport  implements java.io.Serializable {
    private java.lang.String target;

    private java.lang.Boolean compress;

    private java.lang.Boolean activeOnly;

    private java.lang.Boolean protect;

    public WUQuerysetExport() {
    }

    public WUQuerysetExport(
           java.lang.String target,
           java.lang.Boolean compress,
           java.lang.Boolean activeOnly,
           java.lang.Boolean protect) {
           this.target = target;
           this.compress = compress;
           this.activeOnly = activeOnly;
           this.protect = protect;
    }


    /**
     * Gets the target value for this WUQuerysetExport.
     * 
     * @return target
     */
    public java.lang.String getTarget() {
        return target;
    }


    /**
     * Sets the target value for this WUQuerysetExport.
     * 
     * @param target
     */
    public void setTarget(java.lang.String target) {
        this.target = target;
    }


    /**
     * Gets the compress value for this WUQuerysetExport.
     * 
     * @return compress
     */
    public java.lang.Boolean getCompress() {
        return compress;
    }


    /**
     * Sets the compress value for this WUQuerysetExport.
     * 
     * @param compress
     */
    public void setCompress(java.lang.Boolean compress) {
        this.compress = compress;
    }


    /**
     * Gets the activeOnly value for this WUQuerysetExport.
     * 
     * @return activeOnly
     */
    public java.lang.Boolean getActiveOnly() {
        return activeOnly;
    }


    /**
     * Sets the activeOnly value for this WUQuerysetExport.
     * 
     * @param activeOnly
     */
    public void setActiveOnly(java.lang.Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }


    /**
     * Gets the protect value for this WUQuerysetExport.
     * 
     * @return protect
     */
    public java.lang.Boolean getProtect() {
        return protect;
    }


    /**
     * Sets the protect value for this WUQuerysetExport.
     * 
     * @param protect
     */
    public void setProtect(java.lang.Boolean protect) {
        this.protect = protect;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WUQuerysetExport)) return false;
        WUQuerysetExport other = (WUQuerysetExport) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.target==null && other.getTarget()==null) || 
             (this.target!=null &&
              this.target.equals(other.getTarget()))) &&
            ((this.compress==null && other.getCompress()==null) || 
             (this.compress!=null &&
              this.compress.equals(other.getCompress()))) &&
            ((this.activeOnly==null && other.getActiveOnly()==null) || 
             (this.activeOnly!=null &&
              this.activeOnly.equals(other.getActiveOnly()))) &&
            ((this.protect==null && other.getProtect()==null) || 
             (this.protect!=null &&
              this.protect.equals(other.getProtect())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getTarget() != null) {
            _hashCode += getTarget().hashCode();
        }
        if (getCompress() != null) {
            _hashCode += getCompress().hashCode();
        }
        if (getActiveOnly() != null) {
            _hashCode += getActiveOnly().hashCode();
        }
        if (getProtect() != null) {
            _hashCode += getProtect().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WUQuerysetExport.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:hpccsystems:ws:wsworkunits", ">WUQuerysetExport"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("target");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:hpccsystems:ws:wsworkunits", "Target"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("compress");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:hpccsystems:ws:wsworkunits", "Compress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activeOnly");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:hpccsystems:ws:wsworkunits", "ActiveOnly"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("protect");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:hpccsystems:ws:wsworkunits", "Protect"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
