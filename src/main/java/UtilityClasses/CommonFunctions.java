package UtilityClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CommonFunctions
{
	//Function to get Sheet Data
	public XSSFSheet getInputSheetData(String sheetName) throws IOException
	{	
		FileInputStream inputFile = new FileInputStream("E:\\Study\\Practice\\EclecticIQ\\src\\main\\java\\InputData\\Data.xlsx");
		XSSFWorkbook inputBook = new XSSFWorkbook(inputFile);
		XSSFSheet inputSheet = inputBook.getSheet(sheetName);
		return inputSheet;	
	}
	
	//Function to Read cell Data
	public List<String> getRowData(int rowId, String sheetName) throws IOException
	{
		List<String> searchString = new ArrayList<>();
		XSSFSheet inputSheet1 = getInputSheetData(sheetName);
		XSSFRow row = inputSheet1.getRow(rowId);
		Iterator<Cell> cellIterator = row.cellIterator();
		int i=0;
		while(cellIterator.hasNext())
		{
			XSSFCell cell = (XSSFCell) cellIterator.next();
			searchString.add(i, cell.getStringCellValue());
			i++;
		}
		return searchString;
	}
	
	//function to get WebElement
	public WebElement getElement(WebDriver driver, String xpath)
	{
		WebElement element = null;
		element = driver.findElement(By.xpath(xpath));
		return element;
	}
}
