package movies;
import java.io.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class Moviescount
{
	 public static class mymap extends Mapper<LongWritable,Text, Text,Text>
	 {
		public void map (LongWritable inkey,Text val,Context context) throws InterruptedException,IOException
		{
			String[] str= val.toString().split(",");
			String name=str[1];
			
			Text outkey=new Text("1");
			Text movie= new Text(name);
			context.write(outkey,movie);
			
		}
		
	 }
	 public  static class myreduce extends Reducer<Text,Text,Text,IntWritable> 
	 {
		 public void reduce (Text inkey,Iterable<Text>val,Context context)throws InterruptedException,IOException
		 {
			 int count=0;
			 for(Text v:val)
			 {
				 count++;
			 }
			 context.write(inkey,new IntWritable(count));
		 }
	 }
	 
	 public static void main(String[] args) throws Exception
	 {
		    Configuration conf = new Configuration();
		    
		    Job job = new Job(conf, "movie");
		    job.setJarByClass(Moviescount.class);
		    job.setMapperClass(mymap.class);
		    job.setReducerClass(myreduce.class);
		    job.setNumReduceTasks(1);
		    job.setOutputKeyClass(Text.class);
		    job.setOutputValueClass(Text.class);
		    FileInputFormat.addInputPath(job, new Path(args[0]));
		    FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    System.exit(job.waitForCompletion(true) ? 0 : 1);
		  }
}