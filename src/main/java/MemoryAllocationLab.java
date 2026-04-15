import java.io.*;
import java.util.*;

public class MemoryAllocationLab {

    static class MemoryBlock {
        int start;
        int size;
        String processName;  // null if free

        public MemoryBlock(int start, int size, String processName) {
            this.start = start;
            this.size = size;
            this.processName = processName;
        }

        public boolean isFree() {
            return processName == null;
        }

        public int getEnd() {
            return start + size - 1;
        }
    }

    static int totalMemory;
    static ArrayList<MemoryBlock> memory;
    static int successfulAllocations = 0;
    static int failedAllocations = 0;

    public static void processRequests(String filename) {
        memory = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
		String line = br.readLine();
		totalMemory = Integer.parseInt(line);

		MemoryBlock memoryBlock = new MemoryBlock(0, totalMemory, null);
		memory.add(memoryBlock);

		while ((line = br.readLine()) != null){
			String[] s = line.split(" ");
		
		if(s[0].equals("REQUEST")){
			allocate(s[1], Integer.parseInt(s[3]));	
		}else if (s[0].equals("RELEASE"){
			deallocate(s[1]);		
			}
		}

	}catch(Exception e){
		throw new RuntimeException(e);	
	}
    }

    /**
     * TODO 2A: Allocate memory using First-Fit
     */
    private static void allocate(String processName, int size) {
        
	for(MemoryBlock m : memory){
	  if(m.isFree() && m.size >= size){
	
	    if(m.size > size){
		int start = m.start + size;
		int newSize = m.size - size; 
		MemoryBlock extraBlock = new MemoryBlock(start, newSize, null);

		int indexOfBlock + 1, extraBlock);
		}

	    m.size = size;
	    m.processName = processName;
	 
	    sucessfulAllocations++;
	    System.out.println("SUCCESS");
	    return;

	  }
	}

	failedAllocations++;
	System.out.println("FAILED");
	
    }

	private static void deallocate(String processName){
	  for(MemoryBlock m : memory){
	    if(!m.isFree() && m.processName.equals(processName)){
		m.processName = null;
		System.out.println("SUCCESS");
		return;
	    }
	  }

	System.out.println("FAILED");
	}


    public static void displayStatistics() {
        System.out.println("\n========================================");
        System.out.println("Final Memory State");
        System.out.println("========================================");

        int blockNum = 1;
        for (MemoryBlock block : memory) {
            String status = block.isFree() ? "FREE" : block.processName;
            String allocated = block.isFree() ? "" : " - ALLOCATED";
            System.out.printf("Block %d: [%d-%d]%s%s (%d KB)%s\n",
                    blockNum++,
                    block.start,
                    block.getEnd(),
                    " ".repeat(Math.max(1, 10 - String.valueOf(block.getEnd()).length())),
                    status,
                    block.size,
                    allocated);
        }

        System.out.println("\n========================================");
        System.out.println("Memory Statistics");
        System.out.println("========================================");

        int allocatedMem = 0;
        int freeMem = 0;
        int numProcesses = 0;
        int numFreeBlocks = 0;
        int largestFree = 0;

        for (MemoryBlock block : memory) {
            if (block.isFree()) {
                freeMem += block.size;
                numFreeBlocks++;
                largestFree = Math.max(largestFree, block.size);
            } else {
                allocatedMem += block.size;
                numProcesses++;
            }
        }

        double allocatedPercent = (allocatedMem * 100.0) / totalMemory;
        double freePercent = (freeMem * 100.0) / totalMemory;
        double fragmentation = freeMem > 0 ?
                ((freeMem - largestFree) * 100.0) / freeMem : 0;

        System.out.printf("Total Memory:           %d KB\n", totalMemory);
        System.out.printf("Allocated Memory:       %d KB (%.2f%%)\n", allocatedMem, allocatedPercent);
        System.out.printf("Free Memory:            %d KB (%.2f%%)\n", freeMem, freePercent);
        System.out.printf("Number of Processes:    %d\n", numProcesses);
        System.out.printf("Number of Free Blocks:  %d\n", numFreeBlocks);
        System.out.printf("Largest Free Block:     %d KB\n", largestFree);
        System.out.printf("External Fragmentation: %.2f%%\n", fragmentation);

        System.out.println("\nSuccessful Allocations: " + successfulAllocations);
        System.out.println("Failed Allocations:     " + failedAllocations);
        System.out.println("========================================");
    }

    /**
     * Main method (FULLY PROVIDED)
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java MemoryAllocationLab <input_file>");
            System.out.println("Example: java MemoryAllocationLab memory_requests.txt");
            return;
        }

        System.out.println("========================================");
        System.out.println("Memory Allocation Simulator (First-Fit)");
        System.out.println("========================================\n");
        System.out.println("Reading from: " + args[0]);

        processRequests(args[0]);
        displayStatistics();
    }
}
