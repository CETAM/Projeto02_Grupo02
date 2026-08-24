package cetam.projeto02grupo02.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfReportService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    public byte[] gerarPdf(String templatePath, Map<String, Object> dados) throws Exception {
        // Criar o contexto do Thymeleaf e preencher com os dados
        Context context = new Context();
        context.setVariables(dados);

        // Processar o template HTML com o Thymeleaf
        String html = templateEngine.process(templatePath, context);

        // Converter o HTML para PDF em memória usando OpenHTMLtoPDF
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            // A baseUri é opcional, mas útil se houvesse links relativos para imagens estáticas
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();

            return os.toByteArray();
        }
    }
}
