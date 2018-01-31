//UT-EID=

import java.util.*;
import java.util.concurrent.*;

public class PSort {
  public static void parallelSort(int[] A, int begin, int end) {
    // TODO: Implement your parallel sort function
    // int processors = Runtime.getRuntime().availableProcessors();
    ForkJoinPool forkJoinPool = new ForkJoinPool();
    QuickSort qs = new QuickSort(A, begin, end, 0);
    forkJoinPool.invoke(qs);

  }

}

class QuickSort extends RecursiveTask<int[]> {
  int[] A;
  int begin, end, depth;

  public QuickSort(int[] A, int begin, int end, int depth) {
    this.A = A;
    this.begin = begin;
    this.end = end;
    this.depth = depth;
  }

  @Override
  protected int[] compute() {
    // TODO Auto-generated method stub
    if (begin == end) {
      return A;
    }
    if (end - begin <= 16) {
      System.out.printf ("depth: %2d\tbegin: %5d\tend: %5d\n", this.depth, begin, end);
      return seqInsertSort(A, begin, end);
      // return A;
    } else {
      int index = getSplitIndex(A, begin, end);
      System.out.printf ("depth: %2d\tbegin: %5d\tend: %5d\tpivot: %5d\n", this.depth, begin, end, index);
      // int index = (begin + end) / 2;
      QuickSort qs1 = new QuickSort(A, begin, index, this.depth+1);
      qs1.fork();
      QuickSort qs2 = new QuickSort(A, index + 1, end, this.depth+1);
      qs2.compute();
      qs1.join();

      return A;
    }
  }

  int pickPivot (int a, int b, int c)
  {
    int result;
    if (a < b)
    {
      if (b > c)
      {
        if (c < a)
          result = 0;
        else
          result = 2;
      }
      else
        result = 1;
    }
    else
    {
      if (b < c)
      {
        if (c > a)
          result = 0;
        else
          result = 2;
      }
      else
        result = 1;
    }

    return result;
  }

  int getSplitIndex(int[] A, int begin, int end) {
    int[] idx = new int[3];
    idx[0] = begin;
    idx[1] = (begin + end)/2;
    idx[2] = end-1;
    int pivot = idx[pickPivot (A[idx[0]], A[idx[1]], A[idx[2]])];
    int i = begin - 1;
    for (int j = begin; j < end; j++) {
      if (A[j] < A[pivot]) {
        int swap = A[++i];
        A[i] = A[j];
        A[j] = swap;
        if (i == pivot)
          pivot = j;
      }
    }
    if (A[pivot] < A[i + 1]) {
      int swap = A[pivot];
      A[pivot] = A[i + 1];
      A[i + 1] = swap;
    }
    return i + 1;
  }

  int[] seqInsertSort(int[] A, int begin, int end) {
    for (int i = begin + 1; i < end; i++) {
      int j = i - 1;
      int current = A[i];
      while (j >= begin && current < A[j]) {
        A[j + 1] = A[j];
        A[j] = current;
        j--;
      }
    }
    return A;
  }
}
