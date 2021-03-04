/* @formatter:off
 *
 * Dave Rosenberg
 * Comp 2000 - Data Structures
 * Lab: Queue ADT
 * Summer, 2020
 *
 * Usage restrictions:
 *
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course).
 *
 * Further, you may not post or otherwise share this code with anyone other than
 * current students in my sections of this course. Violation of these usage
 * restrictions will be considered a violation of the Wentworth Institute of
 * Technology Academic Honesty Policy.
 *
 * Do not remove this notice.
 *
 * @formatter:on
 */

package edu.wit.dcsn.dmr.testing ;

import static org.junit.jupiter.api.Assertions.assertEquals ;
import static org.junit.jupiter.api.Assertions.assertFalse ;
import static org.junit.jupiter.api.Assertions.assertNotEquals ;
import static org.junit.jupiter.api.Assertions.assertNotNull ;
import static org.junit.jupiter.api.Assertions.assertNull ;
import static org.junit.jupiter.api.Assertions.assertThrows ;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively ;
import static org.junit.jupiter.api.Assertions.assertTrue ;
import static org.junit.jupiter.api.Assertions.fail ;

import edu.wit.dcsn.comp2000.queue.adt.ArrayQueue ;
import edu.wit.dcsn.comp2000.queue.common.ArrayQueueCapacity ;
import edu.wit.dcsn.comp2000.queue.common.EmptyQueueException ;
import edu.wit.dcsn.comp2000.queue.common.QueueInterface ;
import edu.wit.dcsn.dmr.testing.junit.JUnitTestingBase ;

import static edu.wit.dcsn.dmr.testing.junit.Reflection.* ;
import static edu.wit.dcsn.dmr.testing.junit.TestData.* ;

import org.junit.jupiter.api.Disabled ;
import org.junit.jupiter.api.DisplayName ;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation ;
import org.junit.jupiter.api.Order ;
import org.junit.jupiter.api.Test ;
import org.junit.jupiter.api.TestInfo ;
import org.junit.jupiter.api.TestInstance ;
import org.junit.jupiter.api.TestInstance.Lifecycle ;
import org.junit.jupiter.api.TestMethodOrder ;
import org.junit.jupiter.params.ParameterizedTest ;
import org.junit.jupiter.params.provider.CsvFileSource ;

/**
 * JUnit tests for the ArrayQueue class. All public and package visible methods are
 * tested. These tests require the API for the ArrayQueue class implement
 * {@code TestableQueueInterface<T>}.
 *
 * @author David M Rosenberg
 * @version 1.0.0 2018-06-27 initial set of tests
 * @version 1.1.0 2018-09-02 add timeouts
 * @version 2.0.0 2019-05-07
 *     <ul>
 *     <li>start restructuring tests
 *     <li>disable System.exit() during testing
 *     <li>start making each subtest independent so they'll all run even if one fails
 *     </ul>
 * @version 3.0.0 2019-10-22 switch to new infrastructure
 * @version 3.1.0 2020-03-01
 *     <ul>
 *     <li>update to coding standard
 *     <li>switch capacity specifications to external enum
 *     <li>implement individual tests
 *     <li>continue switch to new infrastructure
 *     </ul>
 * @version 3.2.0 2020-06-21
 *     <ul>
 *     <li>remove unnecessary method {@code copyQueueIntoArray()}
 *     <li>move test data to test-data-DMR
 *     </ul>
 * @version 3.3.0 2020-06-24 fix Full Queue, Queue Growth, and Multiple Queue tests
 * @version 3.1.0 2020-10-19
 *     <ul>
 *     <li>general cleanup
 *     <li>switch from TestableQueueInterface back to QueueInterface
 *     <ul>
 *     <li>remove tests for {@code size()} and {@code toArray()}
 *     </ul>
 *     <li>continued migration to new testing infrastructure
 *     <li>rename {@code test-data-DMR} folder to {@code test-data-dmr}
 *     </ul>
 */
@DisplayName( "ArrayQueue" )
@TestInstance( Lifecycle.PER_CLASS )
@TestMethodOrder( OrderAnnotation.class )
class ArrayQueueDMRTests extends JUnitTestingBase
    {

    /*
     * local constants
     */
    private static final int DEFAULT_CAPACITY = ArrayQueueCapacity.DEFAULT.capacityValue ;
    private static final int MAX_CAPACITY = ArrayQueueCapacity.MAXIMUM.capacityValue ;
    
    private static final int SMALL_CAPACITY  = ArrayQueueCapacity.SMALL.capacityValue ;
    private static final int MEDIUM_CAPACITY = ArrayQueueCapacity.MEDIUM.capacityValue ;
    private static final int LARGE_CAPACITY  = ArrayQueueCapacity.LARGE.capacityValue ;
    
    private static final int OVER_MAXIMUM_CAPACITY = ArrayQueueCapacity.OVER_MAXIMUM.capacityValue ;
    private static final int WAY_OVER_MAXIMUM_CAPACITY = ArrayQueueCapacity.WAY_OVER_MAXIMUM.capacityValue ;

    private static final int ZERO_CAPACITY = ArrayQueueCapacity.ZERO.capacityValue ;
    private static final int NEGATIVE_CAPACITY = ArrayQueueCapacity.NEGATIVE.capacityValue ;
    private static final int UNDER_MINIMUM_CAPACITY = ArrayQueueCapacity.UNDER_MINIMUM.capacityValue ;
    
    private static final int QUEUE_1_BASE = 100 ;
    private static final int QUEUE_2_BASE = MAX_CAPACITY * 10 ;

    // ----------

    // individual method tests

    /**
     * Test method for
     * {@link edu.wit.dcsn.comp2000.queue.adt.ArrayQueue#ArrayQueue()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = "./test-data-dmr/test-constructor.data",
                    numLinesToSkip = 1 )
    @Order( 100100 )
    @DisplayName( "Queue constructor" )
    final void testArrayQueue( final boolean isLastTest,
                               final boolean isStubBehavior,
                               final String encodedTestConfiguration,
                               final TestInfo testInfo )
        {
        final Object[][] decodedTestConfiguration =
                                        startTest( testInfo,
                                                   isLastTest,
                                                   isStubBehavior,
                                                   new String[] { "initial capacity" },
                                                   encodedTestConfiguration ) ;

        // execute the test
        assertTimeoutPreemptively( testTimeLimit, () ->
            {
            if ( decodedTestConfiguration[ 0 ].length == 0 )
                {
                // test the no-arg constructor

                writeLog( "\tTesting: new ArrayQueue() with implicit default capacity %,d%n",
                          DEFAULT_CAPACITY ) ;
                QueueInterface<Object> testQueue = null ;

                testQueue = new ArrayQueue<>() ;

                verifyQueueState( testQueue, 0, DEFAULT_CAPACITY ) ;

                }
            else
                {
                // test the 1-arg constructor

                final ArrayQueueCapacity testCapacity =
                                                ArrayQueueCapacity.interpretDescription( (String) decodedTestConfiguration[ 0 ][ 0 ] ) ;
                final int testSize = testCapacity.capacityValue ;

                writeLog( "\tTesting: new ArrayQueue(%,d)%n", testSize ) ;

                if ( ( testSize >= DEFAULT_CAPACITY ) &&
                     ( testSize <= MAX_CAPACITY ) )
                    {
                    // test valid initial capacity
                    final QueueInterface<Object> testQueue =
                                                    new ArrayQueue<>( testSize ) ;

                    verifyQueueState( testQueue, 0, testSize ) ;

                    }   // end test valid initial capacity
                else
                    {
                    // test invalid initial capacity
                    assertThrows( IllegalStateException.class, () ->
                        {
                        @SuppressWarnings( "unused" )
                        final QueueInterface<Object> testQueue =
                                                        new ArrayQueue<>( testSize ) ;
                        } ) ;

                    }   // end test invalid initial capacity

                }

            this.currentTestPassed = true ;
            } ) ;

        }   // end testArrayQueue()


    /**
     * Test method for
     * {@link edu.wit.dcsn.comp2000.queue.adt.ArrayQueue#enqueue(java.lang.Object)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 300100 )
    @Disabled                               // FUTURE
    final void testEnqueue( final boolean isLastTest,
                            final boolean isStubBehavior,
                            final String encodedTestConfiguration,
                            final TestInfo testInfo )
        {
        final Object[][] decodedTestConfiguration =
                                        startTest( testInfo,
                                                   isLastTest,
                                                   isStubBehavior,
                                                   encodedTestConfiguration ) ;

        // STUB

        } // end testEnqueue()


    /**
     * Test method for {@link edu.wit.dcsn.comp2000.queue.adt.ArrayQueue#dequeue()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 300200 )
    @Disabled                               // FUTURE
    final void testDequeue( final boolean isLastTest,
                            final boolean isStubBehavior,
                            final String encodedTestConfiguration,
                            final TestInfo testInfo )
        {
        final Object[][] decodedTestConfiguration =
                                        startTest( testInfo,
                                                   isLastTest,
                                                   isStubBehavior,
                                                   encodedTestConfiguration ) ;

        // STUB

        } // end testDequeue()


    /**
     * Test method for {@link edu.wit.dcsn.comp2000.queue.adt.ArrayQueue#getFront()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 300300 )
    @Disabled                               // FUTURE
    final void testGetFront( final boolean isLastTest,
                             final boolean isStubBehavior,
                             final String encodedTestConfiguration,
                             final TestInfo testInfo )
        {
        final Object[][] decodedTestConfiguration =
                                        startTest( testInfo,
                                                   isLastTest,
                                                   isStubBehavior,
                                                   encodedTestConfiguration ) ;

        // STUB

        } // end testGetFront()


    /**
     * Test method for {@link edu.wit.dcsn.comp2000.queue.adt.ArrayQueue#isEmpty()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = "./test-data-dmr/test-is-empty.data",
                    numLinesToSkip = 1 )
    @DisplayName( "isEmpty()" )
    @Order( 200100 )
    final void testIsEmpty( final boolean isLastTest,
                            final boolean isStubBehavior,
                            final String encodedTestConfiguration,
                            final TestInfo testInfo )
        {
        final Object[][] queueContents = startTest( testInfo,
                                                    isLastTest,
                                                    isStubBehavior,
                                                    new String[] { "contents" },
                                                    encodedTestConfiguration ) ;

        // execute the test
        assertTimeoutPreemptively( testTimeLimit, () ->
            {
            // determine expected result
            final int testSize = Math.min( queueContents[ 0 ].length, MAX_CAPACITY ) ;
            final boolean expectedResult = testSize == 0 ;
            final int queueSize = Math.max( testSize, DEFAULT_CAPACITY ) ;

            // display message describing the expected result of this test
            writeLog( "\texpect: %b%n", expectedResult ) ;

            // instantiate testQueue
            final QueueInterface<Object> testQueue = new ArrayQueue<>( queueSize ) ;

            // populate it
            populateQueue( testQueue, queueContents[ 0 ] ) ;
            
            verifyQueueState( testQueue, testSize, queueSize ) ;

            // determine if there are any entries in the queue
            final boolean actualResult ;

            // test isEmpty()
            try
                {
                actualResult = testQueue.isEmpty() ;
                }
            catch ( Throwable e )
                {
                writeLog( "\tactual: %s%s%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                              ? ""
                              : ": \"" + e.getMessage() + "\"" ) ;
                
                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "\tactual: %b%n", actualResult ) ;
            
            // check for the correct result
            assertEquals( expectedResult, actualResult ) ;

            // verify that will return to empty when all contents removed
            
            writeLog( "\treturn queue to empty%n" ) ;
            
            invoke( ArrayQueue.class,
                    testQueue,
                    "initializeState",
                    new Class<?>[] { int.class },
                    new Object[] { queueSize } ) ;
            
            verifyQueueState( testQueue, 0, queueSize ) ;

            // re-test isEmpty()
            final boolean verifyResult ;
            try
                {
                verifyResult = testQueue.isEmpty() ;
                }
            catch ( Throwable e )
                {
                writeLog( "\tverify: %s%s%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                              ? ""
                              : ": \"" + e.getMessage() + "\"" ) ;
                
                throw e ;   // re-throw it
                }

            // display message describing the actual result of this test
            writeLog( "\tverify: %b%n", verifyResult ) ;

            assertTrue( verifyResult ) ;

            this.currentTestPassed = true ;
            } ) ;

        } // end testIsEmpty()


    /**
     * Test method for {@link edu.wit.dcsn.comp2000.queue.adt.ArrayQueue#clear()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = "./test-data-dmr/test-clear.data", numLinesToSkip = 1 )
    @DisplayName( "clear()" )
    @Order( 200200 )
    final void testClear( final boolean isLastTest,
                          final boolean isStubBehavior,
                          final String encodedTestConfiguration,
                          final TestInfo testInfo )
        {
        final Object[][] queueContents = startTest( testInfo,
                                                    isLastTest,
                                                    isStubBehavior,
                                                    new String[] { "contents" },
                                                    encodedTestConfiguration ) ;

        // execute the test
        assertTimeoutPreemptively( testTimeLimit, () ->
            {
            // determine expected result
            final int testSize = Math.min( queueContents[ 0 ].length,
                                           MAX_CAPACITY ) ;
            final int queueSize = Math.max( testSize,
                                            DEFAULT_CAPACITY ) ;

            // display message describing the expected result of this test
            writeLog( "\texpect: %s%n", "[]" ) ;

            // instantiate testQueue
            final QueueInterface<Object> testQueue =
                                            new ArrayQueue<>( queueSize ) ;

            // populate it
            populateQueue( testQueue, queueContents[ 0 ] ) ;
            
            verifyQueueState( testQueue, testSize, queueSize ) ;

            // clear it
            try
                {
                testQueue.clear() ;
                }
            catch ( Throwable e )
                {
                writeLog( "\tactual: %s%s%n",
                          e.getClass().getSimpleName(),
                          e.getMessage() == null
                              ? ""
                              : ": \"" + e.getMessage() + "\"" ) ;
                
                throw e ;   // re-throw it
                }

            // determine the contents of the queue
            final Object[] actualResult = getContentsOfCircularArrayBackedCollection( testQueue, "queue" ) ;

            // display message describing the actual result of this test
            writeLog( "\tactual: %s%n", arrayToString( actualResult ) ) ;

            // verify that it's empty
            assertEquals( 0, actualResult.length ) ;
            
            // verify empty
            verifyQueueState( testQueue, 0, queueSize ) ;

            this.currentTestPassed = true ;
            } ) ;

        } // end testClear()
    
    
    // private method tests


    /**
     * Test method for
     * {@link edu.wit.dcsn.comp2000.queue.adt.ArrayQueue#checkCapacity(int)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 400100 )
    @Disabled                               // FUTURE
    final void testCheckCapacity( final boolean isLastTest,
                                  final boolean isStubBehavior,
                                  final String encodedTestConfiguration,
                                  final TestInfo testInfo )
        {
        final Object[][] decodedTestConfiguration =
                                        startTest( testInfo,
                                                   isLastTest,
                                                   isStubBehavior,
                                                   encodedTestConfiguration ) ;

        // STUB

        } // end testCheckCapacity()


    /**
     * Test method for
     * {@link edu.wit.dcsn.comp2000.queue.adt.ArrayQueue#initializeState(int)}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 400200 )
    @Disabled                               // FUTURE
    final void testInitializeState( final boolean isLastTest,
                                    final boolean isStubBehavior,
                                    final String encodedTestConfiguration,
                                    final TestInfo testInfo )
        {
        final Object[][] decodedTestConfiguration =
                                        startTest( testInfo,
                                                   isLastTest,
                                                   isStubBehavior,
                                                   encodedTestConfiguration ) ;

        // STUB

        } // end testInitializeState()


    /**
     * Test method for {@link edu.wit.dcsn.comp2000.queue.adt.ArrayQueue#isArrayFull()}.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @Test
    @Order( 400300 )
    @Disabled                               // FUTURE
    final void testIsArrayFull( final boolean isLastTest,
                                final boolean isStubBehavior,
                                final String encodedTestConfiguration,
                                final TestInfo testInfo )
        {
        final Object[][] decodedTestConfiguration =
                                        startTest( testInfo,
                                                   isLastTest,
                                                   isStubBehavior,
                                                   encodedTestConfiguration ) ;

        // STUB

        } // end testIsArrayFull()
    

    // functionality tests


    /**
     * Test method for empty queue.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param encodedTestConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = "./test-data-dmr/test-empty-queue.data",
                    numLinesToSkip = 1 )
    @Order( 999992 )
    @DisplayName( "Empty Queue" )
    void testEmptyQueue( final boolean isLastTest,
                         final boolean isStubBehavior,
                         final String encodedTestConfiguration,
                         final TestInfo testInfo )
        {
        final Object[][] decodedTestConfiguration =
                                        startTest( testInfo,
                                                   isLastTest,
                                                   isStubBehavior,
                                                   new String[] { "initial capacity" },
                                                   encodedTestConfiguration ) ;
        
        assertTimeoutPreemptively( testTimeLimit, () ->
            {
            final ArrayQueueCapacity testCapacity =
                            ArrayQueueCapacity.interpretDescription(
                                    (String) decodedTestConfiguration[ 0 ][ 0 ] ) ;
            final int testSize = testCapacity.capacityValue ;

            writeLog( "\tTesting: new ArrayQueue(%,d)%n", testSize ) ;

            final QueueInterface<Object> testQueue =
                                            new ArrayQueue<>( testSize ) ;
            
            writeLog( "\tverifying state%n" );
            verifyQueueState( testQueue, 0, testSize ) ;

            writeLog( "\ttesting: isEmpty()%n" ) ;
            assertTrue( testQueue.isEmpty() ) ;

            writeLog( "\ttesting: clear()%n" ) ;
            testQueue.clear() ;
            assertTrue( testQueue.isEmpty() ) ;

            writeLog( "\ttesting: getFront()%n" ) ;
            assertThrows( EmptyQueueException.class, () -> testQueue.getFront() ) ;

            writeLog( "\ttesting: dequeue()%n" ) ;
            assertThrows( EmptyQueueException.class, () -> testQueue.dequeue() ) ;
            
            this.currentTestPassed = true ;
            } ) ;

        }   // end testEmptyQueue()


    /**
     * Test method for full queue.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param initialCapacityConfiguration
     *     encoded test configuration
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = "./test-data-dmr/test-full-queue.data",
                    numLinesToSkip = 1 )
    @Order( 999993 )
    @DisplayName( "Full Queue" )
    void testFullQueue( final boolean isLastTest,
                        final boolean isStubBehavior,
                        final String initialCapacityConfiguration,
                        final TestInfo testInfo )
        {
        final Object[][] decodedTestConfiguration =
                                        startTest( testInfo,
                                                   isLastTest,
                                                   isStubBehavior,
                                                   new String[] { "initial capacity" },
                                                   initialCapacityConfiguration ) ;

        assertTimeoutPreemptively( testTimeLimit, () ->
            {
            final ArrayQueueCapacity testCapacity =
                            ArrayQueueCapacity.interpretDescription(
                                    (String) decodedTestConfiguration[ 0 ][ 0 ] ) ;
            final int testSize = testCapacity.capacityValue ;

            writeLog( "\tTesting: new ArrayQueue(%,d)%n", testSize ) ;

            final QueueInterface<Object> testQueue =
                                            new ArrayQueue<>( testSize ) ;
            
            verifyQueueState( testQueue, 0, testSize ) ;

            // fill it
            writeLog( "\t...filling: 0..%,d%n", testSize - 1 ) ;
            for ( int i = 0 ; i < testSize ; i++ )
                {
                testQueue.enqueue( i ) ;
                }
            
            verifyQueueState( testQueue, testSize, testSize ) ;

            // empty it
            writeLog( "\t...emptying: 0..%,d%n", testSize - 1 ) ;
            for ( int i = 0 ; i < testSize ; i++ )
                {
                assertEquals( i, (int) testQueue.getFront() ) ;
                assertEquals( i, (int) testQueue.dequeue() ) ;
                }
            
            verifyQueueState( testQueue, 0, testSize ) ;
            
            // advance pointers
            writeLog( "\t...advancing indices%n" );
            for ( int i = 0; i < testSize / 2; i++ )
                {
                testQueue.enqueue( i ) ;
                assertEquals( i, testQueue.dequeue() ) ;
                }
            
            verifyQueueState( testQueue, 0, testSize ) ;

            // re-fill it
            writeLog( "\t...re-filling: 0..%,d%n", testSize - 1 ) ;
            for ( int i = 0 ; i < testSize ; i++ )
                {
                testQueue.enqueue( i ) ;
                }
            
            verifyQueueState( testQueue, testSize, testSize ) ;

            // empty it
            writeLog( "\t...emptying: 0..%,d%n", testSize - 1 ) ;
            for ( int i = 0 ; i < testSize ; i++ )
                {
                assertEquals( i, (int) testQueue.getFront() ) ;
                assertEquals( i, (int) testQueue.dequeue() ) ;
                }

            assertTrue( testQueue.isEmpty() ) ;
            
            verifyQueueState( testQueue, 0, testSize ) ;

            this.currentTestPassed = true ;

            } ) ;

        }   // end testFullQueue()


    /**
     * Test method for queue growth.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param initialCapacityConfiguration
     *     initial queue capacity/array size
     * @param fillToCapacityConfiguration
     *     size to which the queue/array will be filled
     * @param offsetFillConfiguration 
     *     flag to indicate if filling the queue should be 0-based (false) or forced
     *     to wrap (true)
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = "./test-data-dmr/test-queue-growth.data",
                    numLinesToSkip = 1 )
    @Order( 999994 )
    @DisplayName( "Queue Growth" )
    void testQueueGrowth( final boolean isLastTest,
                          final boolean isStubBehavior,
                          final String initialCapacityConfiguration,
                          final String fillToCapacityConfiguration,
                          final String offsetFillConfiguration,
                          final TestInfo testInfo )
        {
        final Object[][] decodedTestConfiguration =
                                        startTest( testInfo,
                                                   isLastTest,
                                                   isStubBehavior,
                                                   new String[] { "initial capacity", 
                                                            "fill-to capacity",
                                                            "offset filling queue"},
                                                   initialCapacityConfiguration,
                                                   fillToCapacityConfiguration,
                                                   offsetFillConfiguration ) ;

        assertTimeoutPreemptively( testTimeLimit, () ->
            {
            // initial queue/array capacity
            final ArrayQueueCapacity testInitialCapacity =
                            ArrayQueueCapacity.interpretDescription(
                                    (String) decodedTestConfiguration[ 0 ][ 0 ] ) ;
            final int testInitialSize = testInitialCapacity.capacityValue ;

            writeLog( "\tTesting: new ArrayQueue(%,d)%n", testInitialSize ) ;

            final QueueInterface<Object> testQueue =
                                            new ArrayQueue<>( testInitialSize ) ;
            
            verifyQueueState( testQueue, 0, testInitialSize ) ;

            
            // fill-to capacity
            final ArrayQueueCapacity testGrowToCapacity =
                            ArrayQueueCapacity.interpretDescription(
                                    (String) decodedTestConfiguration[ 1 ][ 0 ] ) ;
            final int testFillToSize = testGrowToCapacity.capacityValue ;
            
            int testFilledCapacity = testInitialSize ;
            while ( testFilledCapacity < testFillToSize )
                {
                testFilledCapacity *= 2 ;
                }

            writeLog( "\tfill-to: %,d%n", testFillToSize ) ;

            
            // fill offset or aligned?
            final boolean offsetFill = (boolean) decodedTestConfiguration[ 2 ][ 0 ] ;
            
            if ( offsetFill )
                {
                // advance the internal pointers approximately 1/2 way
                final int offsetDistance = testInitialSize / 2 ;
                writeLog( "\t...advancing pointers by %,d elements%n", offsetDistance ) ;
                for ( int i = 0 ; i < offsetDistance ; i++ )
                    {
                    testQueue.enqueue( i ) ;
                    testQueue.dequeue() ;
                    }
    
                assertTrue( testQueue.isEmpty() ) ;
                
                verifyQueueState( testQueue, 0, testInitialSize ) ;
                }
            

            // fill it
            writeLog( "\t...filling queue: 0..%,d%n", testFillToSize ) ;
            for ( int i = 0 ; i < testFillToSize ; i++ )
                {
                testQueue.enqueue( i ) ;
                }
            
            verifyQueueState( testQueue, testFillToSize, testFilledCapacity ) ;

            // empty it
            writeLog( "\t...emptying queue: 0..%,d%n", testFillToSize ) ;
            for ( int i = 0 ; i < testFillToSize ; i++ )
                {
                assertEquals( i, (int) testQueue.getFront() ) ;
                assertEquals( i, (int) testQueue.dequeue() ) ;
                }

            
            // verify that it's empty
            writeLog( "\tverifying empty%n" );
            assertTrue( testQueue.isEmpty() ) ;
            
            verifyQueueState( testQueue, 0, testFilledCapacity ) ;


            // success
            this.currentTestPassed = true ;
            
            } ) ;

        }   // end testQueueGrowth()


    /**
     * Test method for multiple queues.
     *
     * @param isLastTest
     *     flag to indicate that this is the last dataset for this test
     * @param isStubBehavior
     *     flag to indicate that the result of testing this dataset matches the
     *     stubbed behavior
     * @param passConfiguration
     *     temporary fix
     * @param testInfo
     *     info about the test
     */
    @ParameterizedTest( name = "{displayName}:: [{index}] {arguments}" )
    @CsvFileSource( resources = "./test-data-dmr/test-multiple-queues.data",
                    numLinesToSkip = 1 )
    @Order( 999995 )
    @DisplayName( "Multiple Queues" )
    void testMultipleQueues( final boolean isLastTest,
                             final boolean isStubBehavior,
                             final String passConfiguration,
                             final TestInfo testInfo )
        {
        final Object[][] decodedTestConfiguration =
                                        startTest( testInfo,
                                                   isLastTest,
                                                   isStubBehavior,
                                                   new String[] { "pass" },
                                                   passConfiguration ) ;

        assertTimeoutPreemptively( testTimeLimit, () ->
            {
            QueueInterface<Integer> testQueue1 ;
            QueueInterface<Integer> testQueue2 ;

            final Integer testValue37 = new Integer( 37 ) ;
            final Integer testValue42 = new Integer( 42 ) ;

            /* ----- */

            int passNumber = (int) (long) decodedTestConfiguration[ 0 ][ 0 ] ;
            switch ( passNumber )
                {
                case 1:
                    /* @formatter:off
                     * 
                     * - instantiate 2 queues
                     * - add an item to one queue
                     * - make sure it contains the item and other is still empty
                     * - repeat test with opposite queues
                     * - repeat test with both queues simultaneously
                     * - remove the items and make sure both queues are empty
                     * 
                     * @formatter:on
                     */
                    writeLog( "Testing: multiple queue instances (%,d)%n", passNumber ) ;
        
                    writeLog( "\t...instantiate 2 queues%n" ) ;
                    testQueue1 = null ;  // reset the pointer
                    testQueue1 = new ArrayQueue<>( DEFAULT_CAPACITY ) ;
        
                    testQueue2 = null ;  // reset the pointer
                    testQueue2 = new ArrayQueue<>( MEDIUM_CAPACITY ) ;
                    
                    verifyQueueState( testQueue1, 0, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;
        
                    // add an item to testQueue1
                    writeLog( "\t...enqueue 1 item onto queue 1%n" ) ;
                    testQueue1.enqueue( testValue42 ) ;
        
                    writeLog( "\t...test for item on queue 1%n" ) ;
                    assertFalse( testQueue1.isEmpty() ) ;
                    assertEquals( testValue42, testQueue1.getFront() ) ;
        
                    // testQueue2 must still be empty
                    writeLog( "\t...test queue 2 for empty%n" ) ;
                    assertTrue( testQueue2.isEmpty() ) ;
                    
                    verifyQueueState( testQueue1, 1, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;
        
                    // we can remove the item from testQueue1 and both queues are now empty
                    writeLog( "\t...dequeue item from queue 1%n" ) ;
                    assertEquals( testValue42, testQueue1.dequeue() ) ;
        
                    writeLog( "\t...verify both queues empty%n" ) ;
                    assertTrue( testQueue1.isEmpty() ) ;
                    assertTrue( testQueue2.isEmpty() ) ;
                    
                    verifyQueueState( testQueue1, 0, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;
        
                    // add an item to testQueue2
                    writeLog( "\t...enqueue 1 item onto queue 2%n" ) ;
                    testQueue2.enqueue( testValue37 ) ;
        
                    writeLog( "\t...test for item on queue 2%n" ) ;
                    assertFalse( testQueue2.isEmpty() ) ;
                    assertEquals( testValue37, testQueue2.getFront() ) ;
        
                    // testQueue1 must still be empty
                    writeLog( "\t...test queue 1 for empty%n" ) ;
                    assertTrue( testQueue1.isEmpty() ) ;
                    
                    verifyQueueState( testQueue1, 0, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 1, MEDIUM_CAPACITY ) ;
        
                    // we can remove the item from testQueue2 and both queues are now empty
                    writeLog( "\t...dequeue item from queue 2%n" ) ;
                    assertEquals( testValue37, testQueue2.dequeue() ) ;
        
                    writeLog( "\t...verify both queues empty%n" ) ;
                    assertTrue( testQueue1.isEmpty() ) ;
                    assertTrue( testQueue2.isEmpty() ) ;
                    
                    verifyQueueState( testQueue1, 0, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;
        
                    // add an item to testQueue1
                    writeLog( "\t...enqueue 1 item onto each queue%n" ) ;
                    testQueue1.enqueue( testValue42 ) ;
                    testQueue2.enqueue( testValue37 ) ;
        
                    writeLog( "\t...test for correct items on each queue%n" ) ;
                    assertFalse( testQueue1.isEmpty() ) ;
                    assertEquals( testValue42, testQueue1.getFront() ) ;
                    assertFalse( testQueue2.isEmpty() ) ;
                    assertEquals( testValue37, testQueue2.getFront() ) ;
                    
                    verifyQueueState( testQueue1, 1, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 1, MEDIUM_CAPACITY ) ;
        
                    // we can remove the items from each and both queues are now empty
                    writeLog( "\t...dequeue items from each queue%n" ) ;
                    assertEquals( testValue42, testQueue1.dequeue() ) ;
                    assertEquals( testValue37, testQueue2.dequeue() ) ;
        
                    writeLog( "\t...verify both queues empty%n" ) ;
                    assertTrue( testQueue1.isEmpty() ) ;
                    assertTrue( testQueue2.isEmpty() ) ;
                    
                    verifyQueueState( testQueue1, 0, DEFAULT_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;
    
                    break ;
    
                case 2:
                    /* @formatter:off
                     * 
                     * - instantiate queue 1
                     * - add an item to one queue
                     * - instantiate queue 2
                     * - make sure queue 1 contains the item and queue 2 is empty
                     * - remove the item from queue 1 and make sure both queues are empty
                     * 
                     * @formatter:on
                     */
                    writeLog( "Testing: multiple queue instances (2)%n" ) ;
        
                    writeLog( "\t...instantiate queue 1%n" ) ;
                    testQueue1 = null ;  // reset the pointer
                    testQueue1 = new ArrayQueue<>( MEDIUM_CAPACITY ) ;
                    
                    verifyQueueState( testQueue1, 0, MEDIUM_CAPACITY ) ;
        
                    // add an item to testQueue1
                    writeLog( "\t...enqueue 1 item onto queue 1%n" ) ;
                    testQueue1.enqueue( testValue42 ) ;
        
                    writeLog( "\t...test for item on queue 1%n" ) ;
                    assertFalse( testQueue1.isEmpty() ) ;
                    assertEquals( testValue42, testQueue1.getFront() ) ;
                    
                    verifyQueueState( testQueue1, 1, MEDIUM_CAPACITY ) ;
        
                    writeLog( "\t...instantiate queue 2%n" ) ;
                    testQueue2 = null ;  // reset the pointer
                    testQueue2 = new ArrayQueue<>( DEFAULT_CAPACITY ) ;
                    
                    verifyQueueState( testQueue1, 1, MEDIUM_CAPACITY ) ;
        
                    // testQueue2 must be empty
                    writeLog( "\t...test queue 2 for empty%n" ) ;
                    assertTrue( testQueue2.isEmpty() ) ;
                    
                    verifyQueueState( testQueue2, 0, DEFAULT_CAPACITY ) ;
        
                    // we can remove the item from testQueue1 and both queues are now empty
                    writeLog( "\t...dequeue item from queue 1%n" ) ;
                    assertEquals( testValue42, testQueue1.dequeue() ) ;
        
                    writeLog( "\t...verify both queues empty%n" ) ;
                    assertTrue( testQueue1.isEmpty() ) ;
                    assertTrue( testQueue2.isEmpty() ) ;
                    
                    verifyQueueState( testQueue1, 0, MEDIUM_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, DEFAULT_CAPACITY ) ;
    
                    break ;
    
                case 3:
                    /* @formatter:off
                     * 
                     * - instantiate 2 queues
                     * - add items to each queue
                     * - make sure each queue contains the correct items
                     * - remove the items from both queues and make sure they are both empty
                     * 
                     * @formatter:on
                     */
                    writeLog( "Testing: multiple queue instances (3)%n" ) ;
        
                    writeLog( "\t...instantiate 2 queues%n" ) ;
                    testQueue1 = null ;  // reset the pointer
                    testQueue1 = new ArrayQueue<>( SMALL_CAPACITY ) ;
        
                    testQueue2 = null ;  // reset the pointer
                    testQueue2 = new ArrayQueue<>( MEDIUM_CAPACITY ) ;
                    
                    verifyQueueState( testQueue1, 0, SMALL_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;
        
                    // add items to testQueue1
                    writeLog( "\t...enqueue multiple items onto queue 1%n" ) ;
                    for ( int i = 0 ; i < SMALL_CAPACITY ; i++ )
                        {
                        testQueue1.enqueue( QUEUE_1_BASE + i ) ;
                        }
                    
                    verifyQueueState( testQueue1, SMALL_CAPACITY, SMALL_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;
        
                    // add an items to testQueue2
                    writeLog( "\t...enqueue multiple items onto queue 2%n" ) ;
                    for ( int i = 0 ; i < MEDIUM_CAPACITY ; i++ )
                        {
                        testQueue2.enqueue( QUEUE_2_BASE + i ) ;
                        }
                    
                    verifyQueueState( testQueue1, SMALL_CAPACITY, SMALL_CAPACITY ) ;
                    verifyQueueState( testQueue2, MEDIUM_CAPACITY, MEDIUM_CAPACITY ) ;
        
                    // remove items from testQueue1
                    writeLog( "\t...test for items on queue 1%n" ) ;
                    assertFalse( testQueue1.isEmpty() ) ;
        
                    for ( int i = 0 ; i < SMALL_CAPACITY ; i++ )
                        {
                        assertEquals( new Integer( QUEUE_1_BASE + i ),
                                      testQueue1.dequeue() ) ;
                        }
        
                    assertTrue( testQueue1.isEmpty() ) ;
                    
                    verifyQueueState( testQueue1, 0, SMALL_CAPACITY ) ;
                    verifyQueueState( testQueue2, MEDIUM_CAPACITY, MEDIUM_CAPACITY ) ;
        
                    // remove items from testQueue2
                    writeLog( "\t...test for items on queue 2%n" ) ;
                    assertFalse( testQueue2.isEmpty() ) ;
        
                    for ( int i = 0 ; i < MEDIUM_CAPACITY ; i++ )
                        {
                        assertEquals( new Integer( QUEUE_2_BASE + i ),
                                      testQueue2.dequeue() ) ;
                        }
        
                    assertTrue( testQueue2.isEmpty() ) ;
                    
                    verifyQueueState( testQueue1, 0, SMALL_CAPACITY ) ;
                    verifyQueueState( testQueue2, 0, MEDIUM_CAPACITY ) ;
        
                    break ;
    
                case 4:
                    /* @formatter:off
                     * 
                     * - instantiate 2 queues
                     * - add items to each queue causing them to resize
                     * - make sure each queue contains the correct items
                     * - remove the items from both queues and make sure they are both empty
                     * 
                     * @formatter:on
                     */
                    
                    writeLog( "Testing: multiple queue instances with growth (4)%n" ) ;
        
                    writeLog( "\t...instantiate 2 queues%n" ) ;
                    testQueue1 = null ;  // reset the pointer
                    testQueue1 = new ArrayQueue<>( SMALL_CAPACITY ) ;
                    
                    verifyQueueState( testQueue1, 0, SMALL_CAPACITY );
        
                    testQueue2 = null ;  // reset the pointer
                    testQueue2 = new ArrayQueue<>( DEFAULT_CAPACITY ) ;
                    
                    verifyQueueState( testQueue1, 0, SMALL_CAPACITY );
                    verifyQueueState( testQueue2, 0, DEFAULT_CAPACITY ) ;
                    
                    int testFilledCapacity1 = SMALL_CAPACITY ;
                    while ( testFilledCapacity1 < LARGE_CAPACITY )
                        {
                        testFilledCapacity1 *= 2 ;
                        }
                    
                    int testFilledCapacity2 = DEFAULT_CAPACITY ;
                    while ( testFilledCapacity2 < LARGE_CAPACITY )
                        {
                        testFilledCapacity2 *= 2 ;
                        }
        
                    // add items to testQueue1
                    writeLog( "\t...enqueue multiple items onto queue 1%n" ) ;
                    for ( int i = 0 ; i < LARGE_CAPACITY ; i++ )
                        {
                        testQueue1.enqueue( QUEUE_1_BASE + i ) ;
                        }
                    
                    verifyQueueState( testQueue1, LARGE_CAPACITY, testFilledCapacity1 );
                    verifyQueueState( testQueue2, 0, DEFAULT_CAPACITY ) ;
        
                    // add an items to testQueue2
                    writeLog( "\t...enqueue multiple items onto queue 2%n" ) ;
                    for ( int i = 0 ; i < LARGE_CAPACITY ; i++ )
                        {
                        testQueue2.enqueue( QUEUE_2_BASE + i ) ;
                        }
                    
                    verifyQueueState( testQueue1, LARGE_CAPACITY, testFilledCapacity1 );
                    verifyQueueState( testQueue2, LARGE_CAPACITY, testFilledCapacity2 ) ;
        
                    // remove items from testQueue2
                    writeLog( "\t...test for items on queue 2%n" ) ;
                    assertFalse( testQueue2.isEmpty() ) ;
        
                    for ( int i = 0 ; i < LARGE_CAPACITY ; i++ )
                        {
                        assertEquals( new Integer( QUEUE_2_BASE + i ),
                                      testQueue2.dequeue() ) ;
                        }
        
                    assertTrue( testQueue2.isEmpty() ) ;
                    
                    verifyQueueState( testQueue1, LARGE_CAPACITY, testFilledCapacity1 );
                    verifyQueueState( testQueue2, 0, testFilledCapacity2 ) ;
        
                    // remove items from testQueue1
                    writeLog( "\t...test for items on queue 1%n" ) ;
                    assertFalse( testQueue1.isEmpty() ) ;
        
                    for ( int i = 0 ; i < LARGE_CAPACITY ; i++ )
                        {
                        assertEquals( new Integer( QUEUE_1_BASE + i ),
                                      testQueue1.dequeue() ) ;
                        }
        
                    assertTrue( testQueue1.isEmpty() ) ;
                    
                    verifyQueueState( testQueue1, 0, testFilledCapacity1 );
                    verifyQueueState( testQueue2, 0, testFilledCapacity2 ) ;
        
                    break ;
    
                default :
                    fail( "unexpected pass number: " + passNumber ) ;
                
                }
            
            this.currentTestPassed = true ;
            
            } ) ;

        }   // end testMultipleQueues()

    
    // --------------------------------------------------
    //
    // The following utilities are used by the test methods
    //
    // --------------------------------------------------


    /**
     * Utility to populate a queue from the contents of an array
     *
     * @param queueToFill
     *     the queue to populate
     * @param entries
     *     the entries to add to the queueToFill
     */
    private static void populateQueue( final QueueInterface<Object> queueToFill,
                                       final Object[] entries )
        {
        if ( entries != null )
            {
            final int iterations = Math.min( entries.length, MAX_CAPACITY ) ;

            for ( int i = 0 ; i < iterations ; i++ )
                {
                queueToFill.enqueue( entries[ i ] ) ;
                }
            }

        }   // end populateQueue()
    
    
    /**
     * Test the provided ArrayQueue instance to ensure it's in a valid state:
     * <ul>
     * <li>there is a {@code queue} array
     * <li>{@code queue} is the correct length
     * <li>{@code numberOfEntries} is zero
     * <li>{@code frontIndex} is 1 greater than {@code backIndex}
     * <li>{@code integrityOK} is {@code true}
     * <li>{@code queue} array contains no {@code null} entries in used locations
     * <li>{@code queue} array contains all {@code null} entries in unused locations
     * </ul>
     * 
     * @param testQueue
     *     the {@code ArrayQueue} instance to test
     * @param expectedNumberOfEntries
     *     the number of entries that should be in the queue
     * @param expectedQueueLength
     *     the length the queue array should be
     */
    private static void verifyQueueState( final QueueInterface<?> testQueue,
                                          final int expectedNumberOfEntries,
                                          final int expectedQueueLength )
        {
        assertTrue( testQueue instanceof ArrayQueue ) ;
        
        final Object[] queue = (Object[]) getReferenceField( testQueue, "queue" ) ;
        assertNotNull( queue ) ;
        
        if ( expectedQueueLength > 0 )
            {
            assertEquals( expectedQueueLength, queue.length ) ;
            }
        
        final int numberOfEntries = getIntField( testQueue, "numberOfEntries" ) ;
        assertEquals( expectedNumberOfEntries, numberOfEntries ) ;
        
        final int frontIndex = getIntField( testQueue, "frontIndex" ) ;
        final int backIndex = getIntField( testQueue, "backIndex" ) ;
        
        if ( ( numberOfEntries == 0 ) || ( numberOfEntries == queue.length ) )
            {
            assertEquals( frontIndex, ( backIndex + 1 ) % queue.length ) ;
            }
        else
            {
            assertNotEquals( frontIndex, ( backIndex + 1 ) % queue.length ) ;
            }
        
        final boolean integrityOK = getBooleanField( testQueue, "integrityOK" ) ;
        assertTrue( integrityOK ) ;
        
        for ( int i = 0; i < numberOfEntries; i++ )
            {
            assertNotNull( queue[ ( frontIndex + i ) % queue.length ] ) ;
            }
        
        for ( int i = 0; i < queue.length - numberOfEntries; i++ )
            {
            assertNull( queue[ ( frontIndex + numberOfEntries + i ) % queue.length ] ) ;
            }
        
        }   // end 3-arg verifyQueueState()
    
    
    /**
     * Test the provided ArrayQueue instance to ensure it's in a valid state:
     * <ul>
     * <li>there is a {@code queue} array
     * <li>{@code numberOfEntries} is zero
     * <li>{@code frontIndex} is 1 greater than {@code backIndex}
     * <li>{@code integrityOK} is {@code true}
     * <li>{@code queue} array contains no {@code null} entries in used locations
     * <li>{@code queue} array contains all {@code null} entries in unused locations
     * </ul>
     * 
     * @param testQueue
     *     the {@code ArrayQueue} instance to test
     * @param expectedNumberOfEntries
     *     the number of entries that should be in the queue
     */
    private static void verifyQueueState( final QueueInterface<?> testQueue,
                                          final int expectedNumberOfEntries )
        {
        verifyQueueState( testQueue, expectedNumberOfEntries, -1 ) ;
        
        }   // end 2-arg verifyQueueState()

    }	// end class ArrayQueueDMRTests
