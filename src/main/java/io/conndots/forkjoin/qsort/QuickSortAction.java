package io.conndots.forkjoin.qsort;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * Created by xqlee on 16/1/24.
 */
public class QuickSortAction<T extends Comparable<T>> extends RecursiveAction {
    private final ArrayList<T> toSortArray;
    private final int start;
    private final int end;

    public QuickSortAction(ArrayList<T> toSort, int start, int end) {
        Preconditions.checkNotNull(toSort);
        Preconditions.checkArgument(start < end, "end must be larger than start");
        Preconditions.checkArgument(start >= 0, "start must be larger than -1");
        toSortArray = toSort;
        this.start = start;
        this.end = end;
    }

    private int pivotAndSplit() {
        int left = start, right = end - 1;
        T pivot = toSortArray.get(start);

        while (left < right) {
            while (toSortArray.get(right).compareTo(pivot) > 0 && left < right) {
                --right;
            }
            if (left == right) {
                break;
            }
            toSortArray.set(left++, toSortArray.get(right));

            while (toSortArray.get(left).compareTo(pivot) <= 0 && left < right) {
                ++left;
            }
            if (left == right) {
                break;
            }
            toSortArray.set(right--, toSortArray.get(left));
        }
        toSortArray.set(left, pivot);
        return left;
    }

    @Override
    protected void compute() {
        int pivotPosition = pivotAndSplit();

        List<RecursiveAction> todos = Lists.newArrayList();
        if (pivotPosition > start + 1) {
            todos.add(new QuickSortAction(toSortArray, start, pivotPosition));
        }
        if (end > pivotPosition + 1 + 1) {
            todos.add(new QuickSortAction(toSortArray, pivotPosition + 1, end));
        }
        invokeAll(todos);
    }
}
