/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.sort.elementary.InsertionSort;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static edu.neu.coe.info6205.util.Utilities.formatWhole;

/**
 * This class implements a simple Benchmark utility for measuring the running time of algorithms.
 * It is part of the repository for the INFO6205 class, taught by Prof. Robin Hillyard
 * <p>
 * It requires Java 8 as it uses function types, in particular, UnaryOperator&lt;T&gt; (a function of T => T),
 * Consumer&lt;T&gt; (essentially a function of T => Void) and Supplier&lt;T&gt; (essentially a function of Void => T).
 * <p>
 * In general, the benchmark class handles three phases of a "run:"
 * <ol>
 *     <li>The pre-function which prepares the input to the study function (field fPre) (may be null);</li>
 *     <li>The study function itself (field fRun) -- assumed to be a mutating function since it does not return a result;</li>
 *     <li>The post-function which cleans up and/or checks the results of the study function (field fPost) (may be null).</li>
 * </ol>
 * <p>
 * Note that the clock does not run during invocations of the pre-function and the post-function (if any).
 *
 * @param <T> The generic type T is that of the input to the function f which you will pass in to the constructor.
 */
public class Benchmark_Timer<T> implements Benchmark<T> {

    /**
     * Calculate the appropriate number of warmup runs.
     *
     * @param m the number of runs.
     * @return at least 2 and at most m/10.
     */
    static int getWarmupRuns(int m) {
        return Integer.max(2, Integer.min(10, m / 10));
    }

    /**
     * Run function f m times and return the average time in milliseconds.
     *
     * @param supplier a Supplier of a T
     * @param m        the number of times the function f will be called.
     * @return the average number of milliseconds taken for each run of function f.
     */
    @Override
    public double runFromSupplier(Supplier<T> supplier, int m) {
        logger.info("Begin run: " + description + " with " + formatWhole(m) + " runs");
        // Warmup phase
        final Function<T, T> function = t -> {
            fRun.accept(t);
            return t;
        };
        new Timer().repeat(getWarmupRuns(m), supplier, function, fPre, null);

        // Timed phase
        return new Timer().repeat(m, supplier, function, fPre, fPost);
    }

    /**
     * Constructor for a Benchmark_Timer with option of specifying all three functions.
     *
     * @param description the description of the benchmark.
     * @param fPre        a function of T => T.
     *                    Function fPre is run before each invocation of fRun (but with the clock stopped).
     *                    The result of fPre (if any) is passed to fRun.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *                    When you create a lambda defining fRun, you must return "null."
     * @param fPost       a Consumer function (i.e. a function of T => Void).
     */
    public Benchmark_Timer(String description, UnaryOperator<T> fPre, Consumer<T> fRun, Consumer<T> fPost) {
        this.description = description;
        this.fPre = fPre;
        this.fRun = fRun;
        this.fPost = fPost;
    }

    /**
     * Constructor for a Benchmark_Timer with option of specifying all three functions.
     *
     * @param description the description of the benchmark.
     * @param fPre        a function of T => T.
     *                    Function fPre is run before each invocation of fRun (but with the clock stopped).
     *                    The result of fPre (if any) is passed to fRun.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     */
    public Benchmark_Timer(String description, UnaryOperator<T> fPre, Consumer<T> fRun) {
        this(description, fPre, fRun, null);
    }

    /**
     * Constructor for a Benchmark_Timer with only fRun and fPost Consumer parameters.
     *
     * @param description the description of the benchmark.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *                    When you create a lambda defining fRun, you must return "null."
     * @param fPost       a Consumer function (i.e. a function of T => Void).
     */
    public Benchmark_Timer(String description, Consumer<T> fRun, Consumer<T> fPost) {
        this(description, null, fRun, fPost);
    }
    public static void main(String[] args){
        //insertion sort
        InsertionSort insertion_sort = new InsertionSort();
        Benchmark_Timer<Integer[]> benchmarkTimer = new Benchmark_Timer<>("Benchmark Test", null, a -> insertion_sort.sort(a,0,a.length),null);
        // doubles the size of the array
        for (int i = 20; i <1000; i =i*2) {
            int j = i;
            //timing a random ordered array

            Supplier<Integer[]> supplier =  () -> {
                Random random = new Random();
                Integer[] array = new Integer[j];
                //for generating a random array
                for(int k = 0; k < j; k++){
                    array[k] = random.nextInt(j*5);
                }
                return array;
            };

            double time = benchmarkTimer.runFromSupplier(supplier, 20);
            System.out.println(time +"ns " + "is the time taken to sort a randomly ordered array of size" + i );


        }
        //timing a  partially ordered array for doubling n
        for(int i = 20; i < 1000; i = i*2){
            int j = i;
            Supplier <Integer[]> supplier = ()->{
                Random random = new Random();
                Integer[] array = new Integer[j];
                //generating a partially ordered array
                for(int k=0;k< j; k++){
                    array[k] = random.nextInt(j);
                }
                Arrays.sort(array);
                int rearrangeArray = (int) (0.5*j); // rearranges half of the elements

                for(int i1 = 0; i1 < rearrangeArray; i1++){
                    int key = random.nextInt(j);
                    array[key] = random.nextInt(j*10);

                }
                return array;
            };
            double time = benchmarkTimer.runFromSupplier(supplier, 20);
            System.out.println(time +"ns " + "is the time taken to sort a partially ordered array of size" + i );

        }
        //timing an ordered array for doubling n
        for(int i = 20; i <1000; i =i*2) {
            int j =i;
            Supplier<Integer[]> supplier = () -> {
                Random random = new Random();
                Integer[] array = new Integer [j];
                for(int k = 0; k < j; k++){
                    array[k] = random.nextInt(j*20);

                }
                Arrays.sort(array);
                return array;

            };
            double time = benchmarkTimer.runFromSupplier(supplier, 20);
            System.out.println(time +"ns " + "is the time taken to sort an ordered array of size" + i );

        }
        // timing a reverse ordered array
        for(int i = 20; i < 1000;i= i*2){
            int j = i;
            Supplier <Integer[]> supplier = () -> {
                Random random = new Random();
                Integer [] array = new Integer[j];

                for(int k = 0; k < j; k++){
                    array[k] = random.nextInt(j);
                }
                Arrays.sort(array, Collections.reverseOrder());
                return array;

            };
            double time = benchmarkTimer.runFromSupplier(supplier, 20);
            System.out.println(time +"ns " + "is the time taken to sort a reverse ordered array of size" + i );

        }

    }

    /**
     * Constructor for a Benchmark_Timer where only the (timed) run function is specified.
     *
     * @param description the description of the benchmark.
     * @param f           a Consumer function (i.e. a function of T => Void).
     *                    Function f is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     */
    public Benchmark_Timer(String description, Consumer<T> f) {
        this(description, null, f, null);
    }

    private final String description;
    private final UnaryOperator<T> fPre;
    private final Consumer<T> fRun;
    private final Consumer<T> fPost;

    final static LazyLogger logger = new LazyLogger(Benchmark_Timer.class);
}
