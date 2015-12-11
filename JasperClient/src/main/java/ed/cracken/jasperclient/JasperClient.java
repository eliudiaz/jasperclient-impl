/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.cracken.jasperclient;

import com.jaspersoft.jasperserver.dto.reports.ReportParameter;
import com.jaspersoft.jasperserver.dto.reports.ReportParameters;
import com.jaspersoft.jasperserver.jaxrs.client.core.JasperserverRestClient;
import com.jaspersoft.jasperserver.jaxrs.client.core.RestClientConfiguration;
import com.jaspersoft.jasperserver.jaxrs.client.core.Session;
import com.jaspersoft.jasperserver.jaxrs.client.core.operationresult.OperationResult;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.ReportExecutionDescriptor;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.ReportExecutionRequest;
import ed.cracken.jasperclient.exceptions.InvalidReportOutException;
import ed.cracken.jasperclient.exceptions.ReportConfiguationException;
import ed.cracken.jasperclient.exceptions.ReportExecutionException;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2703759290608
 */
public class JasperClient {

    private static final RestClientConfiguration configuration;

    static {
        configuration = RestClientConfiguration.loadConfiguration("configuration.properties");
    }

    public OutputStream getReportResult(String reportPath, String output, List<ReportParam> params) {
        List<ReportParameter> lsParams = new LinkedList<>();
        ReportParameters rpps = new ReportParameters();
        params.stream().forEach((param) -> {
            ReportParameter prr = new ReportParameter();
            prr.setName(param.getName());
            prr.setValues(Arrays.asList(param.getValue()));
        });
        rpps.setReportParameters(lsParams);
        return execReport(reportPath, output, rpps);
    }

    /**
     *
     * @param reportPath
     * @param output
     * @param params
     * @return
     */
    private OutputStream execReport(String reportPath, String output, ReportParameters params) {
        Optional.of(configuration).orElseThrow(ReportConfiguationException::new);

        if (!output.contains("pdf") || !output.contains("xls")) {
            throw new InvalidReportOutException();
        }

        ReportExecutionRequest request = new ReportExecutionRequest();
        request.setReportUnitUri(reportPath);
        request
                .setAsync(false)
                .setOutputFormat(output)
                .setParameters(params);

        JasperserverRestClient client = new JasperserverRestClient(configuration);
        Session session = client.authenticate("jasperadmin", "jasperadmin");

        OperationResult<ReportExecutionDescriptor> executionRequest = null;
        while (executionRequest == null || !executionRequest.getEntity().getStatus().equalsIgnoreCase("ready")) {
            executionRequest = session
                    .reportingService()
                    .newReportExecutionRequest(request);
        }

        ReportExecutionDescriptor reportExecutionDescriptor = executionRequest.getEntity();
        OperationResult<InputStream> operationResult2
                = session //pay attention to this, all requests are in the same session!!!
                .reportingService()
                .reportExecutionRequest(reportExecutionDescriptor.getRequestId())
                .export(executionRequest.entity().getExports().get(0).getId())
                .outputResource();
        return getPayLoad(operationResult2);

    }

    private OutputStream getPayLoad(OperationResult<InputStream> result) throws ReportExecutionException {
        try {
            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            try (InputStream is = result.getEntity()) {
                int inByte;
                while ((inByte = is.read()) != -1) {
                    fos.write(inByte);
                }
            }
            return fos;
        } catch (IOException ex) {
            throw new ReportExecutionException(ex.getMessage());
        }
    }

    public static void main(String... arg) {
        try {
            OutputStream r = new JasperClient().execReport("/Renap/cargoempleado", "pdf", null);
            FileOutputStream fos = new FileOutputStream("prueba4.pdf");
            ((ByteArrayOutputStream) r).writeTo(fos);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JasperClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JasperClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ReportExecutionException | ReportConfiguationException ex) {
            Logger.getLogger(JasperClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
