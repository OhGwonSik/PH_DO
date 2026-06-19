package com.kbph.logistics.bm.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kbph.logistics.bm.domain.BmReportDTO;
import com.kbph.logistics.bm.mapper.BmReportMapper;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoReportDataException;
import com.kbph.logistics.configuration.error.ReportJobFailed;
import com.kbph.logistics.jasper.JasperService;
import com.kbph.logistics.md.domain.MdocmaDTO;
import com.kbph.logistics.md.mapper.DocumentMapper;
import com.kbph.logistics.sy.domain.UserVO;
import com.kbph.logistics.sy.mapper.SystemMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
/**
 * @author : s.h.kim
 * @version : 1.0.0
 * @since : 2024-09-19
 * @note : BmReportService
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-09-19					s.h.kim        						create service class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BmReportService {
	private final BmReportMapper bmReportMapper;
	private final JasperService jasperService;
	private final DocumentMapper documentMapper;
	private final SystemMapper systemMapper;

	// 지급내역서
	public ResponseEntity<byte[]> generatePaymentReport(List<BmReportDTO> reportList){
		List<BmReportDTO> reportData = null;
		JasperReport jasperReport = null;
		JRBeanCollectionDataSource jrBeanCollectionDataSource = null;
		Map<String, Object> parameters = new HashMap<>();
		List<JasperPrint> jasperPringList = new ArrayList<>();
		int weigsum = 0;
		int costsum = 0;
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		for(BmReportDTO report : reportList) {

			// 이송 지급내역서
			if(report.getDoctype().equals("910")) {
				reportData = bmReportMapper.selectTransferPayment(report, userData);

			}
			// 운송 지급내역서
			else if(report.getDoctype().equals("920")) {
				reportData = bmReportMapper.selectTransportPayment(report, userData);
			}

			if(CollectionUtils.isEmpty(reportData)) {
				throw new NoReportDataException();
			}
			// 공통 파라미터
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			parameters.put("printdate", dateFormat.format(new Date())); // 인쇄일
			MdocmaDTO mdocmaDTO = new MdocmaDTO();
			mdocmaDTO.setDoctype(report.getDoctype());
			parameters.put("doctynm", documentMapper.selectDoctypeSelectBox(mdocmaDTO, userData).get(0).getCombonm()); // 운행형태
			parameters.put("confmfr", report.getConfirmdatefrom()); // 정산일자 from
			parameters.put("confmto", report.getConfirmdateto()); // 정산일자 to

			for(BmReportDTO item : reportData) {
				//SPRTHI
				//지급(CPGUBUN=PAYMENT)
				//이송(BFHDKEY), 운송(BPHDKEY)
				weigsum += item.getTotalwg();
				costsum += item.getTotpaco();
				item.setDoccate("900");
				item.setDoctype(report.getDoctype());
				item.setProgrid(report.getType());
				item.setWarekey(report.getWarekey());
				item.setReissue(bmReportMapper.selectPaymentPrintCnt(item, userData)); // 재발행

				// 프린트 로그 저장
				item.setPrintsq(systemMapper.selectPrintsq(SecurityUtils.getSchema()));
				if(bmReportMapper.insertPaymentPrintLog(item, userData) == 0) {
					throw new InsertCheckedException();
				}
			}
			parameters.put("weigsum", weigsum); // 총 중량
			parameters.put("costsum", costsum); // 합계금액

			try {
				jasperReport = jasperService.getJasperReport(report.getType());
				jrBeanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);
				jasperPringList.add(jasperPrint);
				return jasperService.getJasperResonse(jasperPringList);
			} catch (JRException e) {
				log.info("error=>{}", e);
				throw new ReportJobFailed();
			}
		}
		return null;
	}

	// 청구내역서
	public ResponseEntity<byte[]> generateBillingReport(List<BmReportDTO> reportList){
		List<BmReportDTO> reportData = null;
		JasperReport jasperReport = null;
		JRBeanCollectionDataSource jrBeanCollectionDataSource = null;
		Map<String, Object> parameters = new HashMap<>();
		List<JasperPrint> jasperPringList = new ArrayList<>();
		int weigsum = 0;
		int costsum = 0;
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		for(BmReportDTO report : reportList) {

			// 이송 청구내역서
			if(report.getDoctype().equals("910")) {
				if(report.getBtrfcbt() != null) {			//청구처가 null이 아니라면 청구처 내역서 출력
					reportData = bmReportMapper.selectTransferCharge(report, userData);
				} else if(report.getSbillto() != null) {	//추가청구처가 null이 아니라면 추가청구처 내역서 출력
					reportData = bmReportMapper.selectTransferSurCharge(report, userData);
				}
			}
			// 운송 청구내역서
			else if(report.getDoctype().equals("920")) {
				if(report.getBtrfcbt() != null) {			//청구처가 null이 아니라면 청구처 내역서 출력
					reportData = bmReportMapper.selectTransportCharge(report, userData);
				} else if(report.getSbillto() != null) {	//추가청구처가 null이 아니라면 추가청구처 내역서 출력
					reportData = bmReportMapper.selectTransportSurCharge(report, userData);
				}
			}

			if(CollectionUtils.isEmpty(reportData)) {
				throw new NoReportDataException();
			}
			// 공통 파라미터
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			parameters.put("printdate", dateFormat.format(new Date())); // 인쇄일
			MdocmaDTO mdocmaDTO = new MdocmaDTO();
			mdocmaDTO.setDoctype(report.getDoctype());
			parameters.put("doctynm", documentMapper.selectDoctypeSelectBox(mdocmaDTO, userData).get(0).getCombonm()); // 운행형태
			parameters.put("confmfr", report.getConfirmdatefrom()); // 정산일자 from
			parameters.put("confmto", report.getConfirmdateto()); // 정산일자 to

			for(BmReportDTO item : reportData) {
				// CPGUBUN(청구=CHARGE)
				// if(report.get	() != null) true => SPRTHI.CBILLTO
				// false => SPRTHI.SBILLTO
				//이송(BFHDKEY), 운송(BPHDKEY)
				weigsum += item.getTotalwg();
				costsum += report.getBtrfcbt() != null ? item.getTotchco() : item.getTosurch();
				item.setDoccate("900");
				item.setDoctype(report.getDoctype());
				item.setProgrid(report.getType());
				item.setWarekey(report.getWarekey());
				item.setReissue(bmReportMapper.selectChargePrintCnt(item, userData)); // 재발행

				// 프린트 로그 저장
				item.setPrintsq(systemMapper.selectPrintsq(SecurityUtils.getSchema()));
				if(bmReportMapper.insertChargePrintLog(item, userData) == 0) {
					throw new InsertCheckedException();
				}
			}
			parameters.put("weigsum", weigsum); // 총 중량
			parameters.put("costsum", costsum); // 합계금액

			try {
				jasperReport = jasperService.getJasperReport(report.getType());
				jrBeanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);
				jasperPringList.add(jasperPrint);
				return jasperService.getJasperResonse(jasperPringList);
			} catch (JRException e) {
				log.info("error=>{}", e);
				throw new ReportJobFailed();
			}
		}
		return null;
	}
}
