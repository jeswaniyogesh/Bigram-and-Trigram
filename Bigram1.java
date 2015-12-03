//package NLP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bigram1 {
	static String corpus=" ";
	static String corpus1=" ";
	static double p;
	static int[][] CMatrix;
	static double[] CUMatrixGT;
	static int[] CMatrix1;
	static double[][] PMatrix;
	static double[][] PMatrixGT;
	static double[] UMatrix;
	static HashMap<String, Integer> v;
	
	static double[][] CMatrixGT;
	static double[] PUMatrixGT;
	static int[] frequency;
	static HashMap<String, Integer> w = new HashMap<>();
	static int[] freq;
	static int totalBigrams;
	static int[] Ufreq;
	static HashMap<String, Integer> u = new HashMap<>();
	static int totalunigrams;
	
	static void UsetFrequency(){
		u = new HashMap<>();
		String[] words = corpus.split(" ");
		int maxFreq = 0;
		for(int i=1; i<words.length; i++){
			if(u.containsKey(words[i])){
				int a = u.get(words[i]);
				a++;
				u.put(words[i], a);
				if(a>maxFreq)
					maxFreq = a;
			}
			else{
				u.put( words[i], 1);
				if(1>maxFreq)
					maxFreq = 1;
			}
		}
		totalunigrams = words.length -1;
		//System.out.println("Total unigrams"+totalunigrams);
		freq = new int[maxFreq+1];
		for(int i=0; i<freq.length; i++)
			freq[i]=0;
		
		for (Map.Entry<String, Integer> entry : u.entrySet()) {
		    freq[entry.getValue()]++;
		}
		
		for(int j=0;j<freq.length;j++){
			//System.out.println(freq[j]);
		}
		//System.out.println("freq is "+ freq.length);
	}
	
	
    static void setFrequency(){
		w = new HashMap<>();
		String[] words = corpus.split(" ");
		int maxFreq = 0;
		for(int i=2; i<words.length; i++){
			if(w.containsKey(words[i-1]+ " " + words[i])){
				int a = w.get(words[i-1] + " " + words[i]);
				a++;
				w.put(words[i-1] + " " + words[i], a);
				if(a>maxFreq)
					maxFreq = a;
			}
			else{
				w.put(words[i-1] + " " + words[i], 1);
				if(1>maxFreq)
					maxFreq = 1;
			}
		}
		totalBigrams = words.length -1;
		//System.out.println("Total biagrams"+totalBigrams);
		freq = new int[maxFreq+1];
		for(int i=0; i<freq.length; i++)
			freq[i]=0;
		
		for (Map.Entry<String, Integer> entry : w.entrySet()) {
		    freq[entry.getValue()]++;
		}
		
		for(int j=0;j<freq.length;j++){
			//System.out.println(freq[j]);
		}
		//System.out.println("freq is "+ freq.length);
	}
	
	
	static double bigramWithGoodTuring(String sentence){
		setFrequency();
		
        
        String[] words = sentence.split(" ");
       // System.out.println("sentence length"+ words.length);
        int count1;
        double count2=0;
        
        CMatrixGT = new double[words.length][words.length];
        PMatrixGT= new double[words.length][words.length];
        
        for(int i=1; i<words.length; i++){
        	for(int j=1; j<words.length; j++){
        		String q = words[i] + " " + words[j];
	        	
        		if(w.containsKey(q)){
        			count1 = w.get(q);
        			//System.out.println(count1);
        			if(count1==freq.length-1){
        				//count2 = (double)((count1) * freq[count1]) / (double)freq[count1];
        				CMatrixGT[i][j] = (double)freq[1]/(double)totalBigrams;
    	        		PMatrix[i][j] = (double)freq[1]/(double)totalBigrams;
        			}else
	        		count2 = (double)((count1+1) * freq[count1+1]) / (double)freq[count1];
	        		CMatrixGT[i][j] = count2;
	        		PMatrix[i][j] = count2/totalBigrams;
        		}
        		else{
        			CMatrixGT[i][j] = (double)freq[1]/(double)totalBigrams;
	        		PMatrix[i][j] = (double)freq[1]/(double)totalBigrams;
        		}
        	}
        }
        double p=0;
        for(int i=2; i<words.length; i++){
        	if(PMatrix[i-1][i]!=0)
        		p += Math.log(PMatrix[i-1][i]);
        }
        //System.out.println(p);
        return p;
	}
	
	
	static double unigramWithGoodTuring(String sentence){
		UsetFrequency();
		
        
        String[] words = sentence.split(" ");
       //System.out.println("sentence length"+ words.length);
        int count1;
        double count2=0;;
        
        CUMatrixGT = new double[words.length];
        PUMatrixGT = new double[words.length];
        
        for(int i=1; i<words.length; i++){
        	
        		String q = words[i];
	        	
        		if(u.containsKey(q)){
        			count1 = u.get(q);
        			//System.out.println(count1);
        			if(count1==freq.length-1){
        				//count2 = (double)((count1) * freq[count1]) / (double)freq[count1];
        				CUMatrixGT[i] = (double)freq[1]/(double)totalBigrams;
    	        		PUMatrixGT[i] = (double)freq[1]/(double)totalBigrams;
        			}else
	        		count2 = (double)((count1+1) * freq[count1+1]) / (double)freq[count1];
	        		CUMatrixGT[i] = count2;
	        		PUMatrixGT[i] = count2/totalBigrams;
        		}
        		else{
        			CUMatrixGT[i] = (double)freq[1]/(double)totalBigrams;
	        		PUMatrixGT[i] = (double)freq[1]/(double)totalBigrams;
        		}
        	}
        
        double p=0;
        for(int i=2; i<words.length; i++){
        	if(PUMatrixGT[i-1]!=0)
        		p += Math.log(PUMatrixGT[i-1]);
        }
        //System.out.println(p);
        return p;
	}
	
	
	static String readFile(String path) throws IOException{
		
		BufferedReader br=new BufferedReader(new FileReader(path));
		//StringBuilder sb=new StringBuilder();
		String line=br.readLine().replaceAll("\\s+","");
		
		corpus=corpus+line;
		corpus1=corpus1+line;
		
		while((line=br.readLine())!=null){
			corpus=corpus+" "+line;
	        corpus1=corpus1+"  "+line;
	       }
		
		System.out.println(corpus1);
		String [] words= corpus.split(" ");
		v = new HashMap<>();
	    for(int i=1; i<words.length; i++)
	    	v.put(words[i], 1);
		
	    
	    for(int j=1;j<words.length;j++){
	    	//System.out.println(words[j]);
	    }
	    
	    return corpus;
	}
	
	
static String readFile1(String path) throws IOException{
	
	BufferedReader br=new BufferedReader(new FileReader(path));
	//StringBuilder sb=new StringBuilder();
	String line=br.readLine().replaceAll("\\s+","");
	
	String sent=" "+line;
	
	while((line=br.readLine())!=null){
		
        //line = br.readLine();
        sent=sent+" "+line;
        
    }
	
    //System.out.println(sent);
    
    return sent;
}

static int countreg(String patternText){
	String[] words = patternText.split(" ");
	int count=0;
	String[]corp=corpus.split(" ");
	
	for(int i=1;i<corp.length-1;i++){
		
		if(corp[i].equals(words[0])&& corp[i+1].equals(words[1])){
			count++;
		}
	}
	
	return count;
}

static int countreg1(String patternText){
	String[] words = patternText.split(" ");
	int count=0;
	String[]corp=corpus.split(" ");
	
	for(int i=1;i<corp.length;i++){
		
		if(corp[i].equals(words[0])){
			count++;
		}
	}
	
	return count;
}

public static double bigramWithoutSmoothing(String sentence){
	p=0;
	String[] words = sentence.split(" ");
	
	//System.out.println(words.length);
	
	 
     for(int i=1;i<words.length;i++){
    	// System.out.println(words[i]);
     }
    int count1, count2;
    String pattern;
    
    CMatrix = new int[words.length][words.length];
    CMatrix1 = new int[words.length];
    PMatrix = new double[words.length][words.length];
    UMatrix=new double[words.length];
    String word1, word2;
    
    
    for(int i=1; i<words.length; i++){
    	for(int j=1; j<words.length; j++){
            
    		//word1 = " " + words[i] + " ";
    		word1=words[i]+" ";
    		word2 = "" + words[j] + "";
    		pattern=word1+word2;
    		//System.out.println(pattern);
    		count1 = countreg(pattern);
    		
    		//System.out.println(count1);
    		word1=words[i];
    		pattern=word1;
    		
    		count2=countreg1(pattern);
    		//System.out.println(pattern+ " "+count2);
    		
    		if(count1!=0 && count2!=0){
        		
        		p = (double)count1/(double)count2;
        	}
        	else{
        		
        		p=0;
        	}
        	CMatrix[i][j] = count1;
        	CMatrix1[i] = count2;
        	PMatrix[i][j] = p;
        	
        	if(count2!=0)
    		UMatrix[i]=(double)count2/(words.length-1);
        	else UMatrix[i]=0;
    		
    	}
    }
    
    for(int i=2; i<words.length; i++){
    	if(PMatrix[i-1][i] != 0)
    		p = p + Math.log(PMatrix[i-1][i]);
    }
	return p;
	
	
}
	

public static void printArray1(double[] arr,String sentence){
	
	String[] words = sentence.split(" ");
	for(int i=1; i<arr.length; i++){
		System.out.println(words[i]+" "+ arr[i] + "  ");
	}
}


public static void printArray(int[] arr,String sentence){
	
	String[] words = sentence.split(" ");
	for(int i=1; i<arr.length; i++){
		System.out.println(words[i]+" "+ arr[i] + "  ");
	}
}

public static void printArray(int[][] arr,String sentence){
	String[] words = sentence.split(" ");
	for(int i=1; i<arr.length; i++){
		for(int j=1; j<arr.length; j++)
		System.out.println(words[i]+" "+ words[j]+" "+arr[i][j] + "  ");
		
	}
}


public static void printArray(double[][] arr,String sentence){
	String[] words = sentence.split(" ");
	for(int i=1; i<arr.length; i++){
		for(int j=1; j<arr.length; j++)
			System.out.println(words[i]+" "+ words[j]+" "+arr[i][j] + "  ");
		
	}
}

public static double output(String sentence){
	//System.out.println("SENTENCE: " + sentence);
	
	
	p = bigramWithoutSmoothing(sentence);
	
	/*System.out.println("\n------------------------Unigram Count Matrix------------------------\n");
	printArray(CMatrix1,sentence);
	System.out.println("\n------------------------End: UnigramCount Matrix------------------------\n");
    
    System.out.println("\n------------------------Bigram Count Matrix------------------------\n");
    printArray(CMatrix,sentence);
    System.out.println("\n------------------------End: Bigram Count Matrix------------------------\n");*/
    
    System.out.println("------------------------Bigram Probability Matrix without Good Turing------------------------\n");
    printArray(PMatrix,sentence);
    System.out.println("\n------------------------End: Bigram Probability Matrix without Good Turing------------------------\n");
    
    System.out.println("------------------------Unigram Probability Matrix without Good Turing------------------------\n");
    printArray1(UMatrix,sentence);
    System.out.println("\n------------------------End: Unigram Probability Matrix withoug Good Turing------------------------\n");
    
    
	
	return p;
	
}



public static double outputGoodTuring(String sentence){
	double p = bigramWithGoodTuring(sentence);
	unigramWithGoodTuring(sentence);
    
	//System.out.println("SENTENCE: " + sentence);
	
    System.out.println("_________________________________________________BIGRAM WITH GOOD TURING SMOOTHING_________________________________________________");
	
   /* System.out.println("\n------------------------Bigram Count Matrix------------------------\n");
    printArray(CMatrixGT,sentence);
    System.out.println("\n------------------------End: Bigram Count Matrix------------------------\n");*/
    
    System.out.println("\n------------------------Bigram Probability Matrix with Good Turing------------------------\n");
    printArray(PMatrixGT,sentence);
    System.out.println("\n------------------------End: Bigram Probability Matrix with Good Turing------------------------\n");
    
   /* System.out.println("\n------------------------Unigram Count Matrix------------------------\n");
    printArray1(CUMatrixGT,sentence);
    System.out.println("\n------------------------End: Unigram Count Matrix------------------------\n");*/
    
    System.out.println("\n------------------------Unigram Probability Matrix with Good Turing------------------------\n");
    printArray1(PUMatrixGT,sentence);
    System.out.println("\n------------------------End: Unigram Probability Matrix with Good Turing------------------------\n");
    
    
    
	return p;
}
	
	public static void main(String[] args) throws IOException {
		
		
		//String filename="C:/MyJava/UTDNEW/src/NLP/corpus1.txt";
       readFile(args[0]); 
		
		
		//readFile(args[0]);
        String sentence;
        Double p=0.0;
        
       // System.out.println("Unseen corpus");
        sentence = readFile1(args[0]);
       // System.out.println(sentence);
        output(sentence);
       outputGoodTuring(sentence);
       // UsetFrequency();
	
	}
	
}
