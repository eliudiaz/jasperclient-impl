/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.cracken.api;

import ed.cracken.jasperclient.JasperClient;
import ed.cracken.jasperclient.ReportParam;
import ed.cracken.jasperclient.exceptions.InvalidReportNameException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author 2703759290608
 */
@Path("/api/v1")
public class ReportsAPI {

    @Path("/reports/{name}/{out}")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_OCTET_STREAM, "application/pdf", "application/vnd.ms-excel"
    })
    public Response execute(@PathParam("name") String reportName, @PathParam("out") String out, @Context UriInfo uriInfo) {
        Optional.of(reportName).orElseThrow(InvalidReportNameException::new);
        
        out = out == null || out.isEmpty() ? "pdf" : out;
        
        List<ReportParam> params = resolveParameters(uriInfo.getQueryParameters());
        new JasperClient().getReportResult(reportName, out, params);
        return Response.ok().build();
    }

    /**
     * 
     * @param queryParams
     * @return 
     */
    private List<ReportParam> resolveParameters(MultivaluedMap<String, String> queryParams) {
        List<ReportParam> params = new LinkedList<>();
        queryParams.keySet().stream().forEach((k)
                -> params.add(new ReportParam(k, queryParams.getFirst(k)))
        );

        return params;
    }

}
