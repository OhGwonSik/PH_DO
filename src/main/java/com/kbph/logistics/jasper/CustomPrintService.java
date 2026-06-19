package com.kbph.logistics.jasper;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.jfree.util.Log;
import org.springframework.stereotype.Service;

import com.kbph.logistics.configuration.error.PrintJobFailed;
import com.kbph.logistics.configuration.error.PrintLoadFailed;
import com.kbph.logistics.configuration.error.PrinterNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * @author : s.h.kim
 * @version : 1.0.0
 * @since : 2024-10-11
 * @note : CustomPrintService
 * ==================================================
 * DATE							AUTHOR            					NOTE
 * --------------------------------------------------------------------------------------
 * 2024-10-11					s.h.kim        						create service class
 * --------------------------------------------------------------------------------------
 * ==================================================
 */
@Service
@RequiredArgsConstructor
public class CustomPrintService {

	// 창고 조회 시 사용 가능 프린터 찾기
	public List<String> findPrinter() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        Log.info("===========Printer List=========" + printServices);
        List<String> myPrinters = new ArrayList<>();
        if (printServices.length > 0) {
        	for(PrintService printer : printServices) {
        		myPrinters.add(printer.getName());
        	}
        }
        return myPrinters;
	}

	// 인쇄
	public boolean operatePrinter(byte[] reportData, String mainPrinterName, String subPrinterName) {
		AttributeSet aset = new HashAttributeSet();
		PrintService printService = null;

		// 프린터 설정
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(MediaSizeName.ISO_A4); // 용지 크기 설정
		pras.add(new Copies(1)); // 복사본 수

		// 메인프린터 시도
		if(!mainPrinterName.equals("")) {
			aset.add(new PrinterName(mainPrinterName, null));
			// 메인프린터 연결 안되면 서브프린터로
			if(printService == null && !subPrinterName.equals("")) {
				aset = new HashAttributeSet();
				aset.add(new PrinterName(subPrinterName, null));
			}
		}
		// 메인 프린터가 등록 안되어 있으면 바로 서브 프린터로
		else if(!subPrinterName.equals("")) {
			aset = new HashAttributeSet();
			aset.add(new PrinterName(subPrinterName, null));
		}

		try {
			printService = PrintServiceLookup.lookupPrintServices(null, aset)[0];
			try {
				PDDocument pdf = PDDocument.load(reportData);
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintService(printService);
				job.setPageable(new PDFPageable(pdf));
				job.print(pras);
				pdf.close();
			} catch (IOException e) {
				throw new PrintLoadFailed(); // 출력 데이터 로드에 실패하였습니다.
			} catch (PrinterException e) {
				throw new PrintJobFailed(); // 인쇄 작업에 실패하였습니다.
			}
		}catch (ArrayIndexOutOfBoundsException e) {
			throw new PrinterNotFoundException(); // 등록한 프린터기가 오프라인 상태입니다. 연결을 확인해주세요.
		}
		return true;
	}
}
