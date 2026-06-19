package com.kbph.logistics.om.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.om.domain.OmReportDTO;
import com.kbph.logistics.om.service.OmReportService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
/**
 * @author : s.h.kim
 * @version : 1.0.0
 * @since : 2024-09-19
 * @note : OmReportController
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-09-19					s.h.kim        						create Controller class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@RestController
@RequiredArgsConstructor
public class OmReportController {
	private final OmReportService omReportService;

	// 출고지시서
	@PostMapping("/doe053/order/report/reportlist")
	public ResponseEntity<byte[]> generateOrderReport(@RequestBody List<OmReportDTO> reportList) throws JRException{
		return omReportService.generateOrderReport(reportList);
	}

	// 출고송장
	@PostMapping("/doe057/invoice/report/reportlist")
	public ResponseEntity<byte[]> generateInvoiceReport(@RequestBody List<OmReportDTO> reportList) throws JRException{
		return omReportService.generateInvoiceReport(reportList);
	}
}
