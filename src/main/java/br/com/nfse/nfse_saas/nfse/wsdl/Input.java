
package br.com.nfse.nfse_saas.nfse.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de input complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteÃºdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="input"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nfseCabecMsg" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="nfseDadosMsg" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "input", propOrder = {
    "nfseCabecMsg",
    "nfseDadosMsg"
})
public class Input {

    @XmlElement(required = true)
    protected String nfseCabecMsg;
    @XmlElement(required = true)
    protected String nfseDadosMsg;

    /**
     * ObtÃ©m o valor da propriedade nfseCabecMsg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNfseCabecMsg() {
        return nfseCabecMsg;
    }

    /**
     * Define o valor da propriedade nfseCabecMsg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNfseCabecMsg(String value) {
        this.nfseCabecMsg = value;
    }

    /**
     * ObtÃ©m o valor da propriedade nfseDadosMsg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNfseDadosMsg() {
        return nfseDadosMsg;
    }

    /**
     * Define o valor da propriedade nfseDadosMsg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNfseDadosMsg(String value) {
        this.nfseDadosMsg = value;
    }

}

