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
public class InvalidReportNameException extends BaseException {

    public InvalidReportNameException() {
        super(new ComponentError(500, "report request fail", "invocacion invalida, debe indicar el nombre del reporte como minimo "), null);
    }

}
