
package br.com.nfse.nfse_saas.nfse.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de output complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteÃºdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="output"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="outputXML" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "output", propOrder = {
    "outputXML"
})
public class Output {

    @XmlElement(required = true)
    protected String outputXML;

    /**
     * ObtÃ©m o valor da propriedade outputXML.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutputXML() {
        return outputXML;
    }

    /**
     * Define o valor da propriedade outputXML.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutputXML(String value) {
        this.outputXML = value;
    }

}

