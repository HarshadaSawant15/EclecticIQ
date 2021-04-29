package PageElements;

public interface IStatisticsPage {
	
	//List os string variables for xpath of webElements
	
	public static final String imgCyberHeader = "//h1[text()='Cyber attack completely fake statistics']";
	public static final String tblData= "//div[@class='table-content']";
	public static final String tblHeader = "//div[@class='table-header']";
	public static final String txtFilter = "//input[@name='filter-input']";
	public static final String ddSort = "//select[@name='sort-select']";
	public static final String tblRow = "//div[@class='table-row']";
	public static final String colNumCases = "//div[contains(@class,'data-cases')]";
	
	
	

}
