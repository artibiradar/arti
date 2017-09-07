package moviesbet1;

import java.io.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;



public class moviesbetwn{
	
	public static class MapClass extends Mapper<LongWritable,Text,Text,Text>
	   {
	      public void map(LongWritable key, Text value, Context context)
	      {	    	  
	         try{
	            String[] str = value.toString().split(",");	 
	            long year = Long.parseLong(str[2]);
	            
	            
	            if(year>=1945 && year<=1959)
	            {
	            	String moviename=str[1];
	            	 context.write(new Text("1"),new Text(moviename));
	            	
	            }
	           
	         }
	         catch(Exception e)
	         {
	            System.out.println(e.getMessage());
	         }
	      }
	   }
	  public static class ReduceClass extends Reducer<Text,Text,Text,IntWritable>
	   {
		   
		    
		    public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
		      int count= 0;	
		         for (Text total : values)
		         {    
		        	 
		        	count++;
		         }
		     
		      context.write(key,new IntWritable(count));
		      
		      
		    }
	   }
	
	  public static void main(String[] args) throws Exception {
		    Configuration conf = new Configuration();
		   
		    Job job = new Job(conf, "MOVIE DATA");
		    job.setJarByClass(moviesbetwn.class);
		    job.setMapperClass(MapClass.class);
		    job.setMapOutputKeyClass(Text.class);
		    job.setMapOutputValueClass(Text.class);
		    job.setReducerClass(ReduceClass.class);
		    job.setOutputKeyClass(Text.class);
		    job.setOutputValueClass(IntWritable.class);
		    FileInputFormat.addInputPath(job, new Path(args[0]));
		    FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    System.exit(job.waitForCompletion(true) ? 0 : 1);
		  }
}

