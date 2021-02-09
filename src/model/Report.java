package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Report {
	List<Rendering> rendering = new ArrayList<Rendering>();
	Sumarry sumarry = new Sumarry();
	
	public List<Rendering> getRendering() {
		return rendering;
	}

	@XmlElement
	public void setRendering(List<Rendering> rendering) {
		this.rendering = rendering;
	}

	public Sumarry getSumarry() {
		return sumarry;
	}
	
	@XmlElement
	public void setSumarry(Sumarry sumarry) {
		this.sumarry = sumarry;
	}

	public static class Rendering{
		private Long documentID;
		private int page;
		private String documentUID;
		private List<Date> startDatesList = new ArrayList<Date>();
		private List<Date> startGetList = new ArrayList<Date>();
		
		public Long getDocumentID() {
			return documentID;
		}
		
		@XmlElement(name = "document")
		public void setDocumentID(Long documentID) {
			this.documentID = documentID;
		}
		
		public int getPage() {
			return page;
		}
		
		@XmlElement
		public void setPage(int page) {
			this.page = page;
		}
		
		public String getDocumentUID() {
			return documentUID;
		}
		
		@XmlElement(name = "uid")
		public void setDocumentUID(String documentUID) {
			this.documentUID = documentUID;
		}
		
		public List<Date> getStartDatesList() {
			return startDatesList;
		}
		
		@XmlElement(name = "start")
		public void setStartDatesList(List<Date> startDatesList) {
			this.startDatesList = startDatesList;
		}
		
		public List<Date> getStartGetList() {
			return startGetList;
		}
		
		@XmlElement(name = "get")
		public void setStartGetList(List<Date> startGetList) {
			this.startGetList = startGetList;
		}
		
	}
	
	public static class Sumarry{
		int count;
		int duplicates;
		int unnecessary;
		
		public int getCount() {
			return count;
		}
		
		@XmlElement
		public void setCount(int count) {
			this.count = count;
		}
		
		public int getDuplicates() {
			return duplicates;
		}
		
		@XmlElement
		public void setDuplicates(int duplicates) {
			this.duplicates = duplicates;
		}
		
		public int getUnnecessary() {
			return unnecessary;
		}
		
		@XmlElement
		public void setUnnecessary(int unnecessary) {
			this.unnecessary = unnecessary;
		}
	}
}
