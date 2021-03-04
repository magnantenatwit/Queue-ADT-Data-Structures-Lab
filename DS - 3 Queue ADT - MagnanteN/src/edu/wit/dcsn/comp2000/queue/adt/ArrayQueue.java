/* @formatter:off * * Dave Rosenberg * Comp 2000 - Data Structures * Lab: Queue ADT * Fall, 2020 *  * Usage restrictions: *  * You may use this code for exploration, experimentation, and furthering your * learning for this course. You may not use this code for any other * assignments, in my course or elsewhere, without explicit permission, in * advance, from myself (and the instructor of any other course). *  * Further, you may not post nor otherwise share this code with anyone other than * current students in my sections of this course. Violation of these usage * restrictions will be considered a violation of the Wentworth Institute of * Technology Academic Honesty Policy. * * Do not remove this notice. * * @formatter:on */package edu.wit.dcsn.comp2000.queue.adt ;import edu.wit.dcsn.comp2000.queue.common.ArrayQueueCapacity ;import edu.wit.dcsn.comp2000.queue.common.EmptyQueueException ;import edu.wit.dcsn.comp2000.queue.common.QueueInterface ;import java.util.Arrays ;/** * A class that implements the ADT queue by using an expandable circular array. * * @author Frank M. Carrano * @author Timothy M. Henry * @version 4.0 * @version 5.0 *  * @author David M Rosenberg * @version 4.1.0 *     <ul> *     <li>initial version based upon Carrano and Henry implementation in the 4th *     edition of the textbook *     <li>modified per assignment *     </ul> * @version 5.1.0 2019-07-14 *     <ul> *     <li>simplified determination of parameters to {@code System.arraycopy()} in *     {@code ensureCapacity()} *     <li>revisions to match: *     <ul> *     <li>the 5th edition of the textbook *     <li>Dave Rosenberg Company coding standard *     <li>this semester's assignment *     </ul> *     </ul> * @version 5.2.0 2019-10-23 revise for this semester * @version 5.3.0 2020-03-01 *     <ul> *     <li>update to coding standard *     <li>switch from {@code QueueInterface} to {@code TestableQueueInterface} *     </ul> * @version 5.4.0 2020-06-21 rename {@code isFull()} to {@code isArrayFull()} to more *     accurately describe its functionality * @version 5.4.1 2020-06-26 fix {@code ensureCapacity()} - was overwriting the *     contents of the original array rather than copying them into the new array * @version 5.5.0 2020-10-22 *     <ul> *     <li>switch back to {@code QueueInterface} instead of *     {@code TestableQueueInterface} *     <li>remove {@code size()} and {@code toArray()} *     <li>add {@code toString()} to aid debugging *     <li>implement {@code main()} to aid debugging *     </ul> *  * @author Nicholas Magnante * @version 5.6.0 2020-10-22 changes per lab assignment * @param <T> *     The type of all objects to store in the queue */public final class ArrayQueue<T> implements QueueInterface<T>    {    // utility constants    /** default (and minimum) number of entries in a queue */    private static final int DEFAULT_CAPACITY =                                    ArrayQueueCapacity.DEFAULT.capacityValue ;    /** maximum number of entries in a queue */    private static final int MAX_CAPACITY =                                    ArrayQueueCapacity.MAXIMUM.capacityValue ;    // instance variables    private T[] queue ;             // circular array of queue entries        private int frontIndex ;        // index of front entry    private int backIndex ;         // index of back entry        private int numberOfEntries ;   // number of entries currently in the queue        private boolean integrityOK = false ;   // true if data structure is created                                    // correctly, false if corrupted    // constructors    /**     * Initializes a queue with a default capacity     */    public ArrayQueue()        {        this( DEFAULT_CAPACITY ) ;        } // end no-arg constructor    /**     * Initializes a queue with a specified capacity     *     * @param initialCapacity     *     in the range DEFAULT_CAPACITY..MAX_CAPACITY     * @throws IllegalStateException     *     if the {@code desiredCapacity} is outside the valid range     */    public ArrayQueue( final int initialCapacity )        {        // DONE: move this functionality to initializeState() then invoke that method        initializeState( initialCapacity ) ;        } // end 1-arg constructor        /*     * public API methods     */    /*     * (non-Javadoc)     * @see edu.wit.dcsn.comp2000.queue.adt.QueueInterface#clear()     */    @Override    public void clear()        {        // DONE: replace this with an appropriate invocation of initializeState()        checkIntegrity() ;        initializeState( this.queue.length ) ;                if ( !isEmpty() )            { // Clears only the used portion            for ( int index = this.frontIndex ;                  index != this.backIndex ;                  index = ( index + 1 ) % this.queue.length )                {                this.queue[ index ] = null ;                } // end for            this.queue[ this.backIndex ] = null ;            this.numberOfEntries = 0 ;            }        }	// end clear()    /*     * (non-Javadoc)     * @see edu.wit.dcsn.comp2000.queue.adt.QueueInterface#dequeue()     */    @Override    public T dequeue()        {        // save the front item        final T front = getFront() ;        // performs integrity and empty checks        // remove it from the array        this.queue[ this.frontIndex ] = null ;        this.frontIndex = ( this.frontIndex + 1 ) % this.queue.length ;        // DONE: manage this.numberOfEntries        this.numberOfEntries-- ;        return front ;        }	// end dequeue()    /*     * (non-Javadoc)     * @see edu.wit.dcsn.comp2000.queue.adt.QueueInterface#enqueue(java.lang.Object)     */    @Override    public void enqueue( final T newEntry )        {        checkIntegrity() ;        ensureCapacity() ;        this.backIndex = ( this.backIndex + 1 ) % this.queue.length ;        this.queue[ this.backIndex ] = newEntry ;        // DONE: manage this.numberOfEntries        this.numberOfEntries++ ;        }	// end enqueue()    /*     * (non-Javadoc)     * @see edu.wit.dcsn.comp2000.queue.adt.QueueInterface#getFront()     */    @Override    public T getFront()        {        checkIntegrity() ;        if ( isEmpty() )            {            throw new EmptyQueueException() ;            }        return this.queue[ this.frontIndex ] ;        }	// end getFront()    /*     * (non-Javadoc)     * @see edu.wit.dcsn.comp2000.queue.adt.QueueInterface#isEmpty()     */    @Override    public boolean isEmpty()        {        // DONE: replace the following expression with a test of this.numberOfEntries        return this.numberOfEntries == 0 ;        }	// end isEmpty()        /*     * private utility methods     */    /**     * Ensure the instance was properly initialized     */    private void checkIntegrity()        {        if ( !this.integrityOK )            {            throw new SecurityException( "ArrayQueue object is corrupt." ) ;            }        }	// end checkIntegrity()    /**     * Validate that the desired capacity is within acceptable limits.     *     * @param desiredCapacity     *     size in the range DEFAULT_CAPACITY..MAX_CAPACITY, inclusive     * @throws IllegalStateException     *     if the {@code desiredCapacity} is outside the valid range     */    private static void checkCapacity( final int desiredCapacity )        {        // DONE: add check for too small a desiredCapacity                        if ( desiredCapacity < DEFAULT_CAPACITY )            {            throw new IllegalStateException( "Attempt to create a queue whose " +                                             "capacity is too small (" +                                             desiredCapacity + ")." ) ;            }        }	// end checkCapacity()    /**     * Doubles the size of the array queue if it is full     * <p>     * Precondition: checkIntegrity() has been called.     */    private void ensureCapacity()        {        if ( isArrayFull() )				// if array is full, double size of array            {            final T[] oldQueue = this.queue ;            final int oldSize = oldQueue.length ;            final int newSize = 2 * oldSize ;            checkCapacity( newSize ) ;            // inconsistent state if new array can't be instantiated/allocated            this.integrityOK = false ;            // The cast is safe because the new array contains null entries            @SuppressWarnings( "unchecked" )            final T[] tempQueue = (T[]) new Object[ newSize ] ;            /* @formatter:off             *              * DONE: in both invocations of System.arraycopy():             * replace the -1's with source and destination positions and the length             * - only use instance variables, constants (hint: 0), and/or simple              * arithmetic expressions (single subtraction) using a local variable             *              * NOTE: the two calls to System.arraycopy() will replace the for loop             *              * Don't forget to update backIndex to reflect use of the entire array             *              * @formatter:on             */                        // DONE: delete the for loop                        // DONE: uncomment the two calls to System.arraycopy                     // copy the front of the queue            System.arraycopy( oldQueue,                     // from                              this.frontIndex,                              tempQueue,                    // to                              0,                              oldQueue.length - this.frontIndex ) ; // how many elements            // copy the back of the queue            System.arraycopy( oldQueue,                     // from                              0,                              tempQueue,                    // to                              ( this.numberOfEntries - 1 ) - this.backIndex,                              this.frontIndex ) ;           // how many elements            // set the state to utilize the new queue array            this.queue = tempQueue ;            this.frontIndex = 0 ;            this.backIndex = oldQueue.length - 1 ;                        this.integrityOK = true ;            } // end if        } // end ensureCapacity()    /**     * Initializes a queue with a specified capacity     *     * @param desiredCapacity     *     in the range DEFAULT_CAPACITY..MAX_CAPACITY, inclusive     */    private void initializeState( final int desiredCapacity )        {        // STUB - implement this so the 1-arg constructor and clear() can use it        this.integrityOK = false ;                checkCapacity( desiredCapacity ) ;                if ( desiredCapacity > MAX_CAPACITY )            {            throw new IllegalStateException( "Attempt to create a queue whose " +                                             "capacity is too large (" +                                             desiredCapacity + ")." ) ;            }                // DONE: modify references to this.backIndex throughout this class to use all         //          entries in the array (do not leave an unused location to indicate        //          the array is full)        // NOTE: this is the only DONE for this step - there are multiple places you        //          may need to modify - check all the code carefully        this.backIndex = desiredCapacity - 1 ;        this.numberOfEntries = 0 ;                this.integrityOK = true ;             // The cast is safe because the new array contains null entries        @SuppressWarnings( "unchecked" )        final T[] tempQueue = (T[]) new Object[ desiredCapacity ] ;        this.queue = tempQueue ;        this.frontIndex = 0 ;        }	// end initializeState()    /**     * Determine if the queue's array is full     *     * @return true if the array is full, false otherwise     */    private boolean isArrayFull()        {        // DONE: update the test condition with a single boolean expression        return this.numberOfEntries == this.queue.length ;        }	// end isArrayFull()    /*     * testing/debugging methods     */        // This method is not typically implemented - I included it for debugging purposes    /*     * (non-Javadoc)     * @see java.lang.toString()     */    @Override    public String toString()        {        return String.format( "frontIndex: %,2d, backIndex: %,2d, numberOfEntries: %,2d, queue: %s",                              this.frontIndex,                              this.backIndex,                              this.numberOfEntries,                              Arrays.toString( this.queue ) ) ;        }   // end toString()    /**     * (optional) driver for testing/debugging     *      * @param args     *     -unused-     */    public static void main( final String[] args )        {        // OPTIONAL for testing/debugging                // create a queue        System.out.println( "empty queue" ) ;        QueueInterface<Integer> testQueue = new ArrayQueue<>(3) ;        System.out.println( testQueue.toString() ) ;        System.out.println() ;                // fill its array        System.out.println( "enqueue(1)" ) ;        testQueue.enqueue( 1 ) ;        System.out.println( testQueue.toString() ) ;        System.out.println() ;                System.out.println( "enqueue(2)" ) ;        testQueue.enqueue( 2 ) ;        System.out.println( testQueue.toString() ) ;        System.out.println() ;                System.out.println( "enqueue(3)" ) ;        testQueue.enqueue( 3 ) ;        System.out.println( testQueue.toString() ) ;        System.out.println() ;                // cause the array to be resized        System.out.println( "enqueue(4)" ) ;        testQueue.enqueue( 4 ) ;        System.out.println( testQueue.toString() ) ;        System.out.println() ;                System.out.println( "dequeue() -> 1" ) ;        testQueue.dequeue() ;        System.out.println( testQueue.toString() ) ;        System.out.println() ;                System.out.println( "enqueue(5)" ) ;        testQueue.enqueue( 5 ) ;        System.out.println( testQueue.toString() ) ;        System.out.println() ;                System.out.println( "enqueue(6)" ) ;        testQueue.enqueue( 6 ) ;        System.out.println( testQueue.toString() ) ;        System.out.println() ;                System.out.println( "enqueue(7)" ) ;        testQueue.enqueue( 7 ) ;        System.out.println( testQueue.toString() ) ;        System.out.println() ;                // cause the array to be resized        System.out.println( "enqueue(8)" ) ;        testQueue.enqueue( 8 ) ;        System.out.println( testQueue.toString() ) ;        }   // end main()    } // end class ArrayQueue