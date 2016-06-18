package com.stookers.stockclass;
import java.util.ArrayList;
import  java.util.GregorianCalendar;
import  java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class StockDataDownload
{	
	static Float nPrev;
	
	private static String sNegetive  = "Negetive";
    private static String sPositive  = "Positive";
    private static String sEqual     = "Equal";

    private static final String sWriteCSVPath  = "c:/eclipse projects/stookers/stockdata/";
    
    private static String sStockDataFileName;
    private static String sFileName;
    
	
	private static final String FILE_HEADER = "Previous,Open,Max,Min,Last,Action";
	
	public StockDataDownload(String start,String end,String symbol)
	{
		sStockDataFileName 	= symbol.toUpperCase() + "_StockData.csv";
		sFileName 			= symbol.toUpperCase() + "_StockCopyData.csv";
		
		String[] start1=new String[3];
		String[] end1=new String[3];
		System.out.println(start+"$$"+end);
		
		int i=0;
		  for (String retval: start.split("-")){
		         start1[i]=retval;
		         i++;
		      }
		  int j=0;
		  for (String retval: end.split("-")){
		         end1[j]=retval;
		         j++;
		      }
		GregorianCalendar startdate = new GregorianCalendar(Integer.parseInt(start1[0])-1,Integer.parseInt(start1[1]),Integer.parseInt(start1[2]));	
		GregorianCalendar enddate = new GregorianCalendar(Integer.parseInt(end1[0])-1,Integer.parseInt(end1[1]),Integer.parseInt(end1[2]));
		CreateStockData(symbol,startdate,enddate);
	}
	
	static void CreateStockData(String symbol, GregorianCalendar start, GregorianCalendar end)
	{		
		String sURL = BuildURL(symbol,start,end);	
		System.out.println(sURL);
		try
		{
			URL yahoofin = new URL(sURL);
			URLConnection data = yahoofin.openConnection();
			Scanner input = new Scanner(data.getInputStream());
			if(input.hasNext())
				input.nextLine();
			
			WriteStockData(input);
			
			ReadStockDataCSV();
			
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());			
		}
	}
	
	static String BuildURL(String symbol,GregorianCalendar start, GregorianCalendar end)
	{
		return "http://real-chart.finance.yahoo.com/table.csv?s="+symbol+
				"&d="+end.get(Calendar.MONTH)+
				"&e="+end.get(Calendar.DAY_OF_MONTH)+
				"&f="+end.get(Calendar.YEAR)+
				"&g=d&a="+start.get(Calendar.MONTH)+
				"&b="+start.get(Calendar.DAY_OF_MONTH)+
				"&c="+start.get(Calendar.YEAR)+
				"&ignore=.csv";
		
	}
	
	static void WriteStockData(Scanner input) 
	{
		try
		{
			Random obj = new Random();
			String sPrevClose = "";
			FileWriter filewriter = new FileWriter(sWriteCSVPath + sStockDataFileName);
			
			//Write the CSV file header
			filewriter.append(FILE_HEADER.toString());
			filewriter.append("\n");
		
			while(input.hasNextLine())
			{
				String line = input.nextLine();
				String[] line_parts = line.split(",");
				
				if(sPrevClose.equals(""))
				{
					line_parts[0] = line_parts[1];
				}
				else
				{
					line_parts[0] = sPrevClose;
				}
				for(int i=0;i<line_parts.length-3;i++)
				{
					filewriter.append(line_parts[i]);
					filewriter.append(",");
				}
				
				filewriter.append(line_parts[line_parts.length-3]);
				filewriter.append(",");
				if(obj.nextInt(100) < 50)
				{
					filewriter.append("Buy");
				}
				else
				{
					filewriter.append("Sell");
				}
				
				filewriter.append("\n");				
				sPrevClose = line_parts[4];
			}
			filewriter.close();
		}
		catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}		
	}
	
	static void ReadStockDataCSV()
    {
		int nRowCounter = 1;
		List<Object[]> objArray = new ArrayList<Object[]>();
		Object[][] objArrayCopy;        

        BufferedReader fileReader = null;
        
        try
        {        
	        fileReader = new BufferedReader(new FileReader(sWriteCSVPath + sStockDataFileName));
	
	        fileReader.readLine();
	        String line = "";
	        
	        //Read the file line by line starting from the second line
	        while ((line = fileReader.readLine()) != null)
	        {
	        	String[] tokens = line.split(",");
	        	if (tokens.length > 0) 
	        	{    		
	        		objArray.add(new Object[] {tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5]});
	        	}
	        }
	        
	        fileReader.close();
	        
	        objArrayCopy = new Object[objArray.size()][objArray.get(0).length];
	        
	        for(int a=0; a< objArray.size();a++)
	        {
	        	for(int b=0;b<objArray.get(a).length;b++)
	        	{
	        		if (nRowCounter == 1)
                    {
	        			if (b < objArray.get(a).length-1)
                        {
                            if (b == 0)
                            {
                            	objArrayCopy[a][b] = Comparer(Float.valueOf((String)objArray.get(a)[b]), Float.valueOf((String)objArray.get(a)[b+1]));
                            }
                            else
                            {
                            	objArrayCopy[a][b] = Comparer(Float.valueOf((String)objArray.get(a)[0]), Float.valueOf((String)objArray.get(a)[b]));
                            }
                        }
	        			else
	        			{
	        				objArrayCopy[a][b] = (String)objArray.get(a)[b];
	        			}
                    }
	        		
	        		else
                    {
	        			if (b < objArray.get(a).length-1)
                        {
	        				if (b == 0)
                            {
	        					objArrayCopy[a][b] = Comparer(nPrev, Float.valueOf((String)objArray.get(a)[b]));
                            }
                            else
                            {
                            	objArrayCopy[a][b] = Comparer(Float.valueOf((String)objArray.get(a)[0]), Float.valueOf((String)objArray.get(a)[b]));
                            }
                        }
	        			else
	        			{
	        				objArrayCopy[a][b] = (String)objArray.get(a)[b];
	        			}
                    }
	        	}
	        	nRowCounter++;
	        	nPrev = Float.valueOf((String)objArray.get(a)[0]);
	        }
	        
	        WriteCSV(objArrayCopy);	        
        }
        catch(Exception e)
		{
        	System.err.println(e.getMessage());
		}
    }
	
	static String Comparer(float nPrev, float nNum)
    {
        if (nPrev > nNum)
            return sNegetive;
        else if (nPrev < nNum)
            return sPositive;
        else
            return sEqual;
    }

	static void WriteCSV(Object[][] objArray) 
	{
		try
		{
			FileWriter filewriter = new FileWriter(sWriteCSVPath + sFileName);
			
			filewriter.append(FILE_HEADER.toString());
			filewriter.append("\n");
			
			for(int a=0; a< objArray.length;a++)
	        {
	        	for(int b=0;b<objArray[a].length-1;b++)
	        	{
	        		filewriter.append((String)objArray[a][b]);
	        		filewriter.append(",");
	    		}
	        	filewriter.append((String)objArray[a][objArray[a].length-1]);
	        	filewriter.append("\n");
	    	}
			
			filewriter.close();
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
}
