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
public class InvalidReportOutException extends BaseException {

    public InvalidReportOutException() {
        super(new ComponentError(500, "report request fail", "solo se permite salidas tipo: pdf/xls"), null);
    }

}
