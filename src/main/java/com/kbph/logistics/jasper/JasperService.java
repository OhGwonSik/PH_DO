package com.kbph.logistics.jasper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
@Slf4j
@RequiredArgsConstructor
public class JasperService {
	private final JasperConfig jasperConfig;

	@Bean
	HttpHeaders httpHeaders() {
		HttpHeaders httpHeader = new HttpHeaders();
		for(int i=0;i<jasperConfig.getHeaderNames().size();i++) {
			httpHeader.add(jasperConfig.getHeaderNames().get(i), jasperConfig.getHeaderValues().get(i));
		}
		log.info("{}",jasperConfig.getHeaderValues());
		return httpHeader;
	}

	public ResponseEntity<byte[]> getJasperResonse(List<JasperPrint> jasperPrints) throws JRException {
		JRPdfExporter exporter = new JRPdfExporter();
		ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfOutputStream));
		exporter.exportReport();
		return getJasperResonse(pdfOutputStream);
	}

	private ResponseEntity<byte[]> getJasperResonse(ByteArrayOutputStream pdfOutputStream ){
		HttpHeaders httpHeader = httpHeaders();
		return ResponseEntity.ok().headers(httpHeader).contentType(MediaType.APPLICATION_PDF).body(pdfOutputStream.toByteArray());
	}

	public JasperReport getJasperReport(String fileName) throws JRException {
		InputStream is=this.getClass().getResourceAsStream("/static/jasper/report/" + fileName + ".jrxml");
		StringBuilder sb = new StringBuilder();
		sb.append("src" + File.separator + "main" + File.separator + "resources" +File.separator + "static" + File.separator + "jasper" + File.separator + "report" + File.separator)
		.append(fileName)
		.append(jasperConfig.getExtName());
		return JasperCompileManager.compileReport(is);
	}
}
