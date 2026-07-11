
package br.com.nfse.nfse_saas.nfse.wsdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.com.seu.erp.nfse.wsdl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CancelarNfse_QNAME = new QName("http://nfse.abrasf.org.br", "CancelarNfse");
    private final static QName _CancelarNfseResponse_QNAME = new QName("http://nfse.abrasf.org.br", "CancelarNfseResponse");
    private final static QName _ConsultarLoteRps_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarLoteRps");
    private final static QName _ConsultarLoteRpsResponse_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarLoteRpsResponse");
    private final static QName _ConsultarNfseServicoPrestado_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarNfseServicoPrestado");
    private final static QName _ConsultarNfseServicoPrestadoResponse_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarNfseServicoPrestadoResponse");
    private final static QName _ConsultarNfseServicoTomado_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarNfseServicoTomado");
    private final static QName _ConsultarNfseServicoTomadoResponse_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarNfseServicoTomadoResponse");
    private final static QName _ConsultarNfsePorFaixa_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarNfsePorFaixa");
    private final static QName _ConsultarNfsePorFaixaResponse_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarNfsePorFaixaResponse");
    private final static QName _ConsultarNfsePorRps_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarNfsePorRps");
    private final static QName _ConsultarNfsePorRpsResponse_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarNfsePorRpsResponse");
    private final static QName _RecepcionarLoteRps_QNAME = new QName("http://nfse.abrasf.org.br", "RecepcionarLoteRps");
    private final static QName _RecepcionarLoteRpsResponse_QNAME = new QName("http://nfse.abrasf.org.br", "RecepcionarLoteRpsResponse");
    private final static QName _GerarNfse_QNAME = new QName("http://nfse.abrasf.org.br", "GerarNfse");
    private final static QName _GerarNfseResponse_QNAME = new QName("http://nfse.abrasf.org.br", "GerarNfseResponse");
    private final static QName _SubstituirNfse_QNAME = new QName("http://nfse.abrasf.org.br", "SubstituirNfse");
    private final static QName _SubstituirNfseResponse_QNAME = new QName("http://nfse.abrasf.org.br", "SubstituirNfseResponse");
    private final static QName _RecepcionarLoteRpsSincrono_QNAME = new QName("http://nfse.abrasf.org.br", "RecepcionarLoteRpsSincrono");
    private final static QName _RecepcionarLoteRpsSincronoResponse_QNAME = new QName("http://nfse.abrasf.org.br", "RecepcionarLoteRpsSincronoResponse");
    private final static QName _ConsultarUrlNfse_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarUrlNfse");
    private final static QName _ConsultarUrlNfseResponse_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarUrlNfseResponse");
    private final static QName _ConsultarDadosCadastrais_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarDadosCadastrais");
    private final static QName _ConsultarDadosCadastraisResponse_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarDadosCadastraisResponse");
    private final static QName _ConsultarRpsDisponivel_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarRpsDisponivel");
    private final static QName _ConsultarRpsDisponivelResponse_QNAME = new QName("http://nfse.abrasf.org.br", "ConsultarRpsDisponivelResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.com.seu.erp.nfse.wsdl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Input }
     * 
     */
    public Input createInput() {
        return new Input();
    }

    /**
     * Create an instance of {@link Output }
     * 
     */
    public Output createOutput() {
        return new Output();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "CancelarNfse")
    public JAXBElement<Input> createCancelarNfse(Input value) {
        return new JAXBElement<Input>(_CancelarNfse_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "CancelarNfseResponse")
    public JAXBElement<Output> createCancelarNfseResponse(Output value) {
        return new JAXBElement<Output>(_CancelarNfseResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarLoteRps")
    public JAXBElement<Input> createConsultarLoteRps(Input value) {
        return new JAXBElement<Input>(_ConsultarLoteRps_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarLoteRpsResponse")
    public JAXBElement<Output> createConsultarLoteRpsResponse(Output value) {
        return new JAXBElement<Output>(_ConsultarLoteRpsResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarNfseServicoPrestado")
    public JAXBElement<Input> createConsultarNfseServicoPrestado(Input value) {
        return new JAXBElement<Input>(_ConsultarNfseServicoPrestado_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarNfseServicoPrestadoResponse")
    public JAXBElement<Output> createConsultarNfseServicoPrestadoResponse(Output value) {
        return new JAXBElement<Output>(_ConsultarNfseServicoPrestadoResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarNfseServicoTomado")
    public JAXBElement<Input> createConsultarNfseServicoTomado(Input value) {
        return new JAXBElement<Input>(_ConsultarNfseServicoTomado_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarNfseServicoTomadoResponse")
    public JAXBElement<Output> createConsultarNfseServicoTomadoResponse(Output value) {
        return new JAXBElement<Output>(_ConsultarNfseServicoTomadoResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarNfsePorFaixa")
    public JAXBElement<Input> createConsultarNfsePorFaixa(Input value) {
        return new JAXBElement<Input>(_ConsultarNfsePorFaixa_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarNfsePorFaixaResponse")
    public JAXBElement<Output> createConsultarNfsePorFaixaResponse(Output value) {
        return new JAXBElement<Output>(_ConsultarNfsePorFaixaResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarNfsePorRps")
    public JAXBElement<Input> createConsultarNfsePorRps(Input value) {
        return new JAXBElement<Input>(_ConsultarNfsePorRps_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarNfsePorRpsResponse")
    public JAXBElement<Output> createConsultarNfsePorRpsResponse(Output value) {
        return new JAXBElement<Output>(_ConsultarNfsePorRpsResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "RecepcionarLoteRps")
    public JAXBElement<Input> createRecepcionarLoteRps(Input value) {
        return new JAXBElement<Input>(_RecepcionarLoteRps_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "RecepcionarLoteRpsResponse")
    public JAXBElement<Output> createRecepcionarLoteRpsResponse(Output value) {
        return new JAXBElement<Output>(_RecepcionarLoteRpsResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "GerarNfse")
    public JAXBElement<Input> createGerarNfse(Input value) {
        return new JAXBElement<Input>(_GerarNfse_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "GerarNfseResponse")
    public JAXBElement<Output> createGerarNfseResponse(Output value) {
        return new JAXBElement<Output>(_GerarNfseResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "SubstituirNfse")
    public JAXBElement<Input> createSubstituirNfse(Input value) {
        return new JAXBElement<Input>(_SubstituirNfse_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "SubstituirNfseResponse")
    public JAXBElement<Output> createSubstituirNfseResponse(Output value) {
        return new JAXBElement<Output>(_SubstituirNfseResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "RecepcionarLoteRpsSincrono")
    public JAXBElement<Input> createRecepcionarLoteRpsSincrono(Input value) {
        return new JAXBElement<Input>(_RecepcionarLoteRpsSincrono_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "RecepcionarLoteRpsSincronoResponse")
    public JAXBElement<Output> createRecepcionarLoteRpsSincronoResponse(Output value) {
        return new JAXBElement<Output>(_RecepcionarLoteRpsSincronoResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarUrlNfse")
    public JAXBElement<Input> createConsultarUrlNfse(Input value) {
        return new JAXBElement<Input>(_ConsultarUrlNfse_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarUrlNfseResponse")
    public JAXBElement<Output> createConsultarUrlNfseResponse(Output value) {
        return new JAXBElement<Output>(_ConsultarUrlNfseResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarDadosCadastrais")
    public JAXBElement<Input> createConsultarDadosCadastrais(Input value) {
        return new JAXBElement<Input>(_ConsultarDadosCadastrais_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarDadosCadastraisResponse")
    public JAXBElement<Output> createConsultarDadosCadastraisResponse(Output value) {
        return new JAXBElement<Output>(_ConsultarDadosCadastraisResponse_QNAME, Output.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Input }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarRpsDisponivel")
    public JAXBElement<Input> createConsultarRpsDisponivel(Input value) {
        return new JAXBElement<Input>(_ConsultarRpsDisponivel_QNAME, Input.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Output }{@code >}
     */
    @XmlElementDecl(namespace = "http://nfse.abrasf.org.br", name = "ConsultarRpsDisponivelResponse")
    public JAXBElement<Output> createConsultarRpsDisponivelResponse(Output value) {
        return new JAXBElement<Output>(_ConsultarRpsDisponivelResponse_QNAME, Output.class, null, value);
    }

}

