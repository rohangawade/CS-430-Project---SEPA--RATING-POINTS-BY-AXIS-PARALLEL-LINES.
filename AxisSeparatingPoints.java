import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class AxisSeparatingPoints {
	
	List vert = new ArrayList();
	List horz = new ArrayList();
	List solution;
	BufferedReader br;
	BufferedWriter bw;
	String outfilename;
	public int XC[],YC[];

	/*function to read files in folder and to write output */
	public void readwritefile()throws Exception
	{
		//Get all files from input folder
		File folder = new File("input");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) 
		{
		  File file = listOfFiles[i];
		  String getname= file.getName();
		  String fileno= "";
		  for(int p=8;p<getname.length();p++)
		  {
		   fileno = fileno + getname.charAt(p);
		  }
		  //System.out.println("file no ="+fileno);
		  String line = "",readCurrentLine[];
		  //read all txt files
		  if (file.isFile())
		  {
			 String filename = file.getName();
			 int numLines = 0;
			 BufferedReader br = new BufferedReader(new FileReader("input/"+filename));
			 line = br.readLine();
			 //get firstline from file which gives total points
			 numLines = Integer.parseInt(line.trim());
			 //initialize X and Y coordinates array
			 XC = new int[numLines];
			 YC = new int[numLines];
			 //create list for storing vertical and horizontal solution and solution arraylist to print the solution
			 vert= new ArrayList<>();
			 horz= new ArrayList<>();
			 solution = new ArrayList<>();
			 int j=0;
			 //readfile line by line
			 while((line = br.readLine())!=null)
			 {
				readCurrentLine = line.trim().split("\\s+");
				XC[j] = Integer.parseInt(readCurrentLine[0]);
				YC[j] = Integer.parseInt(readCurrentLine[1]); 
				j++;		
			 } 		 
			 br.close(); 
		  }   
		  //create output file according to store output.
		  outfilename = "greedy_solution"+fileno;
		 /* if(i < 10)
		  {
			  outfilename = outfilename + "0" + (i+1);
		  }
		  else
		  {
			  outfilename = outfilename + (i+1);
		  }
		  outfilename = outfilename;
		  */
		  //System.out.println("Outfile name ="+ fileno);
		  
		 // File outfile = new File("output_greedy",outfilename);
		  File outfile = new File("output",outfilename);
			 
		  if(!outfile.exists())
		  {
			  outfile.createNewFile();
		  }
		  else
		  {
			  outfile.delete();
			  outfile.createNewFile();		
		  }	 	  
		  
		 // bw = new BufferedWriter(new FileWriter("output_greedy/"+outfilename));
		  bw = new BufferedWriter(new FileWriter("output/"+outfilename));
		  
		  //call function to select vertical and horizontal line
		  greedy_SeparatingPoints(XC, YC,0,XC.length-1);
	
		  bw.write(solution.size()+"\n");
		  for(int z=0;z<solution.size();z++)
		  {
			  bw.write((String)solution.get(z)+"\n");
		  }
		  bw.close();
		}		
	}

	public void greedy_SeparatingPoints(int X[], int Y[],int start,int end)
	{
		int length = X.length;
		boolean splitH = false,splitV=false;
		int diff = end-start;
		if(diff>0)
		{
			float vertical = 0;
			float horizontal = 0;	
			//calculate horizontal and vertical split according to x	
			int firstvalue = X[start];		
			int lastvalue = X[end];
			int splitmid = (firstvalue + lastvalue);
			if((splitmid % 2) != 0)
			{
				vertical = (float)splitmid/2;
				horizontal = (float)splitmid/2;
			}	
			else
			{	
				vertical = (float)(splitmid-1)/2;
				horizontal = (float)(splitmid-1)/2;		
			}
			if(vert.isEmpty())
			{ 
				vert.add(new Float(vertical));
				solution.add("v "+vertical);
			}
			else
			{
				splitV = checksplitV(X,Y,vertical);
				if(splitV == true)
				{	
					vert.add(new Float(vertical));
					solution.add("v "+vertical);
				}
			}
			splitH = checksplitH(X, Y, horizontal);
			if(splitH == true)
			{		
				horz.add(new Float(horizontal));
				solution.add("h "+horizontal);				
			}
			//recursive calling
			int upstart = (int)(vertical - 0.5);
			int downend = (int)(vertical - 1.5);
			
			greedy_SeparatingPoints(X, Y, start,downend);
			greedy_SeparatingPoints(X, Y, upstart, end);					
		}			
	}
	
	public boolean checksplitH(int X1[],int Y1[],float splitpoint)
	{
		int flagsplit = 0;
		
		for(int i=0;i<Y1.length;i++)
		{
			for(int j=i+1;j<Y1.length;j++)
			{
				if(flagsplit==0)
				{
					//check if line is between two points.
					if((Y1[i]>splitpoint && Y1[j]<splitpoint) || (Y1[i]<splitpoint && Y1[j]>splitpoint))
					{
						//check if there exist vertical line between the two points.
						int x1 = X1[i];
						int x2 = X1[j];
						int y1 = Y1[i];
						int y2 = Y1[j];
						int splitvertfound = 0;
						for(int k=0;k<vert.size();k++)
						{
							if(splitvertfound ==0)
							{	
								float vertival =(float) vert.get(k);
								if((x1<vertival && x2>vertival))
								{
								//there exists a line between points. Dont split 
								splitvertfound = 1;
								}
							}
						}
						if(splitvertfound==0)
						{
							//point is available for split. Check if any horizontal line exists between this two points.
							if(horz.isEmpty())
							{
								flagsplit=1;
							}
							int flagcount =0;													
							for(int l=0;l<horz.size();l++)
							{
								if(flagcount == 0)
								{
									float horzval = (float)horz.get(l);
									//System.out.println("horzval = "+horzval);
									if((y1>horzval && y2 <horzval)||(y1<horzval && y2 >horzval))
									{
										flagcount = 1;
									}
								}
							}
							if(flagcount == 1)
							{
								flagsplit =0;									
							}
							else
							{ 
								flagsplit = 1;
								break;
							}	
						}			
					}
				}
			}
		}
		if(flagsplit == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean checksplitV(int X1[],int Y1[],float splitpoint)
	{
		int flagsplit=0;
		int x1,x2;
		//get the closest point to split point
		x1 = (int)(splitpoint - 0.5 - 1);
		x2 = (int)(splitpoint + 0.5 - 1);
		
		//check if there exist a horizontal line between the y coordinates of x1close and x2close
		
		for( int k =0;k<horz.size();k++)
		{
			if(flagsplit==0)
			{
				float betweenline = (float) horz.get(k);
				//check if there the horizontal line already splits the point.
				if((Y1[x1]> betweenline && Y1[x2]<betweenline) || (Y1[x1]<betweenline && Y1[x2]> betweenline))
				{
					//There is a line between Y1[x1] and Y1[x2]. No split
					flagsplit = 1;
				}
				else
				{
					//No line between two points. so we can split
					flagsplit = 0;
				}
			}
		}		
		if(flagsplit == 1)
			return false;
		else 
			return true;	
	}		 
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		AxisSeparatingPoints asp = new AxisSeparatingPoints();
		asp.readwritefile();
	}
}
