/**
 * TpSetMachineStatusResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.hpccsystems.ws.client.gen.wstopology.v1_28;

public class TpSetMachineStatusResponse  implements java.io.Serializable {
    private org.hpccsystems.ws.client.gen.wstopology.v1_28.ArrayOfEspException exceptions;

    private java.lang.Boolean tpSetMachineStatusResult;

    public TpSetMachineStatusResponse() {
    }

    public TpSetMachineStatusResponse(
           org.hpccsystems.ws.client.gen.wstopology.v1_28.ArrayOfEspException exceptions,
           java.lang.Boolean tpSetMachineStatusResult) {
           this.exceptions = exceptions;
           this.tpSetMachineStatusResult = tpSetMachineStatusResult;
    }


    /**
     * Gets the exceptions value for this TpSetMachineStatusResponse.
     * 
     * @return exceptions
     */
    public org.hpccsystems.ws.client.gen.wstopology.v1_28.ArrayOfEspException getExceptions() {
        return exceptions;
    }


    /**
     * Sets the exceptions value for this TpSetMachineStatusResponse.
     * 
     * @param exceptions
     */
    public void setExceptions(org.hpccsystems.ws.client.gen.wstopology.v1_28.ArrayOfEspException exceptions) {
        this.exceptions = exceptions;
    }


    /**
     * Gets the tpSetMachineStatusResult value for this TpSetMachineStatusResponse.
     * 
     * @return tpSetMachineStatusResult
     */
    public java.lang.Boolean getTpSetMachineStatusResult() {
        return tpSetMachineStatusResult;
    }


    /**
     * Sets the tpSetMachineStatusResult value for this TpSetMachineStatusResponse.
     * 
     * @param tpSetMachineStatusResult
     */
    public void setTpSetMachineStatusResult(java.lang.Boolean tpSetMachineStatusResult) {
        this.tpSetMachineStatusResult = tpSetMachineStatusResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpSetMachineStatusResponse)) return false;
        TpSetMachineStatusResponse other = (TpSetMachineStatusResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.exceptions==null && other.getExceptions()==null) || 
             (this.exceptions!=null &&
              this.exceptions.equals(other.getExceptions()))) &&
            ((this.tpSetMachineStatusResult==null && other.getTpSetMachineStatusResult()==null) || 
             (this.tpSetMachineStatusResult!=null &&
              this.tpSetMachineStatusResult.equals(other.getTpSetMachineStatusResult())));
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
        if (getExceptions() != null) {
            _hashCode += getExceptions().hashCode();
        }
        if (getTpSetMachineStatusResult() != null) {
            _hashCode += getTpSetMachineStatusResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TpSetMachineStatusResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:hpccsystems:ws:wstopology", ">TpSetMachineStatusResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exceptions");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:hpccsystems:ws:wstopology", "Exceptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:hpccsystems:ws:wstopology", "ArrayOfEspException"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tpSetMachineStatusResult");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:hpccsystems:ws:wstopology", "TpSetMachineStatusResult"));
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
