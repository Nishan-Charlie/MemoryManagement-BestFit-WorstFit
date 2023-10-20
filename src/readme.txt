The MemManager class has four methods for allocating memory:firstFit, bestFit, worstFit, and allocate.

The firstFit, bestFit, and worstFit methods are responsible for finding a suitable block of free memory for the request, and then calling the allocate method to actually perform the allocation.

The allocate method creates a new MemBlock object for the allocated memory and adds it to the allocatedMem list.
It then updates the freeMem list by creating a new MemBlock object for the remaining free memory and adding it to the list.

The deallocate method is used to free up a block of previously allocated memory. 
It finds the MemBlock object in the allocatedMem list that corresponds to the request, 
adds it to the freeMem list, and updates the freeMem list by combining adjacent blocks of free memory.