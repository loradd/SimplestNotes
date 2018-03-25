/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.f4i.iw.simplestnote.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.simplestnote.data.impl.SimplestNoteDataLayerMysqlImpl;
import it.univaq.f4i.iw.simplestnote.data.model.SimplestNoteDataLayer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author lorenzoaddazi
 */
public abstract class SimpleNoteBaseController extends HttpServlet {
    
    public SimplestNoteDataLayer dataLayer;
    
    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DataLayerException;
    
    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response){
        try {
            dataLayer = new SimplestNoteDataLayerMysqlImpl((DataSource) getServletContext().getAttribute("datasource"));
            dataLayer.init();
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace(System.out); // only debug
            (new FailureResult(getServletContext())).activate("Ooops!!! An Unexpected Error Occurred...", request, response);
            //(new FailureResult(getServletContext())).activate((ex.getMessage() != null || ex.getCause() == null) ? ex.getMessage() : ex.getCause().getMessage(), request, response);
        } finally {
            try {
                dataLayer.destroy();
            } catch (DataLayerException ex) {
                Logger.getLogger(SimpleNoteBaseController.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
    }
    
    public SimplestNoteDataLayer getDataLayer(){
        return dataLayer;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

}
