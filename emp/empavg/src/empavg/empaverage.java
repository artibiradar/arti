package empavg;

import java.io.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;


public class empaverage {
	public static class MapClass extends Mapper<LongWritable,Text,Text,LongWritable>
	   {
	      public void map(LongWritable key, Text value, Context context)
	      {	    	  
	         try{
	            String[] str = value.toString().split(",");	 
	            long salary = Long.parseLong(str[3]);
	            context.write(new Text(str[1]),new LongWritable(salary));
	         }
	         catch(Exception e)
	         {
	            System.out.println(e.getMessage());
	         }
	      }
	   }
	
	  public static class ReduceClass<DoubleWritable> extends Reducer<Text,LongWritable,Text,LongWritable>
	   {
		   // private LongWritable result = new LongWritable();
		    
		    public void reduce(Text key, Iterable<LongWritable> values,Context context) throws IOException, InterruptedException {
		      long sum = 0;
			int count=0;
			long avg=0;	
		         for (LongWritable total : values)
		         {       	
		        	sum += total.get();
   				count++;
		         }
			avg=sum/count;
		//LongWritable Text res=Avg+ ',' +count;
		         
		     // avg.set(Avg);		      
		      context.write(key, new LongWritable(avg));
		      
		      
		    }
	   }
	  public static void main(String[] args) throws Exception {
		    Configuration conf = new Configuration();
		   
		    Job job = new Job(conf, "Volume Count");
		    job.setJarByClass(empaverage.class);
		    job.setMapperClass(MapClass.class);	
		    job.setReducerClass(ReduceClass.class);
		    job.setNumReduceTasks(1);
		    job.setOutputKeyClass(Text.class);
		    job.setOutputValueClass(LongWritable.class);
		    FileInputFormat.addInputPath(job, new Path(args[0]));
		    FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    System.exit(job.waitForCompletion(true) ? 0 : 1);
		  }

}
