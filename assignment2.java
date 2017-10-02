/* Machine Learning Assignment 2
 * Naive Bayes and Logistic Regression
 * Vidya Sri Mani
 * vxm163230
 */
import java.io.*;
import java.util.*;
import java.util.Map.Entry;


public class assignment2 {
	static double spamcount = 0;//number of spam examples
	static double hamcount = 0;//number of ham examples
	static double totalNumberOfExamples = 1;//spam +ham examples
	static ArrayList<String> stopWords = new ArrayList<String>();
	
	//LR
	static double lambda = 0.001;
	static int numbeofIterations = 500;
	static HashMap<String,Integer> distinctWords = new HashMap<String,Integer>();
	static ArrayList<String> FeatureRepository = new ArrayList<>();
	static Map <String, Double> WeightedWords = new HashMap <>();
	static HashSet <String> WordOccurrences= new HashSet <String> ();
	static double S=0;
    static double SNew=0;
    static double total=0;
    static double total1=0;
	static double TruePositives = 0;
	static double FalseNegatives= 0;
	static double FalsePositives = 0;
	static double TrueNegatives =0;
	static boolean withStopwords=true;
	static double clearPositive=7;
	
	public static void main(String[] args) throws java.io.IOException{
		loadStopWords();
		/*Order of Input to program
		 * 1.Training Ham folder location
		 * 2.Training Spam folder Location
		 * 3.Test Ham folder location
		 * 4.Test Spam folder Location
		 * Optional: 
		 * - value of lamba : 0.001 by default
		 * - number of iterations : 500 by default 
		 */
		String hamFolder ="";
		String spamFolder ="";
		String TesthamFolder ="";
		String TestspamFolder ="";
		InputStreamReader istream = new InputStreamReader(System.in) ;
        BufferedReader bufRead = new BufferedReader(istream) ;       
        try {
        	System.out.println("Enter Training Ham Folder Location");
        	hamFolder=bufRead.readLine();
        	System.out.println("Enter Training Spam Folder Location");
        	spamFolder=bufRead.readLine();
        	System.out.println("Enter Test Ham Folder Location");
        	TesthamFolder=bufRead.readLine();
        	System.out.println("Enter Test Spam Folder Location");
        	TestspamFolder=bufRead.readLine();
        }
        catch (IOException err) {
        	System.out.println("Error reading input");
        }
		//Spam and Ham folder locations
        File hamFolderLocation = new File(hamFolder);
        File spamFolderLocation = new File(spamFolder);
        File testHamFolderLocation = new File(TesthamFolder);
        File testSpamFolderLocation = new File(TestspamFolder);
		
		//Naive Bayes Classification with stop words
		System.out.println("Naive Bayes with stop words \n----------------------------------------");
		NBClassify(hamFolderLocation, spamFolderLocation, testHamFolderLocation, testSpamFolderLocation,true);
		
		System.out.println("================================================================");
		
		System.out.println("\n\nNaive Bayes without stop words \n----------------------------------------");
		NBClassify(hamFolderLocation, spamFolderLocation, testHamFolderLocation, testSpamFolderLocation,false);
		
		System.out.println("================================================================");
		System.out.println("\nLOGISTIC REGRESSION\n");
		try {
        	System.out.println("Logistic Regression, Number of iterations = 500, value of lambda = 0.001");
        	System.out.println("Enter y/Y to change values, otherwise enter n/N");
        	String choice = bufRead.readLine();
        	if(choice.compareTo("y")==0 || choice.compareTo("Y")==0){
        		System.out.println("Enter new value for lambda :");
        		lambda = Double.parseDouble(bufRead.readLine());
        		System.out.println("Enter new value for number of iterations :");
        		numbeofIterations = Integer.parseInt(bufRead.readLine());
        	}
        	else{
        		
        	}
        }
        catch (IOException err) {
        	System.out.println("Error reading input");
        }
		
		System.out.println("\n\nLogistic Regresion with stop words \n----------------------------------------");
		LRClassify(hamFolderLocation, spamFolderLocation, testHamFolderLocation, testSpamFolderLocation,true);
		
		System.out.println("\n\nLogistic Regresion without stop words \n----------------------------------------");
		LRClassify(hamFolderLocation, spamFolderLocation, testHamFolderLocation, testSpamFolderLocation,false);
	}
	
		public static void NBClassify(File hamFolder,File spamFolder, File testHamFolderLocation, File testSpamFolderLocation,boolean withStopWOrds){	
			System.out.println("Training...");
			double hamLen= 0;//number of words in ham class
			double numberOfDistinctWords = 0;//total number of distinct words
			double spamLen= 0;//number of words in spam class
		
			//creating a new hashmap to enter the words
			HashMap<String,Integer> hhm = new HashMap<String,Integer>();
			File[] files = hamFolder.listFiles();
			for(File f:files){//for every file in the folder
				if(f.isFile()){
					hamcount++;//number of ham class examples
					try{
						/*
						 * for every word in each text file,if the word already exists, then increase count
						 * else
						 * add the new entry to hashmap
						 */
						Scanner s = new Scanner(f).useDelimiter("[^a-zA-Z]");
						while(s.hasNext()){
							String word = s.next().toLowerCase();
							if(withStopWOrds){
								hamLen++;
									if(hhm.containsKey(word)){
										hhm.put(word,hhm.get(word)+1);
									}
									else{
										hhm.put(word,1);
									}
									if(!(distinctWords.containsKey(word))){
										distinctWords.put(word,1);
									}		
							
						}
						else{
							
							if(word.compareTo("")!=0 && !stopWords.contains(word))
							{
								    hamLen++;
									if(hhm.containsKey(word)){
										hhm.put(word,hhm.get(word)+1);
									}
									else{
										hhm.put(word,1);
									}
									if(!(distinctWords.containsKey(word))){
										distinctWords.put(word,1);
									}		
							}
						}
					}
				}
				catch(Exception e){	
					e.printStackTrace(System.out);
				}
			}
		}
		
		//creating a bag of words for spam class
		HashMap<String,Integer> shm = new HashMap<String,Integer>();
		File[] files2 = spamFolder.listFiles();
		for(File f:files2){//for every file in the folder
			if(f.isFile()){
				spamcount++;
				try{
					/*
				     * for every word in each text file,if the word already exists, then increase count
					 * else
					 * add the new entry to hashmap
					 */
					Scanner s = new Scanner(f).useDelimiter("[^a-zA-Z]");
					while(s.hasNext()){
								String word = s.next().toLowerCase();
								if(withStopWOrds){
									spamLen++;
									if(shm.containsKey(word)){
										shm.put(word,shm.get(word)+1);
									}
									else{
										shm.put(word,1);
									}
									if(!(distinctWords.containsKey(word))){
										distinctWords.put(word,1);
									}
								}
								else{
									if(word.compareTo("")!=0 && !stopWords.contains(word)){
										spamLen++;
										if(shm.containsKey(word)){
											shm.put(word,shm.get(word)+1);
										}
										else{
											shm.put(word,1);
										}
										if(!(distinctWords.containsKey(word))){
											distinctWords.put(word,1);
										}
									}
									
								}
							}
						}
						catch(Exception e){	
							e.printStackTrace(System.out);
						}
					}
				}

		//calculating number of distinct words
		numberOfDistinctWords = distinctWords.size();
		
		//for every word calculate the individual probability
		HashMap<String,Double> hamWords = new HashMap<String,Double>();
		HashMap<String,Double> spamWords = new HashMap<String,Double>();
		
		double numerator = 1;
		//for ham class
		double denominator = hamLen + numberOfDistinctWords; 
		//for every word in the ham class
		for(Map.Entry<String,Integer>entry:hhm.entrySet()){
				numerator = entry.getValue()+1;
				hamWords.put(entry.getKey(),(numerator/denominator));
			}
		denominator = spamLen + numberOfDistinctWords;
		for(Map.Entry<String,Integer>entry:shm.entrySet()){
			numerator = entry.getValue()+1;
			spamWords.put(entry.getKey(),(numerator/denominator));
		}
		System.out.println("probabilties calculated");
		//Testing phase
		int testSpamCount=0;
		int predictedSpamCount=0;
		int testHamCount=0;
		int predictedHamCount=0;

		System.out.println("Testing...");
		
		//calculating P(c) and P(~c)
		totalNumberOfExamples = spamcount+hamcount;
		double pHam = hamcount/totalNumberOfExamples;//p(C) for ham class
		double pSpam = spamcount / totalNumberOfExamples;//p(~c) for spamclass

		File[] testHamFiles = testHamFolderLocation.listFiles();
		for(File f:testHamFiles){//for every file in the folder
			if(f.isFile()){
				double pHamWord=Math.log(pHam);
				double pSpamWord = Math.log(pSpam);
				
				testHamCount++;//number of test ham class examples
				try{					
					Scanner s = new Scanner(f).useDelimiter("[^a-zA-Z]");
					while(s.hasNext()){
						
						String word = s.next().toLowerCase();
						if(word.compareTo("")!=0)
						{
							if(hamWords.containsKey(word)){
								pHamWord+=Math.log(hamWords.get(word));
							}
							if(spamWords.containsKey(word)){
								pSpamWord+=Math.log(spamWords.get(word));
							}	
						}
						
					}
				}
			
				catch(Exception e){	
					e.printStackTrace(System.out);
				}
				if(pHamWord < pSpamWord){//since its log is negative
					predictedHamCount++;
				}
				else
				{
					predictedSpamCount++;
				}
			}
		}

		System.out.println("For Test Ham class files");
		System.out.println("Total Number of Ham classified Files:"+ testHamCount );
		System.out.println("Total Number of Predicted Ham classified Files:"+ predictedHamCount + " out of " + testHamCount );
		System.out.println("Total Number of Predicted Spam classified Files:"+ predictedSpamCount + " out of " + testHamCount );
		System.out.println("----------------------------------------");
		hamLen = testHamCount;
		double correctlyClassified = predictedHamCount;
		predictedHamCount = 0;
		predictedSpamCount = 0;
		
		File[] testSpamFiles = testSpamFolderLocation.listFiles();
		for(File f:testSpamFiles){//for every file in the folder
			if(f.isFile()){
				double pHamWord=Math.log(pHam);
				double pSpamWord = Math.log(pSpam);
				
				testSpamCount++;//number of test ham class examples
				try{
					
					Scanner s = new Scanner(f).useDelimiter("[^a-zA-Z]");
					while(s.hasNext()){
						
						String word = s.next().toLowerCase();
						if(word.compareTo("")!=0)
						{
							if(hamWords.containsKey(word)){
								pHamWord+=Math.log(hamWords.get(word));
							}
							if(spamWords.containsKey(word)){
								pSpamWord+=Math.log(spamWords.get(word));
							}	
						}
						
					}
				}
			
				catch(Exception e){	
					e.printStackTrace(System.out);
				}
				if(pHamWord < pSpamWord){//since its log is negative
					predictedHamCount++;
				}
				else
				{
					predictedSpamCount++;
				}
			}
		}
		
		System.out.println("For Test Spam class files");
		System.out.println("Total Number of Spam classified Files:"+ testSpamCount );
		System.out.println("Total Number of Predicted Ham classified Files:"+ predictedHamCount + " out of " + testSpamCount );
		System.out.println("Total Number of Predicted Spam classified Files:"+ predictedSpamCount + " out of " + testSpamCount );
		System.out.println("----------------------------------------");
		spamLen = testSpamCount;
		correctlyClassified += predictedSpamCount;
		
		double accuracy = (correctlyClassified/(hamLen+spamLen))*100;
		System.out.println("Accuracy of Naive Bayes : " + accuracy + "%");
	}
		public static void clearAll(){
			distinctWords.clear();
			FeatureRepository.clear();
			WeightedWords.clear();
			WordOccurrences.clear();
			S=0;
		    SNew=0;
		    total=0;
		    total1=0;
			TruePositives = clearPositive;
			FalseNegatives= 0;
			FalsePositives = 0;
			TrueNegatives =0;
			withStopwords = false;
			
		}
		
		public static void LRClassify(File hamFolder,File spamFolder, File testHamFolderLocation, File testSpamFolderLocation,boolean withStopWOrds){
			
			if(!withStopWOrds){
				clearAll();
			}
		    int numberOfRepetition=0;
    
		    readFiles(hamFolder,withStopWOrds);
		    readFiles(spamFolder,withStopWOrds);
		   
		    train();
		    
		    while(numberOfRepetition < numbeofIterations && S - SNew <lambda)
	        {          
	            trainMessages(hamFolder,0,S);   
	            trainMessages(spamFolder,1,SNew);
	            numberOfRepetition++;
	            S=0;
	            SNew=0;
	        }
		    
		    //classify new messages
		    classifyNewMessages(testHamFolderLocation);
		    classifyNewMessages(testSpamFolderLocation);
		    double accuracy = getAccuracy();
		    System.out.println("Accuracy : "+accuracy*100+"%");

		}
		
		public static double getAccuracy()
		{
			if(total==0) return 0;
			//number of correctly classified messages
			System.out.println("Number of correctly classsified messages : "+(TruePositives+ TrueNegatives));
			System.out.println("Total Number of files : "+ total);
			return ( (double)(TruePositives + TrueNegatives)/(double)total);
		}
		
		public static void classifyNewMessages(File testFolderLocation)
		{
			File[] files = testFolderLocation.listFiles();
			
			try
			{
				for(File f : files)
				{
					total++;
					BufferedReader br = new BufferedReader(new FileReader(f));
					String content="";
					String line = br.readLine();
					while(line!=null) 
					{
						content +=line;
						line=br.readLine();
					}
					
					boolean result = classify(content);
					
					if(f.getName().contains("spm") && result){
						TruePositives++;
					}
					else if (f.getName().contains("spm") && !result){
						FalseNegatives++;
					}
					else if(!f.getName().contains("spm") && result) {
						FalsePositives++;
					}
					else if(!f.getName().contains("spm") && !result){
						TrueNegatives++;
					}
					
					br.close();
				} 
			}
				
			catch(Exception e)
			{
					System.err.println("ERROR : "+e.getMessage());
			}
			//System.out.println("Number of Files in folder :" + total);
			
		}
		public static boolean classify(String str)
	     {				
	        double ham;
	        double spam;	        
	        HashSet<String> msgWords = new HashSet<String>();
	        StringTokenizer token = new StringTokenizer(str);	       
	        while(token.hasMoreTokens()) 
	        {
	            msgWords.add(token.nextToken());
	        }
	        spam=computeSpamProbability(msgWords,WeightedWords);	                
	        ham=computeHamProbability(msgWords,WeightedWords);

	        if(spam <=ham) return false;
	        return true;
	     }
		
		
		public static void trainMessages (File path, int category, double s)
	    {
	        try
	        {     
	            File[] files = path.listFiles();
	            double likelihood=0;
	            SNew = s;
	            for(File f :files)
	            {     
	                BufferedReader br = new BufferedReader(new FileReader(f));
	                String line = br.readLine();
	                while(line!=null)
	                {
	                    StringTokenizer token = new StringTokenizer(line);
					    while(token.hasMoreTokens())
	                    {
	                        String word = token.nextToken();
	                        if(!withStopwords){
	                        	if(word.compareTo("")!=0 && !stopWords.contains(word)){
	                        		WordOccurrences.add(word);
	                        	}
	                        }
	                        else{
	                        	 WordOccurrences.add(word);
	                        }
	                       
	                    }	
	                    line=br.readLine();						
	                }
	                likelihood = computeLikeliHood(WordOccurrences,WeightedWords,category);
	                S=SNew;
	                SNew += likelihood;	                
	                newWeight(WordOccurrences,WeightedWords,category);
	                WordOccurrences.clear();//clear before next message is read
	                br.close();	   
	            }            
	        }	
	        
	        catch(Exception e)
	        {
	            System.err.println("ERROR : "+e.getMessage());
	        }
	    }
		
		private static double computeLikeliHood(HashSet<String> WordOccurrences ,Map<String, Double> WeightedWords, int realcategory)
	    {   
	        double likelihood = (realcategory * Math.log(computeSpamProbability(WordOccurrences,WeightedWords)))+ ((1-realcategory)*Math.log(computeHamProbability(WordOccurrences,WeightedWords)))- (0.1 *computeSqrtWeight(WeightedWords)) ; 	        
	        return likelihood;
	    }
		
		private static double computeSqrtWeight(Map<String, Double> WeightedWords)
	    {        
	        Set<Entry<String, Double>> s = WeightedWords.entrySet();         
	        double weight=0;
	        Iterator<Entry<String, Double>> it1=s.iterator();   
	        while(it1.hasNext())
	        {
	            Entry<String, Double> m =it1.next();
	            String w= m.getKey();
	            double wt=WeightedWords.get(w);
	            weight += Math.pow(wt,2);
	        }
	        return Math.sqrt(weight);
	    }
		
		private static double computeSpamProbability(HashSet<String> WordOccurrences,Map<String, Double> WeightedWords)
	    {
	        double SpamProbability= 1/(1+Math.exp(-computeInnerProduct(WordOccurrences,WeightedWords)));	        
	        return SpamProbability;
	    }
		private static double computeHamProbability(HashSet<String> WordOccurrences,Map<String, Double> WeightedWords)
	     {
	        double HamProbability=Math.exp(-computeInnerProduct(WordOccurrences,WeightedWords))/(1+Math.exp(-computeInnerProduct(WordOccurrences,WeightedWords)));     	        
	        return HamProbability;
	     }
		private static double computeInnerProduct(HashSet<String> WordOccurrences,Map<String, Double> WeightedWords)
	    {
	        double sum=0;
	        Iterator<String> it1=WordOccurrences.iterator();	        
	        while(it1.hasNext())
	        {
	           String w= it1.next();
	           
	           if(WeightedWords.containsKey(w))
	           {
	                double myweight = WeightedWords.get(w);           
	                sum += myweight;   
	           }          
	        }	         
	        return sum;
	    }
		
	     private static void newWeight(HashSet<String> WordOccurrences,Map<String, Double> WeightedWords,int category)
	     {
	        HashSet<String>  myHash = WordOccurrences;	        
	        double new_weight;
	        Iterator<String> it1=myHash.iterator();	     
	        while(it1.hasNext())
	        {
	            String w= it1.next();
	            double myweight=WeightedWords.get(w);	            
	            new_weight= myweight+ 0.1 *(category-computeSpamProbability(myHash,WeightedWords));
	            //System.out.println(new_weight);
	            WeightedWords.remove(w);
	            WeightedWords.put(w, new_weight);
	        }
	     }
				
		public static void train(){
			String word = "";
			for(int i=0;i <FeatureRepository.size();i++)
	        {
	            word = FeatureRepository.get(i);
	            if(withStopwords){
	            	 WeightedWords.put(word,1.0);
         	   }
         	   else{
         		   if(word.compareTo("")!=0 && !stopWords.contains(word)){
         			  WeightedWords.put(word,1.0);
         		   }
         	   }	           
	        }			
		}
		
		public static void readFiles(File path,boolean withStopWOrds)
	    {
	    	try
	    	{		            
	            File[] files = path.listFiles();				
	            for(File f :files)
	            {
	                BufferedReader br = new BufferedReader(new FileReader(f));
	                String line = br.readLine();
	                while(line!=null)
	                {
	                    StringTokenizer content = new StringTokenizer(line);
	                    while(content.hasMoreTokens())
	                    {
	                        String word = content.nextToken().toString();
	                       int pos = FeatureRepository.indexOf(word);						
	                       if(pos==-1) 
	                       {         
	                    	   if(withStopWOrds){
	                    		   FeatureRepository.add(word);
	                    	   }
	                    	   else{
	                    		   if(word.compareTo("")!=0 && !stopWords.contains(word)){
	                    			   FeatureRepository.add(word);
	                    		   }
	                    	   }	                           
	                       }	                      
	                    }							
	                    line=br.readLine();						
	                }				
	                br.close();
	            }
	        }	
	    	catch(Exception e)
	        {
	            System.err.println("ERROR : "+e.getMessage());
	        }
	    }	   

		//loads the list of all stop words
		public static void loadStopWords(){
						
stopWords.addAll(Arrays.asList("a","about","above","after","again","against","all","am","an","and","any","are",
"aren\'t","as","at","be","because","been","before","being","below","between","both","but",
"by","can\'t","cannot","could","couldn\'t","did","didn\'t","do","does","doesn\'t","doing","don\'t",
"down","during","each","few","for","from","further","had","hadn\'t","has","hasn\'t","have","haven\'t","having",
"he","he\'d","he\'ll","he\'s","her","here","here\'s","hers","herself","him","himself","his","how","how\'s","i",
"i\'d","i\'ll","i\'m","i\'ve","if","in","into","is","isn\'t","it","it\'s","its","itself","let\'s","me","more","most","mustn\'t",
"my","myself","no","nor","not","of","off","on","once","only","or","other","ought","our","ours","ourselves",
"out","over","own","same","shan\'t","she","she\'d","she\'ll","she\'s","should","shouldn\'t","so","some","such","than",
"that","that\'s","the","their","theirs","them","themselves","then","there","there\'s","these","they","they\'d",
"they\'ll","they\'re","they\'ve","this","those","through","to","too","under","until","up","very","was","wasn\'t","we",
"we\'d","we\'ll","we\'re","we\'ve","were","weren\'t","what","what\'s","when","when\'s","where","where\'s","which",
"while","who","who\'s","whom","why","why\'s","with","won\'t","would","wouldn\'t","you","you\'d","you\'ll","you\'re",
"you\'ve","your","yours","yourself","yourselves"));

		}

}
