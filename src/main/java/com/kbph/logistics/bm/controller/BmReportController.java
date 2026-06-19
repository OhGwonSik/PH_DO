package com.kbph.logistics.bm.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.bm.domain.BmReportDTO;
import com.kbph.logistics.bm.service.BmReportService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
/**
 * @author : s.h.kim
 * @version : 1.0.0
 * @since : 2024-09-19
 * @note : BmReportController
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-09-19					s.h.kim        						create Controller class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@RestController
@RequiredArgsConstructor
public class BmReportController {
	private final BmReportService bmReportService;

	// 지급내역서
	@PostMapping("/doe088/payment/report/reportlist")
	public ResponseEntity<byte[]> generatePaymentReport(@RequestBody List<BmReportDTO> reportList) throws JRException{
		return bmReportService.generatePaymentReport(reportList);
	}

	// 청구내역서
	@PostMapping("/doe088/billing/report/reportlist")
	public ResponseEntity<byte[]> generateDoe088Report(@RequestBody List<BmReportDTO> reportList) throws JRException{
		return bmReportService.generateBillingReport(reportList);
	}
}
