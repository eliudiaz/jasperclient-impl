/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.cracken.jasperclient.exceptions;

import ed.cracken.api.exceptions.BaseException;
import ed.cracken.api.exceptions.ComponentError;

/**
 *
 * @author 2703759290608
 */
public class ReportConfiguationException extends BaseException {

    public ReportConfiguationException() {
        super(new ComponentError(500, "report configuration fail", "no fue cargar la configuracion del reporte"), null);
    }

}
