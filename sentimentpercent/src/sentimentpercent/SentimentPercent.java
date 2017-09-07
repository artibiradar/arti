package sentimentpercent;

import java.io.BufferedReader;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class SentimentPercent {
	
	public static class TokenizerMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
		
		private Map<String, String> abMap =new HashMap<String, String>();
		private final static IntWritable total_value =new IntWritable();
		private Text word = new Text();
		String myWord ="";
		int myValue =0;
		
		protected void setup (context context) throws java.io.IOException, InterruptedExeption{
			super.setup(context);
			
			URI[] files = context.getCacheFiles(); //getcaheFiles return null
			
			path p= new path(files[0]);
			
			if(p.getName().equals("AFINN.txt")){
				BufferedReader reader =new BufferedReader(new FileReader(p.toString()));
				String line = reader.readLine();
				while (line != null){
					String[] tokens =line.split("\t");
					String diction_word = tokens[0];
					String diction_value = tokens[0];
					abMap.put(diction_word, diction_value);
					line=reader.readLine();
				}
				reader.close();
			}
			
			if (abMap.isEmpty()){
				throw new IOException("MyError:Unable to load dictionary data.");
			}
		}
		public void map(LongWritable key, Text value, Context context
				)throws IOException, InterruptedException {
			StringTokenizer itr= new StringTokenizer(value.toString());
			while (itr.hasMoreTokens())
			{
				myWord=itr.nextToken().toLowercase();
				if (abMap.get(myWord)!=null)
				{
					myValue=Integer.parseInt(abMap.get(myWord));
					if(myValue>0)
					{
						myWord="positive";
					}
					if (myValue<0)
					{
						myWord="negative";
						myValue=myValue * -1;
					}
				}
				else
				{
					myWord="positive";
					myValue=0;
				}
				word.set(myWord);
				total_value.set(myValue);
				context.write(word,total_value);
			}
			public static class IntSumReducer
			extends Reducer<Text,IntWritable,NullWritable,Text>
			{
				int pos_total=0; //variables
				int neg_total=0;
				double sentpercent=0.00;
				
				public void reduce(Text key, Iterable<IntWritable>values,Context context
						)throws IOException , InterruptedException{
					int sum=0;
					for (IntWritable val :values)
					{
						sum +=val.get();
					}
					
					if (key.toString().equals("positive"))
					{
						pos_total=sum;
					}
					
					if (key.toString().equals("negative"))
					{
						neg_total=sum;
					}
				}
				protected void cleanup(Context context) throws IOException,
				InterruptedException
				{
					sentpercent =(((double)pos_total) -((double)neg_total))/(((double)pos_total)+((double)neg_total))*100;
					 
			
String str="Sentiment percent of given text\t"+String.format("%.2f", sentpercent);
context.write(NullWritable.get(), new Text(str));
}
}
public static void main(String ar[]) throws Exception
{
try
{
Configuration conf = new Configuration();
Job job = Job.getInstance(conf,"sentiment %");
job.setJarByClass(SentimentPercent.class);			
job.setMapperClass(SentimentMapper.class);
job.addCacheFile(new Path("AFINN.txt").toUri());			
job.setReducerClass(ReduceClass.class);
//job.setNumReduceTasks(0);	      
job.setMapOutputKeyClass(Text.class);
job.setMapOutputValueClass(IntWritable.class);	      
job.setInputFormatClass(TextInputFormat.class);			
job.setOutputFormatClass(TextOutputFormat.class);
job.setOutputKeyClass(NullWritable.class);
job.setOutputValueClass(Text.class);	      
FileInputFormat.setInputPaths(job, new Path(ar[0]));
FileOutputFormat.setOutputPath(job,new Path(ar[1]));			
System.exit(job.waitForCompletion(true)? 0 : 1);
}
catch(ArrayIndexOutOfBoundsException e)
{
	System.out.println(e.getMessage());
}
}
}

