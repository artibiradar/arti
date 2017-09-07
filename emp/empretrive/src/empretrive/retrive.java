package empretrive;

import java.io.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class retrive {
	
	public static class MapClass extends Mapper<LongWritable,Text,Text,Text>
	   {
	      public void map(LongWritable key, Text value, Context context)
	      {	    	  
	         try{
	            String[] str = value.toString().split(",");	 
	            String name  = str[2];
	            context.write(new Text(str[1]), new Text (name));
	         }
	         catch(Exception e)
	         {
	            System.out.println(e.getMessage());
	         }
	      }
	   }

               public static void main(String[] args) throws Exception {
		    Configuration conf = new Configuration();
		    Job job = new Job(conf, "Volume Count");
		    job.setJarByClass(retrive.class);
		    job.setMapperClass(MapClass.class);
		    job.setOutputKeyClass(Text.class);
		    job.setOutputValueClass(Text.class);
		    FileInputFormat.addInputPath(job, new Path(args[0]));
		    FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    System.exit(job.waitForCompletion(true) ? 0 : 1);
		  }
}


