package com.tianji.dam.service;



import org.springframework.stereotype.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Service
public interface PdfExportService {
    public void make(Map<String, Object> model, Document document, PdfWriter writer,
                     HttpServletRequest request, HttpServletResponse response) throws IOException;
}
