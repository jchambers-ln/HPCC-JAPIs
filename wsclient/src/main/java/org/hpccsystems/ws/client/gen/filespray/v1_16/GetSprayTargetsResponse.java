/**
 * GetSprayTargetsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.hpccsystems.ws.client.gen.filespray.v1_16;

public class GetSprayTargetsResponse  implements java.io.Serializable {
    private org.hpccsystems.ws.client.gen.filespray.v1_16.ArrayOfEspException exceptions;

    private org.hpccsystems.ws.client.gen.filespray.v1_16.GroupNode[] groupNodes;

    public GetSprayTargetsResponse() {
    }

    public GetSprayTargetsResponse(
           org.hpccsystems.ws.client.gen.filespray.v1_16.ArrayOfEspException exceptions,
           org.hpccsystems.ws.client.gen.filespray.v1_16.GroupNode[] groupNodes) {
           this.exceptions = exceptions;
           this.groupNodes = groupNodes;
    }


    /**
     * Gets the exceptions value for this GetSprayTargetsResponse.
     * 
     * @return exceptions
     */
    public org.hpccsystems.ws.client.gen.filespray.v1_16.ArrayOfEspException getExceptions() {
        return exceptions;
    }


    /**
     * Sets the exceptions value for this GetSprayTargetsResponse.
     * 
     * @param exceptions
     */
    public void setExceptions(org.hpccsystems.ws.client.gen.filespray.v1_16.ArrayOfEspException exceptions) {
        this.exceptions = exceptions;
    }


    /**
     * Gets the groupNodes value for this GetSprayTargetsResponse.
     * 
     * @return groupNodes
     */
    public org.hpccsystems.ws.client.gen.filespray.v1_16.GroupNode[] getGroupNodes() {
        return groupNodes;
    }


    /**
     * Sets the groupNodes value for this GetSprayTargetsResponse.
     * 
     * @param groupNodes
     */
    public void setGroupNodes(org.hpccsystems.ws.client.gen.filespray.v1_16.GroupNode[] groupNodes) {
        this.groupNodes = groupNodes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSprayTargetsResponse)) return false;
        GetSprayTargetsResponse other = (GetSprayTargetsResponse) obj;
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
            ((this.groupNodes==null && other.getGroupNodes()==null) || 
             (this.groupNodes!=null &&
              java.util.Arrays.equals(this.groupNodes, other.getGroupNodes())));
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
        if (getGroupNodes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGroupNodes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGroupNodes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSprayTargetsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:hpccsystems:ws:filespray", ">GetSprayTargetsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exceptions");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:hpccsystems:ws:filespray", "Exceptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:hpccsystems:ws:filespray", "ArrayOfEspException"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupNodes");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:hpccsystems:ws:filespray", "GroupNodes"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:hpccsystems:ws:filespray", "GroupNode"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("urn:hpccsystems:ws:filespray", "GroupNode"));
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
