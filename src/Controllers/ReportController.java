package Controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Report;

public class ReportController {
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

	/*
	 * Method responsible for reading all the log lines and returning a list of Rendering objects
	 */
	public List<Report.Rendering> createRenderingFromInputLines(List<String> inputLines){
		Map<String, String> documentIdUidMap = new HashMap<String, String>();
		Map<String, List<Date>> uidStartDatesMap = new HashMap<String, List<Date>>();
		Map<String, List<Date>> uidGetDatesMap = new HashMap<String, List<Date>>();
		
		for(int i = 0; i < inputLines.size(); i++) {
			String logLine = inputLines.get(i);
			Date logDate;
			try {
				logDate = getDateFromLogLine(logLine);
			} catch (Exception e) {
				// The error lines won't have a date, ignore them
				continue;
			}
			
			if(logLine.contains("Executing request startRendering")) {
				String documentIdString = logLine.substring(logLine.lastIndexOf("Executing request startRendering") + ("Executing request startRendering").length());
				documentIdString = documentIdString.substring(documentIdString.indexOf("[") + 1);
				documentIdString = documentIdString.substring(0, documentIdString.indexOf("]"));
				
				// Get the unique id after two lines. 
				// The line after can be a verification for existing processes and therefore give the correct UID for that document-page
				
				boolean uidFound = false;
				String uniqueID = "";
				while(!uidFound) {
					logLine = inputLines.get(++i);
					if(logLine.contains("Service startRendering returned")) {
						uniqueID = logLine.substring(logLine.lastIndexOf("Service startRendering returned") + ("Service startRendering returned").length()+1);
						uidFound = true;
					}
				}
				
				documentIdUidMap.put(documentIdString,uniqueID);
				if(uidStartDatesMap.get(uniqueID) ==  null) {
					List<Date> startDateList = new ArrayList<Date>();
					uidStartDatesMap.put(uniqueID, startDateList);
				}
				uidStartDatesMap.get(uniqueID).add(logDate);
				
			}else if(logLine.contains("Executing request getRendering")) {
				String documentUIdString = logLine.substring(logLine.lastIndexOf("Executing request getRendering") + ("Executing request getRendering").length());
				documentUIdString = documentUIdString.substring(documentUIdString.indexOf("[") + 1);
				documentUIdString = documentUIdString.substring(0, documentUIdString.indexOf("]"));
				if(uidGetDatesMap.get(documentUIdString) ==  null) {
					List<Date> getDateList = new ArrayList<Date>();
					uidGetDatesMap.put(documentUIdString, getDateList);
				}
				uidGetDatesMap.get(documentUIdString).add(logDate);
			}
		}
		
		return getRenderingListFromMaps(documentIdUidMap, uidStartDatesMap, uidGetDatesMap);
	}
	
	public Date getDateFromLogLine(String line) throws ParseException {
		return format.parse(line.substring(0,24));
	}
	
	public List<Report.Rendering> getRenderingListFromMaps(Map<String, String> documentIdUidMap, Map<String, List<Date>> uidStartDateMap, Map<String, List<Date>> uidGetDatesMap){
		List<Report.Rendering> returnList = new ArrayList<Report.Rendering>();
		for(Map.Entry<String, String> entry : documentIdUidMap.entrySet()) {
			Report.Rendering rendering = new Report.Rendering();
			String[] documentIDPageArray = entry.getKey().split(", ");
			String documentUID = entry.getValue();
			rendering.setDocumentID(Long.parseLong(documentIDPageArray[0]));
			rendering.setPage(Integer.parseInt(documentIDPageArray[1]));
			rendering.setDocumentUID(documentUID);
			
			for(Date startDate : uidStartDateMap.get(documentUID)) {
				rendering.getStartDatesList().add(startDate);
			}
			
			if(uidGetDatesMap.get(documentUID) != null) {
				for(Date getDate : uidGetDatesMap.get(documentUID)) {
					rendering.getStartGetList().add(getDate);
				}
			}
			
			returnList.add(rendering);
		}
		return returnList;
	}
	
	public Report.Sumarry createSumarryFromRenderingList(List<Report.Rendering> renderingList){
		Report.Sumarry sumarryReturn = new Report.Sumarry();
		int duplicates = 0;
		int unnecessary = 0;
		
		for(Report.Rendering rendering : renderingList) {
			if(rendering.getStartDatesList().size() > 1) {
				duplicates++;
			}
			if(rendering.getStartGetList().size() == 0 ) {
				unnecessary++;
			}
		}
		
		sumarryReturn.setCount(renderingList.size());
		sumarryReturn.setDuplicates(duplicates);
		sumarryReturn.setUnnecessary(unnecessary);
		
		return sumarryReturn;
	}
	
	public Report createReportFile(List<String> inputLines){
		Report report = new Report();
		
		List<Report.Rendering> renderingList = createRenderingFromInputLines(inputLines);
		report.setRendering(renderingList);
		report.setSumarry(createSumarryFromRenderingList(renderingList));
		
		return report;
	}
}
