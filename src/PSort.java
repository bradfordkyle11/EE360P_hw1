//UT-EID=


import java.util.*;
import java.util.concurrent.*;

public class PSort{
  public static void parallelSort(int[] A, int begin, int end){
    // TODO: Implement your parallel sort function
	  ForkJoinPool forkJoinPool = new ForkJoinPool();
	  QuickSort qs = new QuickSort(A, begin, end);
	  forkJoinPool.invoke(qs);



  }


}

class QuickSort extends RecursiveTask<int[]>{
	int[] A;
	int begin, end;
	public QuickSort(int[] A, int begin, int end){
		this.A  = A;
		this.begin = begin;
		this.end = end;
	}
	@Override
	protected int[] compute() {
		// TODO Auto-generated method stub
		if (begin == end) {
			return A;
		}
		if (end - begin <= 16) {
			return seqInsertSort(A, begin, end);
		}
		else {
			int index = getSplitIndex(A, begin, end);
			QuickSort qs1 = new QuickSort(A, begin, index);
			qs1.fork();
			QuickSort qs2 = new QuickSort(A, index+1, end);
			qs2.compute();
			qs1.join();

      return A;
		}
	}

	int getSplitIndex(int[] A, int begin, int end) {
		int pivot = A[begin];
		int i = begin - 1;
		for (int j = begin; j < end - 1; j++) {
			if (A[j] < pivot) {
				i += 1;
				int swap = A[i];
				A[i] = A[j];
				A[j] = A[i];
			}
		}
		if (A[end-1] < A[i+1]) {
			int swap = A[end-1];
			A[end - 1] = A[i + 1];
			A[i + 1] = swap;
		}
		return i + 1;
	}


	int[] seqInsertSort(int[] A, int begin, int end) {
		for (int i = begin + 1; i < end; i++) {
			int j = i - 1;
			int current = A[i];
			while (current < A[j] && j > begin) {
				A[j + 1] = A[j];
				A[j] = current;
				j--;
			}
		}
		return A;
	}
}
