package geneticsPackage;
import java.util.Random;
import java.io.IOException;
import java.util.ArrayList;

public class Scheduler {

	private Genes genes = new Genes();
	private Random rand = new Random();
	private WriteToFile data = new WriteToFile("schedules.txt", true);
	private double[] fitnessArray = new double[500];
	private int fitnessIndex;
	private double fittest = 0;
	private double[] fitnessProbabilities;
	private int indexOfFittest = 0;
	//private ArrayList<Double> pastFitnessScores = new ArrayList<Double>();
	
	//this is the big list of the current read-only parent generation, hurray
	private ArrayList<String[][]> generation = new ArrayList<String[][]>();
	//the child generation
	private ArrayList<String[][]> nextGen = new ArrayList<String[][]>();
	private int generationID = 0;
	//and this is the read-only list that contains the previous generations
	private ArrayList<ArrayList<String[][]>> pool = new ArrayList<ArrayList<String[][]>>();
	
	private WriteToFile children = new WriteToFile("children.txt", true);
	
	
	public Scheduler() {
		
		//Create a random gene pool of about 1000 genes
		//why 1,000? not sure, guess large genetic diversity
		for(int i = 0; i < 1000; i++) {
			genes.createRandomGene();
		}
		fitnessIndex = 0;
		
		//Create 500 schedules with the 1000 genes - initial generation
		for(int i = 0; i < 500; i++)
		{
			String[][] schedule = generateSchedule();
			fitness(schedule);
			//add it to generation 0
			generation.add(schedule);
			try {
				data.writeFile(toString(generateSchedule()) + "\n \n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		pool.add(generation);
		generationID++;
		
		//initial loop - 250 parents and 500 children
		for(int i = 0; i < 250; i++)
		{
			//create a new fitness array to process 500 children
			for(int j = 0; j < 500; j++)
			{
				fitnessArray[j] = 0;
			}
			
			//Okay so for this first generation there isn't really a fitness array yet, so we are gonna choose
			//parents at random
			int parent1 = rand.nextInt(generation.size());
			int parent2 = rand.nextInt(generation.size());
			
			//i'm too scared to put these writes into that bigass loop
			try {
				children.writeFile("PARENT 1  \n" + toString(generation.get(parent1)));
				children.writeFile("PARENT 2  \n" + toString(generation.get(parent2)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			genes.createCrossoverWithCompleteScheule(generation.get(parent1), generation.get(parent2));
			try {
				children.writeFile("CHILD 1 \n " + toString(genes.assembleFirstChild()));
				children.writeFile("CHILD 2  \n" + toString(genes.assembleSecondChild()) + "\n \n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(fitnessIndex < 499)
			{
				nextGen.add(genes.assembleFirstChild());
				fitness(genes.assembleFirstChild());
				nextGen.add(genes.assembleSecondChild());
				fitness(genes.assembleSecondChild());
			}
			//one of the kids died in childbirth (index out of bounds error handling)
			else if(fitnessIndex == 499)
			{
				nextGen.add(genes.assembleFirstChild());
				fitness(genes.assembleFirstChild());
			}
		}
		
		fitnessProbabilities = fitnessOfGeneration();
		fittest = 0;
		
		for(int i = 0; i < 500; i++)
		{
			if(fitnessProbabilities[i] > fittest)
			{
				fittest = fitnessProbabilities[i];
				indexOfFittest = i;
			}
		}
		System.out.println("MAX FITNESS OF GENERATION " + generationID + " " + fittest);
		//reinitialize the generations
		pool.add(generation);
		generationID++;
		generation = nextGen;
		//nextGen.clear();
		//pastFitnessScores.add(fittest);
		
		for(int i = 0; i < 100; i++)
		{
			generationalLoop();
		}
		
		//while(pastFitnessScores.get(pastFitnessScores.size() - 1)
		//		.compareTo(pastFitnessScores.get(pastFitnessScores.size())) < 0.01)
		//{
		//	generationalLoop();
		//}
		
		WriteToFile fittestOutput = new WriteToFile("fittestSchedule.txt", true);
		try {
			fittestOutput.writeFile("FITTEST SCHEDULE OF GENERATION " + generationID + " WITH A FITNESS SCORE OF " + fittest
					+ '\n' + toString(generation.get(indexOfFittest)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void generationalLoop()
	{
		//only reproducing 250 times because it creates two children each (totals 500) (hopefully)
		for(int i = 0; i < 250; i++)
		{
			//create a new fitness array to process 500 children
			for(int j = 0; j < 500; j++)
			{
				fitnessArray[j] = 0;
			}
			
			int indexOfParent1 = 0;
			int indexOfParent2 = 0;
			
			indexOfParent1 = rand.nextInt(250);
			indexOfParent2 = rand.nextInt(250);
			
			//since the fitness score isn't working, we're going with this
			genes.createCrossoverWithCompleteScheule(generation.get(indexOfParent1), generation.get(indexOfParent2));
			
			
			// old method seen below
			// I used this source to help me because I was lost 
			// https://stackoverflow.com/questions/9330394/how-to-pick-an-item-by-its-probability?
			
			//double parent1Prob = rand.nextDouble(250);
			//double parent2Proba = rand.nextDouble(250);
			//double cumulativeProbability = 0;
			
			//for(int k = 0; k < 250; k++) {
			// 	cumulativeProbability += fitnessProbabilities[k];
			//	if(cumulativeProbability > parent1Prob)
			//		indexOfParent1 = k;
			// else if(cumulativeProbability > parent2Prob && k != indexOfParent1)
			//		indexOfParent2 = k;
			//}
			
			if(fitnessIndex < 499)
			{
				nextGen.add(genes.assembleFirstChild());
				fitness(genes.assembleFirstChild());
				nextGen.add(genes.assembleSecondChild());
				fitness(genes.assembleSecondChild());
			}
			//one of the kids died in childbirth (index out of bounds error handling)
			else if(fitnessIndex == 499)
			{
				nextGen.add(genes.assembleFirstChild());
				fitness(genes.assembleFirstChild());
			}
		}
		
		fitnessProbabilities = fitnessOfGeneration();
		fittest = 0;
		for(int i = 0; i < 500; i++)
		{
			if(fitnessProbabilities[i] > fittest)
			{
				fittest = fitnessProbabilities[i];
				indexOfFittest = i;
			}
		}
		System.out.println("MAX FITNESS OF GENERATION " + generationID + " " + fittest);
		//reinitialize the generations
		pool.add(generation);
		generationID++;
		generation = nextGen;
		//nextGen.clear();
		//pastFitnessScores.add(fittest);
	}
	
	//generates a schedule
	public String[][] generateSchedule()
	{
		String[][] schedule = new String[11][4];
		insertAllClasses(schedule);
		
		return schedule;
	}
	
	//inserts in a random instance of each activity -- meaning there will be NO repeated activities
	//note - I named the activities classes, I don't know why but class will from here on be activities
	public void insertAllClasses(String[][] schedule)
	{
		String[] classes = {"SLA100A", "SLA100B", "SLA191A", "SLA191B", "SLA201",
				"SLA291", "SLA303", "SLA304", "SLA394", "SLA449", "SLA451"};
		for(int i = 0; i < 11; i++)
		{
			ArrayList<Integer> classesByIndex = new ArrayList<Integer>();
			classesByIndex = genes.findGeneByClass(classes[i]);
			if(classesByIndex.size() > 1)
			{
				int indexOfClass = rand.nextInt(classesByIndex.size());
				
				schedule[i] = genes.getGene(classesByIndex.get(indexOfClass));
			}
		}
		
	}
	
	//outputs a schedule instance to a string -- probably a better way to do this I'm just stupid
	public String toString(String[][] schedule) {
		String fixedSchedule = "";
		for(int i = 0; i < 11; i++)
		{
			if(i == 10)
			{
				for(int j = 0; j < 4; j++)
				{
					if(j == 3)
					{
						fixedSchedule += schedule[i][j];
						break;
					}
					else {
						fixedSchedule += schedule[i][j] + " - ";
					}
				}
				break;
			}
			else 
			{
				for(int j = 0; j < 4; j++)
				{
					if(j == 3)
					{
						fixedSchedule += schedule[i][j];
						break;
					}
					else {
						fixedSchedule += schedule[i][j] + " - ";
					}
				}
			}
			fixedSchedule += '\n';
		}
		return fixedSchedule;
	}
	
	//something is wrong in here, and I don't know what
	public double fitness(String[][] schedule)
	{
		double fitness = 0;
		//for each schedule
		for(int i = 0; i < 11; i++)
		{
			String className = schedule[i][0];
			String instructor = schedule[i][1];
			String room = schedule[i][2];
			
			//FITNESS - CALCULATE INSTRUCTOR VALUE
			if(genes.preferredFac(className, instructor))
			{
				fitness = fitness +  0.5;
			}
			else if(genes.otherFac(className, instructor))
			{
				fitness = fitness + 0.2;
			}
			else if(!genes.preferredFac(className, instructor) && !genes.otherFac(className, instructor))
			{
				fitness = fitness - 0.1;
			}
			
			//FITNESS - CALCULATE ROOM SIZE 
			fitness = genes.sizeOfRoom(className, room, fitness);
			
			//The rest of the fitness calculations
			fitness = genes.facilitatorLoad(fitness, schedule);
			
			genes.specificAdjustments(fitness, schedule);
			if(fitnessIndex < 499)
			{
				fitnessArray[fitnessIndex] = fitness;
				fitnessIndex++;
			}
		}
		
		//used in testing
		//System.out.println(fitness);
		return fitness;
	}
	
	public double[] fitnessOfGeneration()
	{
		return softmax(fitnessArray);
	}
	
	public double[] softmax(double[] fitnessScores) {
		 
		double[] distribution = new double[fitnessScores.length];
		double sum = 0;
		
		for(int i = 0; i < fitnessScores.length; i++)
		{
			distribution[i] = Math.exp(fitnessScores[i]);
			sum += distribution[i];
		}
		
		for(int i = 0; i < distribution.length; i++)
		{
		distribution[i] /= sum;
		}
		
		return distribution;
	}
	
	public static void main(String[] args) {
		Scheduler schedule = new Scheduler();
		
		//okay so clearly softmax works, it's the fitness function that's an issue
		double[] array = {2.9, 0.5, 4.2};
		schedule.softmax(array);
	}
}
