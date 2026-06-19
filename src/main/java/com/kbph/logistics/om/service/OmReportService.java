package com.kbph.logistics.om.service;

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

import com.kbph.logistics.common.enums.Useyn;
import com.kbph.logistics.common.util.SecurityUtils;
import com.kbph.logistics.configuration.error.InsertCheckedException;
import com.kbph.logistics.configuration.error.NoReportDataException;
import com.kbph.logistics.configuration.error.ReportJobFailed;
import com.kbph.logistics.jasper.CustomPrintService;
import com.kbph.logistics.jasper.JasperService;
import com.kbph.logistics.md.mapper.OrganizationMapper;
import com.kbph.logistics.om.domain.OmReportDTO;
import com.kbph.logistics.om.mapper.OmReportMapper;
import com.kbph.logistics.om.type.InvoiceMode;
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
 * @note : OmReportService
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
public class OmReportService {
	private final OrganizationMapper organizationMapper;
	private final OmReportMapper omReportMapper;
	private final SystemMapper systemMapper;
	private final JasperService jasperService;
	private final CustomPrintService customPrintService;

	// doe049 출고지시서(doe053 - 출고대상 확정)
	public ResponseEntity<byte[]> generateOrderReport(List<OmReportDTO> reportList){
		List<OmReportDTO> reportData = null;
		JasperReport jasperReport = null;
		JRBeanCollectionDataSource jrBeanCollectionDataSource = null;
		Map<String, Object> parameters = new HashMap<>();
		List<JasperPrint> jasperPrintList = new ArrayList<>();
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		if(organizationMapper.selectShipOrderYn(reportList.get(0).getWarekey(), userData).equals(Useyn.USE.getString())) {
			try {
				for(OmReportDTO report : reportList) {
					reportData = omReportMapper.selectShippingOrder(report, userData);
					OmReportDTO params = omReportMapper.selectShippingOrderParam(report, userData);
					if(CollectionUtils.isEmpty(reportData)) {
						throw new NoReportDataException("출고지시서 출력 오류 - 데이터 없음");
					}

					// 프린트 기록 확인
//					parameters.put("reissue", omReportMapper.selectOmPrintCnt(report, userData)); // 재발행
					parameters.put("quancnt", params.getQuancnt()); // 총 수량
					parameters.put("weigsum", params.getWeigsum()); // 총 중량
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					parameters.put("printdate", dateFormat.format(new Date())); // 인쇄일

					// 프린트 로그 저장
					report.setPrintsq(systemMapper.selectPrintsq(SecurityUtils.getSchema()));
					if(omReportMapper.insertOmPrintLog(report, userData) == 0) {
						throw new InsertCheckedException("프린트 로그 저장 오류");
					}

						jasperReport = jasperService.getJasperReport(report.getType());
						jrBeanCollectionDataSource = new JRBeanCollectionDataSource(reportData);

						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);
						jasperPrintList.add(jasperPrint);
				}
				return jasperService.getJasperResonse(jasperPrintList);
			} catch (JRException e) {
				log.info("error=>{}", e);
				throw new ReportJobFailed("출고지시서 발행에 실패하였습니다.");
			}
		}
		return null;
	}

	// 출고송장
//	public ResponseEntity<byte[]> generateInvoiceReport(List<OmReportDTO> reportList){
//		List<OmReportDTO> reportData = null;
//		JasperReport jasperReport = null;
//		JRBeanCollectionDataSource jrBeanCollectionDataSource = null;
//		Map<String, Object> parameters = new HashMap<>();
//		List<JasperPrint> jasperPrintList = new ArrayList<>();
//		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();
//
//		try {
//			for(OmReportDTO report : reportList) {
//				OmReportDTO params  = omReportMapper.selectShippingInvoiceHead(report, userData);
//				reportData = omReportMapper.selectShippingInvoiceItem(report, userData);
//
//				// 리포트 데이터가 있는지 확인
//				if(params == null) {
//					throw new NoReportDataException("출고송장 데이터 없음");
//				}
//
//				// 파라미터 설정
////				parameters.put("reissue", omReportMapper.selectOmPrintCnt(report, userData)); // 재발행
//				parameters.put("shpdcky", params.getShpdcky()); // 송장번호
//				parameters.put("itemcnt", params.getItemcnt()); // 총 수량
//				parameters.put("spehdat", params.getSpehdat()); // 출고일시
//				parameters.put("whnamlc", params.getWhnamlc()); // 창고명칭, 출고자, 검수자
//				parameters.put("skudesc", params.getSkudesc()); // 품명
//				parameters.put("skwgsum", params.getSkwgsum()); // 중량 N
//				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				parameters.put("printdate", dateFormat.format(new Date())); // 발행일시
//				parameters.put("renamlc", params.getRenamlc()); // 목적지
//				parameters.put("grwgsum", params.getGrwgsum()); // 중량 G
//				parameters.put("vhcfnam", params.getVhcfnam()); // 운송차량
//				parameters.put("vonamlc", params.getVonamlc()); // 기사성명
//				parameters.put("cunamlc", params.getCunamlc()); // 판매계약자, 고객사, 최종고객사
//				parameters.put("demodnm", params.getDemodnm()); // 상세착지
//				parameters.put("warteln", params.getWarteln()); // 문의처
//
//				// 수요가증빙용 출력여부 확인
//				List<String> invoiceMode = new ArrayList<>();
//				invoiceMode.add(InvoiceMode.CUSTOMER.getString());
//				invoiceMode.add(InvoiceMode.RECEIPT.getString());
//				if(Useyn.USE.getString().equals(params.getProofyn())) {
//					invoiceMode.add(InvoiceMode.DEMAND.getString());
//				}
//
//				// 송장모드 리스트 size 만큼 반복 (2장 또는 3장)
//				for(int i=0; i<invoiceMode.size(); i++) {
//					parameters.put("invoiceMode", invoiceMode.get(i));
//					jasperReport = jasperService.getJasperReport(report.getType());
//					jrBeanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
//					parameters.put("reportData", reportData);
//					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);
//					jasperPrintList.add(jasperPrint);
//				}
//
//				// 메인프린터기 / 서브프린터기 둘 중 하나라도 있으면 custom으로. 아니면 그냥 인쇄 미리보기 화면으로 이동
////				PrinterDTO myPrinterDto = organizationMapper.selectWarehousePrinter(report.getWarekey(), userData);
////				if(((!myPrinterDto.getWmptrnm().equals("") && myPrinterDto.getWmptrnm() != null)
////					|| (!myPrinterDto.getWmptrnm().equals("") && myPrinterDto.getWsptrnm() != null))
////					&& customPrintService.operatePrinter(jasperService.getJasperResonse(jasperPrintList).getBody(), myPrinterDto.getWmptrnm(), myPrinterDto.getWsptrnm())) {
////					return null;
////				}
//
//				// 프린트 로그 저장
////				report.setPrintsq(systemMapper.selectPrintsq(SecurityUtils.getSchema()));
////				if(omReportMapper.insertOmPrintLog(report, userData) == 0) {
////					throw new InsertCheckedException("프린트 로그 저장 에러");
////				}
//			}
//			return !CollectionUtils.isEmpty(jasperPrintList) ? jasperService.getJasperResonse(jasperPrintList) : null;
//		} catch (JRException e) {
//			throw new ReportJobFailed("출고송장 발행에 실패하였습니다.");
//		}
//	}

	public ResponseEntity<byte[]> generateInvoiceReport(List<OmReportDTO> reportList){
		List<OmReportDTO> reportData = null;
		JasperReport jasperReport = null;
		JRBeanCollectionDataSource jrBeanCollectionDataSource = null;
		Map<String, Object> parameters = new HashMap<>();
		List<JasperPrint> jasperPrintList = new ArrayList<>();
		UserVO userData = SecurityUtils.getCustomUserDetails().getUserInfo();

		try {
			for(OmReportDTO report : reportList) {
				List<OmReportDTO> paramList  = omReportMapper.selectShippingInvoiceHeadList(report, userData);
				for(OmReportDTO params : paramList) {
					reportData = omReportMapper.selectShippingInvoiceItem(params, userData);

					// 리포트 데이터가 있는지 확인
					if(params == null) {
						throw new NoReportDataException("출고송장 데이터 없음");
					}

					// 파라미터 설정
	//				parameters.put("reissue", omReportMapper.selectOmPrintCnt(report, userData)); // 재발행
					parameters.put("shpdcky", params.getShpdcky()); // 송장번호
					parameters.put("itemcnt", params.getItemcnt()); // 총 수량
					parameters.put("spehdat", params.getSpehdat()); // 출고일시
					parameters.put("whnamlc", params.getWhnamlc()); // 창고명칭, 출고자, 검수자
					parameters.put("skudesc", params.getSkudesc()); // 품명
					parameters.put("skwgsum", params.getSkwgsum()); // 중량 N
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					parameters.put("printdate", dateFormat.format(new Date())); // 발행일시
					parameters.put("renamlc", params.getRenamlc()); // 목적지
					parameters.put("grwgsum", params.getGrwgsum()); // 중량 G
					parameters.put("vhcfnam", params.getVhcfnam()); // 운송차량
					parameters.put("vonamlc", params.getVonamlc()); // 기사성명
					parameters.put("ownrnam", params.getOwnrnam()); // 판매계약사
					parameters.put("cunamlc", params.getCunamlc()); // 고객사
					parameters.put("fncusky", params.getFncusky()); // 최종고객사
					parameters.put("demodnm", params.getDemodnm()); // 상세착지
					parameters.put("warteln", params.getWarteln()); // 문의처

					// 수요가증빙용 출력여부 확인
					List<String> invoiceMode = new ArrayList<>();
					invoiceMode.add(InvoiceMode.CUSTOMER.getString());
					invoiceMode.add(InvoiceMode.RECEIPT.getString());
					if(Useyn.USE.getString().equals(params.getProofyn())) {
						invoiceMode.add(InvoiceMode.DEMAND.getString());
					}

					// 송장모드 리스트 size 만큼 반복 (2장 또는 3장)
					for(int i=0; i<invoiceMode.size(); i++) {
						parameters.put("invoiceMode", invoiceMode.get(i));
						jasperReport = jasperService.getJasperReport(report.getType());
						jrBeanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
						parameters.put("reportData", reportData);
						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);
						jasperPrintList.add(jasperPrint);
					}

					// 메인프린터기 / 서브프린터기 둘 중 하나라도 있으면 custom으로. 아니면 그냥 인쇄 미리보기 화면으로 이동
	//				PrinterDTO myPrinterDto = organizationMapper.selectWarehousePrinter(report.getWarekey(), userData);
	//				if(((!myPrinterDto.getWmptrnm().equals("") && myPrinterDto.getWmptrnm() != null)
	//					|| (!myPrinterDto.getWmptrnm().equals("") && myPrinterDto.getWsptrnm() != null))
	//					&& customPrintService.operatePrinter(jasperService.getJasperResonse(jasperPrintList).getBody(), myPrinterDto.getWmptrnm(), myPrinterDto.getWsptrnm())) {
	//					return null;
	//				}

					// 프린트 로그 저장
	//				report.setPrintsq(systemMapper.selectPrintsq(SecurityUtils.getSchema()));
	//				if(omReportMapper.insertOmPrintLog(report, userData) == 0) {
	//					throw new InsertCheckedException("프린트 로그 저장 에러");
	//				}
				}
			}
			return !CollectionUtils.isEmpty(jasperPrintList) ? jasperService.getJasperResonse(jasperPrintList) : null;
		} catch (JRException e) {
			throw new ReportJobFailed("출고송장 발행에 실패하였습니다.");
		}
	}
}
