package stddata;

import java.io.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class Details {
	
	public static class MapClass extends Mapper<LongWritable,Text,LongWritable,Text>
	   {
	      public void map(LongWritable key, Text value, Context context)
	      {	    	  
	         try{
	          
                  
	           
	        	 context.write(key,value);
	         }
	         catch(Exception e)
	         {
	            System.out.println(e.getMessage());
	         }
	      }
	   }
	
	 
		  public static void main(String[] args) throws Exception {
		    Configuration conf = new Configuration();
		    Job job = new Job(conf, "name Count");
		    job.setJarByClass(Details.class);
		    job.setMapperClass(MapClass.class);
		    job.setOutputKeyClass(LongWritable.class);
		    job.setOutputValueClass(Text.class);
		    FileInputFormat.addInputPath(job, new Path(args[0]));
		    FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    System.exit(job.waitForCompletion(true) ? 0 : 1);
		  }
}
