package moviesyercount4;

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




public class  moviesyercount{
	public static class MapClass extends Mapper<LongWritable,Text,LongWritable,Text>
	   {
	      public void map(LongWritable key, Text value, Context context)
	      {	    	  
	         try{
	            String[] str = value.toString().split(",");	 
	            String moviename =str[1];
	            long year = Long.parseLong(str[2]);
	            context.write(new LongWritable(year),new Text(moviename));
	            
	           
	           
	         }
	         catch(Exception e)
	         {
	            System.out.println(e.getMessage());
	         }
	      }
	   }
	  public static class ReduceClass extends Reducer<LongWritable,Text,LongWritable,IntWritable>
	   {
		   
		    
		    public void reduce(LongWritable key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
		      int count= 0;	
		         for (Text T : values)
		         {    
		        	 
		        	count++;
		         }
		     
		      context.write(key,new IntWritable(count));
		      
		      
		    }
	   }
	
	  public static void main(String[] args) throws Exception {
		    Configuration conf = new Configuration();
		   
		    Job job = new Job(conf, "MOVIE DATA");
		    job.setJarByClass(moviesyercount.class);
		    job.setMapperClass(MapClass.class);
		    job.setMapOutputKeyClass(LongWritable.class);
		    job.setMapOutputValueClass(Text.class);
		    job.setReducerClass(ReduceClass.class);
		    job.setOutputKeyClass(LongWritable.class);
		    job.setOutputValueClass(IntWritable.class);
		    FileInputFormat.addInputPath(job, new Path(args[0]));
		    FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    System.exit(job.waitForCompletion(true) ? 0 : 1);
		  }


}

