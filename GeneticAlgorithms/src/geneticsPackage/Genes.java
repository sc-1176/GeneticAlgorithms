package geneticsPackage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Genes {
	
	private String[] activities = {"SLA100A", "SLA100B", "SLA191A", "SLA191B", "SLA201",
			"SLA291", "SLA303", "SLA304", "SLA394", "SLA449", "SLA451"};
	
	
	private String[] facilitators = {"Lock", "Glen", "Banks", "Richards", "Shaw", "Singer",
			"Uther", "Tyler", "Numen", "Zeldin"};
	
	
	private String[] rooms = {"Slater 003", "Roman 216", "Loft 206", "Roman 201", "Loft 310",
			"Beach 201", "Beach 301", "Logos 325", "Frank 119"};
	
	
	private String[] times = {"10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM"};
	
	
	private String[] capacity = {"45", "30", "75", "50", "108", "60", "75", "450", "60"};
	
	
	private String[] enrollment = {"50", "50", "50", "50", "50", "50", "60", "25", "20", "60", "100"};
	
	private String[] child = new String[4];
	
	private String[] secondChild = new String[4];
	
	private String[][] childPhenotype = new String[11][4];
	
	private String[][] secondChildPhenotype = new String[11][4];
	
	private Random rand = new Random();
	
	//500 members of a generation, each with 4 elements
	private String[][] generation;
	
	private int geneID;
	
	private double mutationRate = 0.01;
	
	private WriteToFile data = new WriteToFile("output.txt", true);
	
	public Genes() {
		generation = new String[1000][4];
		geneID = 0;
	}
	
	public String[] createRandomGene()
	{
		String[] gene = {"activity", "facilitator", "room", "time"};
		
		int activityIndex = rand.nextInt(activities.length);
		gene[0] = setActivity(activityIndex);
		
		int facilitatorIndex = rand.nextInt(facilitators.length);
		gene[1] = setFacilitator(facilitatorIndex);
		
		int roomIndex = rand.nextInt(rooms.length);
		gene[2] = setRoom(roomIndex);
		
		int timeIndex = rand.nextInt(times.length);
		gene[3] = setTime(timeIndex);
		
		try {
			data.writeFile(toString(gene));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(geneID == 0)
		{
			generation[0] = gene;
			geneID++;
		}
		else
		{
			generation[geneID] = gene;
			geneID++;
		}
		
		return gene;
	}
	
	//crosses over two individual genes
	public void crossoverGenes(String[] mother, String[] father)
	{
		//create child instances
		String[] child1 = {"activity", "facilitator", "room", "time"};
		String[] child2 = {"activity", "facilitator", "room", "time"};
		//predetermine whether or not we will mutate
		double mutationOfChild1 = rand.nextDouble(1);
		double mutationOfChild2 = rand.nextDouble(1);
		
		//System.out.println("MOTHER - " + toString(mother));
		//System.out.println("FATHER - " + toString(father));
		
		//choose a random crossover point
		int crossoverPoint = rand.nextInt(4);
		
		//switch the genes of the mother and father up
		for(int i = 0; i < crossoverPoint; i++)
		{
			child1[i] = mother[i];
			child2[i] = father[i];
		}
		for(int i = crossoverPoint; i < 4; i++)
		{
			child1[i] = father[i];
			child2[i] = mother[i];
		}
		
		// decide if a child should be mutated or not, if so, choose what to mutate
		if(mutationOfChild1 == mutationRate)
		{
			mutate(child1, rand.nextInt(4));
		}
		if(mutationOfChild2 == mutationRate)
		{
			mutate(child1, rand.nextInt(4));
		}
		
		//add them here so they can be added to the phenotype later
		child = child1;
		secondChild = child2;
		
		//System.out.println("CHILD 1 - " + toString(child1));
		//System.out.println("CHILD 2 - " + toString(child2));

	}
	
	// a bit primitive -- does first line x first line, second line x second line, etc
	// not the greatest for diversity but it will crossover two phenotypes reliably
	public void createCrossoverWithCompleteScheule(String[][] mother, String[][] father)
	{
		String[] motherGenes = new String[4];
		String[] fatherGenes = new String[4];
		for(int i = 0; i < 11; i++)
		{
			motherGenes[0] = mother[i][0];
			motherGenes[1] = mother[i][1];
			motherGenes[2] = mother[i][2];
			motherGenes[3] = mother[i][3];
			
			fatherGenes[0] = father[i][0];
			fatherGenes[1] = father[i][1];
			fatherGenes[2] = father[i][2];
			fatherGenes[3] = father[i][3];
			
			crossoverGenes(motherGenes, fatherGenes);
			
			childPhenotype[i] = child;
			secondChildPhenotype[i] = secondChild;
		}
	}
	
	public String[][] assembleFirstChild()
	{
		return childPhenotype;
	}
	
	public String[][] assembleSecondChild()
	{
		return secondChildPhenotype;
	}
	
	//mutate the little bastards by randomizing the index of their mutation
	public void mutate(String[] gene, int indexOfMutation)
	{
		if(indexOfMutation == 0)
		{
			gene[0] = setActivity(rand.nextInt(activities.length));
		}
		else if(indexOfMutation == 1)
		{
			gene[1] = setFacilitator(rand.nextInt(facilitators.length));
		}
		else if(indexOfMutation == 2)
		{
			gene[2] = setRoom(rand.nextInt(rooms.length));
		}
		else if(indexOfMutation == 3)
		{
			gene[3] = setTime(rand.nextInt(times.length));
		}
	}
	
	//setters
	public String setActivity(int i) 
	{
		return activities[i];
	}
	
	public String setFacilitator(int i)
	{
		return facilitators[i];
	}
	
	public String setTime(int i)
	{
		return times[i];
	}
	
	public String setRoom(int i)
	{
		return rooms[i];
	}

	//getters
	public String getCapacity(String room)
	{
		int index = 0;
		for(int i = 0; i < rooms.length; i++)
		{
			if(rooms[i].equalsIgnoreCase(room))
			{
				index = i;
			}
		}
		return capacity[index];
	}
	
	public String getEnrollment(String activity)
	{
		int index = 0;
		for(int i = 0; i < activities.length; i++)
		{
			if(activities[i].equalsIgnoreCase(activity))
			{
				index = i;
			}
		}
		return enrollment[index];
	}
	
	//display function
	public String toString(String[] gene)
	{
		String fixedGene = "";
		for(int i = 0; i < 4; i++)
		{
			if(i == 3)
			{
				fixedGene += gene[i];
				break;
			}
			fixedGene += gene[i] + " - ";
		}
		return fixedGene;
	}
	
	//another getter but this time it uses the index of the gene in the generation
	public String[] getGene(int index)
	{
		String[] gene = {generation[index][0], generation[index][1], generation[index][2], generation[index][3]};
		return gene;
	}
	
	//finds a gene by whatever class it's in and passes it to an array list so the scheduler can use it in a randomizer
	public ArrayList<Integer> findGeneByClass(String className)
	{
		ArrayList<Integer> indexOfClasses = new ArrayList<Integer>();
		for(int i = 0; i < generation.length; i++)
		{
			if(generation[i][0].equalsIgnoreCase(className))
			{
				//this print function was used during testing
				//System.out.println(i + " - " + generation[i][0]);
				indexOfClasses.add(i);
			}
		}
		return indexOfClasses;
	}
	
	//fitness functions
	//returns true is passed the preferred facilitator for the activity
	public boolean preferredFac(String className, String faculty)
	{
		if(className.equals("SLA100A") || className.equals("SLA100B"))
		{
			if(faculty.equals("Glen") || faculty.equals("Lock") || faculty.equals("Banks") || faculty.equals("Zeldin")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA191A") || className.equals("SLA191B"))
		{
			if(faculty.equals("Glen") || faculty.equals("Lock") || faculty.equals("Banks") || faculty.equals("Zeldin")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA201")) {
			if(faculty.equals("Glen") || faculty.equals("Banks") || faculty.equals("Shaw") || faculty.equals("Zeldin")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA291")) {
			if(faculty.equals("Lock") || faculty.equals("Banks") || faculty.equals("Zeldin") || faculty.equals("Singer")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA303")) {
			if(faculty.equals("Glen") || faculty.equals("Zeldin") || faculty.equals("Banks")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA304")) {
			if(faculty.equals("Glen") || faculty.equals("Banks") || faculty.equals("Tyler")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA394")) {
			if(faculty.equals("Tyler") || faculty.equals("Singer"))
			{
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA449")) {
			if(faculty.equals("Tyler") || faculty.equals("Singer") || faculty.equals("Shaw")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA451")) {
			if(faculty.equals("Tyler") || faculty.equals("Singer") || faculty.equals("Shaw")) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	//returns true if passed acceptable facilitators for the activity
	public boolean otherFac(String className, String faculty) {
		
		if(className.equals("SLA100A") || className.equals("SLA100B"))
		{
			if(faculty.equals("Numen") || faculty.equals("Richards")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA191A") || className.equals("SLA191B"))
		{
			if(faculty.equals("Numen") || faculty.equals("Richards")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA201")) {
			if(faculty.equals("Numen") || faculty.equals("Richards") || faculty.equals("Singer")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA291")) {
			if(faculty.equals("Numen") || faculty.equals("Richards") || faculty.equals("Shaw") || faculty.equals("Tyler")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA303")) {
			if(faculty.equals("Numen") || faculty.equals("Singer") || faculty.equals("Shaw")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA304")) {
			if(faculty.equals("Numen") || faculty.equals("Singer") || faculty.equals("Shaw")
					||faculty.equals("Richards") || faculty.equals("Uther") || faculty.equals("Zeldin")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA394")) {
			if(faculty.equals("Richards") || faculty.equals("Zeldin"))
			{
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA449")) {
			if(faculty.equals("Zeldin") || faculty.equals("Uther") || faculty.equals("Shaw")) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(className.equals("SLA451")) {
			if(faculty.equals("Zeldin") || faculty.equals("Uther") || faculty.equals("Richards") || faculty.equals("Banks")) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	//checks to see the size of the room (created to prevent redundant code)
	public double sizeOfRoom(String className, String room, double fitness) {
		int indexOfRoom = 0;
		int indexOfClass = 0;
		for(int i = 0; i < rooms.length; i++)
		{
			if(rooms[i].equals(room))
			{
				indexOfRoom = i;
			}
		}
		for(int i = 0; i < activities.length; i++)
		{
			if(activities[i].equals(className))
			{
				indexOfClass = i;
			}
		}
		
		if(tooSmall(indexOfClass, indexOfRoom))
		{
			fitness = fitness - 0.5;
		}
		else if(tooBig(indexOfClass, indexOfRoom))
		{
			fitness = fitness - 0.2;
		}
		else if(excessivelyLarge(indexOfClass, indexOfRoom))
		{
			fitness = fitness - 0.4;
		}
		return fitness;
	}
	
	//room capacity too small
	public boolean tooSmall(int indexOfClass, int indexOfRoom) {
		int roomCapacity = Integer.parseInt(capacity[indexOfRoom]);
		int expectedEnrollment = Integer.parseInt(enrollment[indexOfClass]);
		
		if(roomCapacity < expectedEnrollment)
		{
			return true;
		}
		return false;
	}
	
	//room capacity > 3 times enrollment
	public boolean tooBig(int indexOfClass, int indexOfRoom) {
		int roomCapacity = Integer.parseInt(capacity[indexOfRoom]);
		int expectedEnrollment = Integer.parseInt(enrollment[indexOfClass]);
		
		if(roomCapacity > expectedEnrollment * 3)
		{
			return true;
		}
		
		return false;
	}
	
	//room capacity > 6 times enrollment
	public boolean excessivelyLarge(int indexOfClass, int indexOfRoom) {
		int roomCapacity = Integer.parseInt(capacity[indexOfRoom]);
		int expectedEnrollment = Integer.parseInt(enrollment[indexOfClass]);
		
		if(roomCapacity > expectedEnrollment * 6)
		{
			return true;
		}
		return false;
	}
	
	//calculates facilitator load
	public double facilitatorLoad(double fitness, String[][] schedule) {
		
		int[] facultyCount = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		 
		 String[] faculty = new String[11];
		 
		 for(int i = 0; i < 11; i++)
		 {
			 faculty[i] = schedule[i][1];
			 switch(schedule[i][1]) {
			 case "Lock" : facultyCount[0] += 1;
			 case "Glen" : facultyCount[1] += 1;
			 case "Banks" : facultyCount[2] += 1;
			 case "Richards" : facultyCount[3] += 1;
			 case "Shaw" : facultyCount[4] += 1;
			 case "Singer" : facultyCount[5] += 1;
			 case "Uther" : facultyCount[6] += 1;
			 case "Tyler" : facultyCount[7] += 1;
			 case "Numen" : facultyCount[8] += 1;
			 case "Zeldin" : facultyCount[9] += 1;
			 }
		 }
		 
		 //checking for facilitator imposters at separate classes at the same time
		 for(int i = 0; i < 11; i++)
		 {
			 for(int j = 0; j < 11; j++)
			 {
				 //same professor, but not the same class (j != i means we aren't the same class)
				 if(schedule[i][1].equals(schedule[j][1]) && j != i)
				 {
					 //same time (in two places at once)
					 if(schedule[i][3].equals(schedule[j][3]))
					 {
						 fitness = fitness - 0.2;
						 
					 }
					 //different times, only one class in the time slot
					 else 
					 {
						 fitness = fitness + 0.2;
						 
					 }
				 }
			 }
		 }
		 
		 
		 //too many and too few activities penalties
		 if(facultyCount[0] > 4)
			 fitness = fitness - 0.5;
		 else if(facultyCount[0] <= 2)
			 fitness = fitness - 0.4;
		 
		 if(facultyCount[1] > 4)
			 fitness = fitness - 0.5;
		 else if(facultyCount[1] <= 2)
			 fitness = fitness - 0.4;
		 
		 if(facultyCount[2] > 4)
			 fitness = fitness - 0.5;
		 else if(facultyCount[2] <= 2)
			 fitness = fitness - 0.4;
		 
		 if(facultyCount[3] > 4)
			 fitness = fitness - 0.5;
		 else if(facultyCount[3] <= 2)
			 fitness = fitness - 0.4;
		 
		 if(facultyCount[4] > 4)
			 fitness = fitness - 0.5;
		 else if(facultyCount[4] <= 2)
			 fitness = fitness - 0.4;
		 
		 if(facultyCount[5] > 4)
			 fitness = fitness - 0.5;
		 else if(facultyCount[5] <= 2)
			 fitness = fitness - 0.4;
		 
		 if(facultyCount[6] > 4)
			 fitness = fitness - 0.5;
		 else if(facultyCount[6] <= 2)
			 fitness = fitness - 0.4;
		 
		 //he's special so as long as he's 0,1, or 2 he's chill
		 //this is tyler by the way
		 if(facultyCount[7] > 2)
			 fitness = fitness - 0.5;
		 
		 if(facultyCount[8] > 4)
			 fitness = fitness - 0.5;
		 else if(facultyCount[8] <= 2)
			 fitness = fitness - 0.4;
		 
		 if(facultyCount[9] > 4)
			 fitness = fitness - 0.5;
		 else if(facultyCount[9] <= 2)
			 fitness = fitness - 0.4;
		 
		 //penalties for making your facilitators run (room penalties)
		 for(int i = 0; i < 11; i++)
		 {
			 for(int j = 0; j < 11; j++)
			 {
				 if(schedule[i][1].equals(schedule[j][1]) && i != j)
				 {
					 //if the rooms are far apart AND consecutive
					 if(roomsFarApart(schedule[i][2], schedule[j][2]) && consecutive(schedule[i][3], schedule[j][3]))
					 {
						fitness = fitness - 0.4; 
					 }
				 }
			 }
		 }
		 
		return fitness;
	}
	
	//specific adjustments that don't fit anywhere else
	public double specificAdjustments(double fitness, String[][] schedule) {
		//cheating? maybe
		//but it works because everything is in a specific order that doesn't change
		
		//both A and B taught at the same time
		if(schedule[0][3].equals(schedule[1][3]))
			fitness = fitness - 0.5;
		if(schedule[2][3].equals(schedule[3][3]))
			fitness = fitness - 0.5;
		
		//A and B are taught 4 hours apart
		if(fourHoursApart(schedule[0][3], schedule[1][3]))
		{
			fitness = fitness + 0.5;
		}
		if(fourHoursApart(schedule[2][3], schedule[3][3]))
		{
			fitness = fitness + 0.5;
		}
		
		
		//section of 109 and section of 191 at the same time
		if(schedule[0][3].equals(schedule[2][3]) || schedule[0][3].equals(schedule[3][3])
				|| schedule[1][3].equals(schedule[2][3]) || schedule[1][3].equals(schedule[3][3]))
			fitness = fitness - 0.25;
		
		//sections 191 and 101 are taught consecutively
		if(consecutive(schedule[0][3], schedule[2][3]))
		{
			fitness = fitness + 0.5;
			if(roomsFarApart(schedule[0][2], schedule[2][2]))
			{
				fitness = fitness - 0.9;
			}
		}
		if(consecutive(schedule[0][3], schedule[3][3]))
		{
			fitness = fitness + 0.5;
			if(roomsFarApart(schedule[0][2], schedule[3][2]))
			{
				fitness = fitness - 0.9;
			}
		}
		if(consecutive(schedule[1][3], schedule[2][3]))
		{
			fitness = fitness + 0.5;
			if(roomsFarApart(schedule[1][2], schedule[2][2]))
			{
				fitness = fitness - 0.9;
			}
		}
		if(consecutive(schedule[1][3], schedule[3][3]))
		{
			fitness = fitness + 0.5;
			if(roomsFarApart(schedule[1][2], schedule[3][2]))
			{
				fitness = fitness - 0.9;
			}
		}
		
		return fitness;
	}
	
	//checks to see if two times are >= 4 hours apart
	public boolean fourHoursApart(String time1, String time2)
	{
		if(time1.equals(times[0]) && time2.equals(times[4]) || time1.equals(times[4]) && time2.equals(times[0]))
		{
			return true;
		}
		else if(time1.equals(times[0]) && time2.equals(times[5]) || time1.equals(times[5]) && time2.equals(times[0]))
		{
			return true;
		}
		else if(time1.equals(times[1]) && time2.equals(times[5]) || time1.equals(times[5]) && time2.equals(times[1]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//checks to see if two times are 1 hour apart
	public boolean oneHourApart(String time1, String time2)
	{
		if(time1.equals(times[0]) && time2.equals(times[2]) || time1.equals(times[2]) && time2.equals(times[0]))
		{
			return true;
		}
		else if(time1.equals(times[1]) && time2.equals(times[3]) || time1.equals(times[3]) && time2.equals(times[1]))
		{
			return true;
		}
		else if(time1.equals(times[2]) && time2.equals(times[4]) || time1.equals(times[4]) && time2.equals(times[2]))
		{
			return true;
		}
		else if(time1.equals(times[3]) && time2.equals(times[5]) || time1.equals(times[5]) && time2.equals(times[3]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//checks to see if two times are consecutive
	public boolean consecutive(String time1, String time2)
	{
		if(time1.equals(times[0]) && time2.equals(times[1]) || time1.equals(times[1]) && time2.equals(times[0]))
		{
			return true;
		}
		else if(time1.equals(times[1]) && time2.equals(times[2]) || time1.equals(times[2]) && time2.equals(times[1]))
		{
			return true;
		}
		else if(time1.equals(times[2]) && time2.equals(times[3]) || time1.equals(times[3]) && time2.equals(times[2]))
		{
			return true;
		}
		else  if(time1.equals(times[3]) && time2.equals(times[4]) || time1.equals(times[4]) && time2.equals(times[3]))
		{
			return true;
		}
		else  if(time1.equals(times[4]) && time2.equals(times[5]) || time1.equals(times[5]) && time2.equals(times[4]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//checks to see if the rooms are far apart
	public boolean roomsFarApart(String room1, String room2)
	{
		if(room1.equals(rooms[1]) || room1.equals(rooms[3]) || room1.equals(rooms[5])
				|| room1.equals(rooms[6]))
		{
			if(!room2.equals(rooms[1]) || !room2.equals(rooms[3]) || !room2.equals(rooms[5])
					|| !room2.equals(rooms[6]))
			{
				return true;
			}
			if(!room2.equals(rooms[1]) || !room2.equals(rooms[3]) || !room2.equals(rooms[5])
					|| !room2.equals(rooms[6]))
			{
				return true;
			}
		}
		return false;
	}
}
