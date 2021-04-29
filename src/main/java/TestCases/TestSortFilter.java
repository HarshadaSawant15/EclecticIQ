package TestCases;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
//import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
//import java.util.Map;
//import org.openqa.selenium.WebDriver;
//import org.testng.annotations.Test;
import org.testng.annotations.Test;

//import PageClasses.HomePage;
import PageElements.IStatisticsPage;
import UtilityClasses.CommonFunctions;

public class TestSortFilter implements IStatisticsPage
{
	CommonFunctions cm = new CommonFunctions();
	String browserSheet = "BrowserSetting";
	String filterdataSheet = "FilterData";
	String sortingrdataSheet = "SortingData";
	WebDriver driver;
	WebDriverWait wait;
	boolean result;
	int count=0;
	
	
	//Setting WebDriver
	//@SuppressWarnings("deprecation")
	@BeforeTest
	public void setUp() throws IOException
	{
		List<String> browserStr = new ArrayList<String>();
		try
		{
			browserStr = cm.getRowData(1, browserSheet);
			if(browserStr.size()==2)
			{
				String browser = browserStr.get(0);
				String driverPath = browserStr.get(1);
				
				//Setting up browser based on configuration
				if(browser.equalsIgnoreCase("chrome"))
				{
					System.setProperty("webdriver.chrome.driver", driverPath);
					driver = new ChromeDriver();
					driver.get("https://mystifying-beaver-ee03b5.netlify.app/");
					driver.manage().window().maximize();
				}
				if(browser.equalsIgnoreCase("firefox"))
				{
					System.setProperty("webdriver.gecko.driver", driverPath);
					driver = new FirefoxDriver();
					driver.get("https://mystifying-beaver-ee03b5.netlify.app/");
				}
				if(browser.equalsIgnoreCase("IE"))
				{
					System.setProperty("webdriver.ie.driver", driverPath);
					driver = new InternetExplorerDriver();
					driver.get("https://mystifying-beaver-ee03b5.netlify.app/");
				}
				wait = new WebDriverWait(driver, 30);
			}
			else
			{
				System.out.println("Browser Configuration is not mentioned");
				result=false;
				Assert.assertTrue(result);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Function to check visibility for sorting control
	@Test(priority=1)
	public void verifyVisibiltyofSorting()
	{
		WebElement weddsort = cm.getElement(driver, ddSort);
		if(weddsort.isDisplayed()==true&&weddsort.isEnabled())
		{
			System.out.println("Sorting control is visible and enabled");
			result=true;
			Assert.assertTrue(result);
		}
		else
		{
			System.out.println("Sorting control is either disable/invisible");
			result=false;
			Assert.assertTrue(result);
		}
	}
	
	//Function to check visibility for sorting control
	@Test(priority=2)
	public void verifyVisibiltyofFilter()
	{
		WebElement wetxtFilter = cm.getElement(driver, txtFilter);
		if(wetxtFilter.isDisplayed()==true)
		{
			System.out.println("Filter control is visible and enabled");
			result=true;
			Assert.assertTrue(result);
		}
		else
		{
			System.out.println("Filter control is either disable/invisible");
			result=false;
			Assert.assertTrue(result);
		}
	}
	
	//function to check Filter Control with Valid Inputs
	@Test(priority=3)
	public void testFilterWithValidInput() throws IOException
	{
		System.out.println("====================Verifying Filter with Valid Input==============");
		List<String> searchStr = new ArrayList<String>();
		searchStr = cm.getRowData(1, filterdataSheet);
		String searchString = searchStr.get(1);

		try
		{
			WebElement weTxtFilter = cm.getElement(driver, txtFilter);
			WebElement weTblData = cm.getElement(driver, tblData);
			
			weTxtFilter.clear();
			weTxtFilter.sendKeys(searchString);
			wait.until(ExpectedConditions.visibilityOf(weTblData));
			List<WebElement> listData = driver.findElements(By.xpath(tblRow));
			if(listData.size()>0)
			{
				for(int i=0;i<listData.size();i++)
				{
					String actText = driver.findElement(By.xpath("//div[@class='table-row']["+(i+1)+"]/div[contains(@class,'data-name')]")).getText();
					if(actText.toLowerCase().contains(searchString.toLowerCase()))
					{
						count++;
						System.out.println("Record matched correctly");
					}
					else
					{
						System.out.println("Incorrect data filtered");
					}
				}
				if(count==listData.size())
				{
					System.out.println("Filter Control working fine");
					result=true;
					count=0;
					Assert.assertTrue(result);	
				}
				else
				{
					System.out.println("Records are mis matched hence Filter not working fine");
					result=false;
					count=0;
					Assert.assertTrue(result);
				}
			}
			else
			{
				System.out.println("Data is not present");
				result=false;
				Assert.assertTrue(result);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result=false;
			Assert.assertTrue(result);
		}
	}
	
	
	//function to check Filter Control with Invalid Input
	@Test(priority=4)
	public void testFilterWithInvalidInput() throws IOException
	{
		System.out.println("==============Verifying Filter with Invalid Inputs===================");
		List<String> searchStr = new ArrayList<String>();
		searchStr = cm.getRowData(2, filterdataSheet);
		String searchString = searchStr.get(1);
		
		try
		{
			WebElement weTxtFilter = cm.getElement(driver, txtFilter);
			WebElement weTblRow = cm.getElement(driver, tblRow);
			
			weTxtFilter.clear();
			weTxtFilter.sendKeys(searchString);
			boolean present = wait.until(ExpectedConditions.stalenessOf(weTblRow));
			if(present==true)
			{
				System.out.println("Data is not present and Filter working fine");
				result=true;
				Assert.assertTrue(result);
			}
			else
			{
				System.out.println("Data is present even if Invalid string is provided hence Filter is not working");
				result=false;
				Assert.assertTrue(result);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result=false;
			Assert.assertTrue(result);
		}
	}
	
	//function to verify sorting on Average Impact Score Column
	@Test(priority=5)
	public void testAvgImpactScoreSort() throws IOException
	{
		System.out.println("==============Verifying Sort of Impact Score Column===================");
		List<Float> impactIntData = new ArrayList<>();
		List<Float> afterSort = new ArrayList<>();
		
		try
		{
			WebElement weTxtFilter = cm.getElement(driver, txtFilter);
			weTxtFilter.clear();
			driver.navigate().refresh();
			
			WebElement weTblData = cm.getElement(driver, tblData);
			WebElement weddSort = cm.getElement(driver, ddSort);
			WebElement weTxtFilter1 = cm.getElement(driver, txtFilter);
			wait.until(ExpectedConditions.visibilityOf(weTxtFilter1));
			Select ddSelectSort = new Select(weddSort);
			List<WebElement> colData = driver.findElements(By.xpath("//div[contains(@class,'data-averageImpact')]"));
			for(int i=0;i<colData.size();i++)
			{
				String actData = driver.findElement(By.xpath("(//div[contains(@class,'data-averageImpact')])["+(i+1)+"]")).getText();
				impactIntData.add(Float.parseFloat(actData));
			}
			Collections.sort(impactIntData);
			
			weddSort.click();
			ddSelectSort.selectByValue("averageImpact");
			wait.until(ExpectedConditions.visibilityOf(weTblData));
			List<WebElement> colData1 = driver.findElements(By.xpath("//div[contains(@class,'data-averageImpact')]"));
			for(int i=0;i<colData1.size();i++)
			{
				String actData = driver.findElement(By.xpath("(//div[contains(@class,'data-averageImpact')])["+(i+1)+"]")).getText();
				afterSort.add(Float.parseFloat(actData));
			}
			for(int i=0;i<afterSort.size();i++)
			{
				float temp = impactIntData.get(i);
				float temp1 = afterSort.get(i);
				
				if(temp==temp1)
				{
					count++;
				}
				else
				{
					count=0;
				}
			}
			if(count==afterSort.size())
			{
				System.out.println("Sorting working properly");
				result = true;
				count=0;
				Assert.assertTrue(result);
			}
			else
			{
				System.out.println("Sorting not working properly");
				result = false;
				count=0;
				Assert.assertTrue(result);
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result=false;
			Assert.assertTrue(result);
		}
	}
	
	//function to verify Sorting for Complexity Column
	@Test(priority=6)
	public void testComplexitySort() throws IOException
	{
		System.out.println("==============Verifying Sort of Complexity Column===================");
		List<String> afterSort = new ArrayList<>();
		List<String> beforeSort = new ArrayList<>();
		
		try
		{
			WebElement weTxtFilter = cm.getElement(driver, txtFilter);
			weTxtFilter.clear();
			driver.navigate().refresh();
			
			WebElement weTblData = cm.getElement(driver, tblData);
			WebElement weddSort = cm.getElement(driver, ddSort);
			WebElement weTxtFilter1 = cm.getElement(driver, txtFilter);
			
			wait.until(ExpectedConditions.visibilityOf(weTxtFilter1));
			Select ddSelectSort = new Select(weddSort);
			wait.until(ExpectedConditions.visibilityOf(weTblData));
			
			//Storing complexity Column data before applying sorting
			List<WebElement> comlexColData = driver.findElements(By.xpath("//div[contains(@class,'data-complexity')]"));
			for(int i=0;i<comlexColData.size();i++)
			{
				String actData = driver.findElement(By.xpath("(//div[contains(@class,'data-complexity')])["+(i+1)+"]")).getText();
				beforeSort.add(actData);
			}
			
			//To sort complexity column to verify against after applying sorting
			for(int i=0;i<beforeSort.size();i++)
			{
				for(int j=i+1;j<beforeSort.size();j++)
				{
					String temp1 = beforeSort.get(j);
					if(temp1.equalsIgnoreCase("low"))
					{
						Collections.swap(beforeSort, i, j);
					}
				}
			}
	
			for(int i=0;i<beforeSort.size();i++)
			{
				for(int j=i+1;j<beforeSort.size();j++)
				{
					String temp = beforeSort.get(i);
					String temp1 = beforeSort.get(j);
					if(temp1.equalsIgnoreCase("Medium")&&temp.equalsIgnoreCase("High"))
					{
						Collections.swap(beforeSort, i, j);
					}
				}
			}
			weddSort.click();
			ddSelectSort.selectByValue("complexity");
			wait.until(ExpectedConditions.visibilityOf(weTblData));
			List<WebElement> colData = driver.findElements(By.xpath("//div[contains(@class,'data-complexity')]"));
			for(int i=0;i<colData.size();i++)
			{
				String actData = driver.findElement(By.xpath("(//div[contains(@class,'data-complexity')])["+(i+1)+"]")).getText();
				afterSort.add(i, actData);
			}
			for(int i=0;i<afterSort.size();i++)
			{
				String beforeString = beforeSort.get(i);
				String afterString = afterSort.get(i);
				if(beforeString.equalsIgnoreCase(afterString))
				{
					count++;
				}
				else
				{
					count=0;
				}
			}
			if(count==afterSort.size())
			{
				System.out.println("Sorting working properly");
				result = true;
				count=0;
				Assert.assertTrue(result);
			}
			else
			{
				System.out.println("Sorting not working properly");
				result = false;
				count=0;
				Assert.assertTrue(result);
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result=false;
			Assert.assertTrue(result);
		}
	}
	
	
	//Function to verify Sorting when filter is applied
	@Test(priority=7)
	public void testFilterandSorting() throws IOException
	{
		System.out.println("====================Verifying Sorting with Filter==============");
		List<String> searchStr = new ArrayList<String>();
		List<Float> impactIntData = new ArrayList<>();
		List<Float> afterSort = new ArrayList<>();
		searchStr = cm.getRowData(1, filterdataSheet);
		String searchString = searchStr.get(1);
		
		try
		{
			WebElement weTxtFilter = cm.getElement(driver, txtFilter);
			WebElement weTblData = cm.getElement(driver, tblData);
			
			weTxtFilter.clear();
			weTxtFilter.sendKeys(searchString);
			wait.until(ExpectedConditions.visibilityOf(weTblData));
			List<WebElement> listData = driver.findElements(By.xpath(tblRow));
			WebElement weddSort = cm.getElement(driver, ddSort);
			Select selectSort = new Select(weddSort);
			if(listData.size()>0)
			{
				for(int i=0;i<listData.size();i++)
				{
					String actText = driver.findElement(By.xpath("//div[@class='table-row']["+(i+1)+"]/div[contains(@class,'data-name')]")).getText();
					if(actText.toLowerCase().contains(searchString.toLowerCase()))
					{
						count++;
						System.out.println("Record matched correctly");
					}
					else
					{
						System.out.println("Incorrect data filtered");
					}
				}
				if(count==listData.size())
				{
					count=0;//resetting to 0 to reuse same variable for sorting result
					System.out.println("Filter Control working fine");
					List<WebElement> colData = driver.findElements(By.xpath("//div[contains(@class,'data-averageImpact')]"));
					for(int i=0;i<colData.size();i++)
					{
						String actData = driver.findElement(By.xpath("(//div[contains(@class,'data-averageImpact')])["+(i+1)+"]")).getText();
						impactIntData.add(Float.parseFloat(actData));
					}
					Collections.sort(impactIntData);
							
					weddSort.click();
					selectSort.selectByValue("averageImpact");
					wait.until(ExpectedConditions.visibilityOf(weTblData));
					List<WebElement> colData1 = driver.findElements(By.xpath("//div[contains(@class,'data-averageImpact')]"));
					for(int i=0;i<colData1.size();i++)
					{
						String actData = driver.findElement(By.xpath("(//div[contains(@class,'data-averageImpact')])["+(i+1)+"]")).getText();
						afterSort.add(Float.parseFloat(actData));
					}
					
					for(int i=0;i<afterSort.size();i++)
					{
						float temp = impactIntData.get(i);
						float temp1 = afterSort.get(i);
						if(temp==temp1)
						{
							count++;
						}
						else
						{
							count=0;
						}
					}
					if(count==afterSort.size())
					{
						System.out.println("Sorting working properly");
						result = true;
						Assert.assertTrue(result);
					}
					else
					{
						System.out.println("Sorting not working properly");
						result = false;
						Assert.assertTrue(result);
					}
				}
				else
				{
					System.out.println("Records are mis matched hence Filter not working fine");
					result=false;
					count=0;
					Assert.assertTrue(result);
				}
			}
			else
			{
				System.out.println("Data is not present");
				result=false;
				Assert.assertTrue(result);
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result=false;
			Assert.assertTrue(result);
		}
	}
	
	@AfterTest
	public void afterCleanUp()
	{
		driver.quit();
	}

}