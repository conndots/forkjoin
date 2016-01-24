package io.conndots.forkjoin.qsort

import com.google.common.collect.Lists
import io.conndots.forkjoin.ForkJoinRunner
import spock.lang.Specification

/**
 * Created by xqlee on 16/1/24.
 */
class QuickSortActionTest extends Specification {
    def "sort corectness"() {
        given:
        ArrayList<Integer> toSort = Lists.newArrayList(3, 2, 90, 23, 11, 13, 6, 10, -42, 0)
        List<Integer> compareList = Lists.newArrayList(toSort)
        ForkJoinRunner.runTask(new QuickSortAction(toSort, 0, toSort.size()))
        List<Integer> sorted = compareList.sort()

        expect:
        toSort == sorted



    }

    def "running efficency check"() {
        given:
        Random random = new Random();
        ArrayList<Integer> toSort = Lists.newArrayList()
        for (int i = 0; i < 100000000; i ++) {
            toSort.add(random.nextInt(1000000000))
        }

        List<Integer> compareList = Lists.newArrayList(toSort)
        long singleStart = System.currentTimeMillis()
        ForkJoinRunner.runTask(new QuickSortAction(compareList, 0, compareList.size()), 1)
        long takenSingle = System.currentTimeMillis() - singleStart


        compareList = Lists.newArrayList(toSort)
        long javaStart = System.currentTimeMillis()
        compareList = compareList.sort()
        long takenJava = System.currentTimeMillis() - javaStart

        long parrallelStart = System.currentTimeMillis()
        ForkJoinRunner.runTask(new QuickSortAction(toSort, 0, toSort.size()), 16)
        long takenParrallel = System.currentTimeMillis() - parrallelStart

        expect:
        println("Parrallel:" + takenParrallel + "; Single:" + takenSingle + "; Java:" + takenJava)
        takenParrallel < takenSingle
        takenParrallel < takenJava
        toSort == compareList

        /**
         * 100000000-size
         * Parrallel:21462; Single:106027; Java:65548

         */

    }

    def "async/sync compare"() {
        given:
        Random random = new Random();
        ArrayList<Integer> toSort = Lists.newArrayList()
        for (int i = 0; i < 100000000; i ++) {
            toSort.add(random.nextInt(1000000000))
        }

        List<Integer> compareList = Lists.newArrayList(toSort)
        long syncStart = System.currentTimeMillis()
        ForkJoinRunner.runTask(new QuickSortAction(compareList, 0, compareList.size()), 16, false)
        long syncTaken = System.currentTimeMillis() - syncStart
        println("sync: " + syncTaken)

        long asyncStart = System.currentTimeMillis()
        ForkJoinRunner.runTask(new QuickSortAction(toSort, 0, toSort.size()), 16, true)
        long asyncTaken = System.currentTimeMillis() - asyncStart
        println("async: " + asyncTaken)

        /**
         * size-10000000
         * sync: 2831
         * async: 1124
         *
         * size-100000000
         * sync: 42303
         * async: 16428
         */
    }
}
