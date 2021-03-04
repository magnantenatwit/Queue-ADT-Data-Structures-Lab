/* @formatter:off
 *
 * Dave Rosenberg
 * Comp 2000 - Data Structures
 * Lab: Testing infrastructure
 * Fall, 2020
 * 
 * Usage restrictions:
 * 
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course).
 * 
 * Further, you may not post nor otherwise share this code with anyone other than
 * current students in my sections of this course. Violation of these usage
 * restrictions will be considered a violation of the Wentworth Institute of
 * Technology Academic Honesty Policy.
 *
 * Do not remove this notice.
 *
 * @formatter:on
 */

package edu.wit.dcsn.dmr.testing.junit;

import java.lang.reflect.Field ;
import java.lang.reflect.InvocationTargetException ;
import java.lang.reflect.Method ;
import java.lang.reflect.Type ;
import java.util.Arrays ;

/**
 * Utilities to access and manipulate classes under test using the reflection
 * mechanism
 *
 * @author Dave Rosenberg
 * @version 1.0.0 2020-08-08 Initial implementation - extracted from
 *     DMRJUnitTestsBase.java
 * @version 1.1.0 2020-09-13 enhance {@code invoke()} to check both defined and 
 *     generic parameter types when searching for a matching method
 * @version 1.2.0 2020-10-19 add circular array retrieval
 */
public class Reflection
    {
    
    /**
     * Utility to copy an array-backed collection's contents into an array
     * 
     * @param collectionToCopy
     *     the collection to copy
     * @param backingStoreFieldName
     *     field name of the collection's backing store
     * @return array of the contents of {@code collectionToCopy} or {@code null} if
     *     {@code collectionToCopy} is {@code null}
     */
    public static Object[] getContentsOfArrayBackedCollection(
                                       final Object collectionToCopy,
                                       final String backingStoreFieldName )
        {
        return getContentsOfArrayBackedCollection( collectionToCopy,
                                                   backingStoreFieldName,
                                                   null,
                                                   -1 ) ;
        
        }   // end getContentsOfArrayBackedCollection() pass-through
    

    /**
     * Utility to copy an array-backed collection's contents into an array
     * 
     * @param collectionToCopy
     *     the collection to copy
     * @param backingStoreFieldName
     *     field name of the collection's backing store
     * @param entryCountFieldName
     *     field name of the collection's entry count (optional - defaults to
     *     "numberOfEntries")
     * @param entryCount
     *     expected number of entries in the collection - only used if non-negative
     * @return array of the contents of {@code collectionToCopy} or {@code null} if
     *     {@code collectionToCopy} is {@code null}
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static Object[] getContentsOfArrayBackedCollection(
                                       final Object collectionToCopy,
                                       final String backingStoreFieldName,
                                       String entryCountFieldName,
                                       int entryCount )
                                throws TestingException
        {
        Object[] collectionContents = null ;

        if ( collectionToCopy != null )
            {
            // handle optional parameters
            if ( entryCountFieldName == null )
                {
                entryCountFieldName = "numberOfEntries" ;
                }
            
            // get current entry count from the collection if not provided
            if ( entryCount < 0 )
                {
                try
                    {
                    entryCount = getIntField( collectionToCopy,
                                              entryCountFieldName ) ;
                    }
                catch ( IllegalArgumentException | SecurityException e )
                    {
                    String errorMessage = String.format( "Failed to retrieve entry count from class %s, field %s, instance %s: %s%s",
                                                         collectionToCopy.getClass()
                                                                         .getSimpleName(),
                                                         entryCountFieldName,
                                                         collectionToCopy.toString(),
                                                         e.getClass()
                                                          .getSimpleName(),
                                                         ( e.getMessage() == null
                                                             ? ""
                                                             : ": " +
                                                               e.getMessage() ) ) ;

                    throw new TestingException( errorMessage, e ) ;
                    }
                }

            // collect the contents of the collection
            try
                {
                collectionContents =
                    Arrays.copyOf( (Object[]) getReferenceField( collectionToCopy,
                                                                 backingStoreFieldName ),
                                   entryCount ) ;
                }
            catch ( IllegalArgumentException | SecurityException ex )
                {
                String errorMessage = String.format( "Failed to retrieve backing array from class %s, field %s, instance %s: %s%s",
                                                     collectionToCopy.getClass().getSimpleName(),
                                                     backingStoreFieldName,
                                                     collectionToCopy.toString(),
                                                     ex.getClass().getSimpleName(),
                                                     ( ex.getMessage() == null
                                                         ? "" 
                                                         : ": " + ex.getMessage() ) ) ;
                
                throw new TestingException( errorMessage, ex ) ;
                }
            }

        return collectionContents ;

        }   // end getContentsOfArrayBackedCollection()
    
    /**
     * Utility to copy a circular-array-backed collection's contents into an array
     * 
     * @param collectionToCopy
     *     the collection to copy
     * @param backingStoreFieldName
     *     field name of the collection's backing store
     * @return array of the contents of {@code collectionToCopy} or {@code null} if
     *     {@code collectionToCopy} is {@code null}
     */
    public static Object[] getContentsOfCircularArrayBackedCollection(
                                       final Object collectionToCopy,
                                       final String backingStoreFieldName )
        {
        return getContentsOfCircularArrayBackedCollection( collectionToCopy,
                                                           backingStoreFieldName,
                                                           null,
                                                           null,
                                                           null,
                                                           -1 ) ;
        
        }   // end getContentsOfCircularArrayBackedCollection() pass-through
    

    /**
     * Utility to copy a circular-array-backed collection's contents into an array
     * 
     * @param collectionToCopy
     *     the collection to copy
     * @param backingStoreFieldName
     *     field name of the collection's backing store
     * @param frontIndexFieldName
     *     field name of the collection's front index (optional - defaults to "frontIndex"
     * @param backIndexFieldName
     *     field name of the collection's back index (optional - defaults to "backIndex"
     * @param entryCountFieldName
     *     field name of the collection's entry count (optional - defaults to
     *     "numberOfEntries")
     * @param entryCount
     *     expected number of entries in the collection - only used if non-negative
     * @return array of the contents of {@code collectionToCopy} or {@code null} if
     *     {@code collectionToCopy} is {@code null}
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static Object[] getContentsOfCircularArrayBackedCollection(
                                       final Object collectionToCopy,
                                       final String backingStoreFieldName,
                                       String frontIndexFieldName,
                                       String backIndexFieldName,
                                       String entryCountFieldName,
                                       int entryCount )
                                throws TestingException
        {
        Object[] collectionContents = null ;

        if ( collectionToCopy != null )
            {
            // handle optional parameters
            if ( entryCountFieldName == null )
                {
                entryCountFieldName = "numberOfEntries" ;
                }
            
            if ( frontIndexFieldName == null )
                {
                frontIndexFieldName = "frontIndex" ;
                }
            
            if ( backIndexFieldName == null )
                {
                backIndexFieldName = "backIndex" ;
                }
            
            // get current entry count from the collection if not provided
            if ( entryCount < 0 )
                {
                try
                    {
                    entryCount = getIntField( collectionToCopy,
                                              entryCountFieldName ) ;
                    }
                catch ( IllegalArgumentException | SecurityException e )
                    {
                    String errorMessage = String.format( "Failed to retrieve entry count from class %s, field %s, instance %s: %s%s",
                                                         collectionToCopy.getClass()
                                                                         .getSimpleName(),
                                                         entryCountFieldName,
                                                         collectionToCopy.toString(),
                                                         e.getClass()
                                                          .getSimpleName(),
                                                         ( e.getMessage() == null
                                                             ? ""
                                                             : ": " +
                                                               e.getMessage() ) ) ;

                    throw new TestingException( errorMessage, e ) ;
                    }
                }

            int frontIndex ;
            try
                {
                frontIndex = getIntField( collectionToCopy, frontIndexFieldName ) ;
                }
            catch ( IllegalArgumentException | SecurityException e )
                {
                String errorMessage = String.format( "Failed to retrieve front index from class %s, field %s, instance %s: %s%s",
                                                     collectionToCopy.getClass()
                                                                     .getSimpleName(),
                                                     frontIndexFieldName,
                                                     collectionToCopy.toString(),
                                                     e.getClass().getSimpleName(),
                                                     ( e.getMessage() == null
                                                         ? ""
                                                             : ": " +
                                                               e.getMessage() ) ) ;

                throw new TestingException( errorMessage, e ) ;
                }

            int backIndex ;
            try
                {
                backIndex = getIntField( collectionToCopy, backIndexFieldName ) ;
                }
            catch ( IllegalArgumentException | SecurityException e )
                {
                String errorMessage = String.format( "Failed to retrieve back index from class %s, field %s, instance %s: %s%s",
                                                     collectionToCopy.getClass()
                                                                     .getSimpleName(),
                                                     backIndexFieldName,
                                                     collectionToCopy.toString(),
                                                     e.getClass().getSimpleName(),
                                                     ( e.getMessage() == null
                                                         ? ""
                                                             : ": " +
                                                               e.getMessage() ) ) ;

                throw new TestingException( errorMessage, e ) ;
                }

            Object[] backingStoreArray ;
            try
                {
                backingStoreArray = (Object[]) getReferenceField( collectionToCopy,
                                                                  backingStoreFieldName ) ;
                }
            catch ( IllegalArgumentException | SecurityException e )
                {
                String errorMessage = String.format( "Failed to retrieve backing store array from class %s, field %s, instance %s: %s%s",
                                                     collectionToCopy.getClass()
                                                                     .getSimpleName(),
                                                     backingStoreFieldName,
                                                     collectionToCopy.toString(),
                                                     e.getClass().getSimpleName(),
                                                     ( e.getMessage() == null
                                                         ? ""
                                                             : ": " +
                                                               e.getMessage() ) ) ;

                throw new TestingException( errorMessage, e ) ;
                }

            // collect the contents of the collection
            try
                {
                // instantiate an array sized according to the entry count
                collectionContents = new Object[ entryCount ] ;

                if ( entryCount > 0 )
                    {
                    // copy the front elements
                    System.arraycopy( backingStoreArray,            // from
                                      frontIndex,
                                      collectionContents,           // to
                                      0,
                                      backingStoreArray.length - frontIndex ) ;
                                                                    // how many elements

                    // copy the back elements
                    System.arraycopy( backingStoreArray,            // from
                                      0,
                                      collectionContents,           // to
                                      backingStoreArray.length - frontIndex,
                                      frontIndex ) ;                // how many elements
                    }
                }
            catch ( IllegalArgumentException | SecurityException ex )
                {
                String errorMessage = String.format( "Failed to retrieve backing array from class %s, field %s, instance %s: %s%s",
                                                     collectionToCopy.getClass()
                                                                     .getSimpleName(),
                                                     backingStoreFieldName,
                                                     collectionToCopy.toString(),
                                                     ex.getClass().getSimpleName(),
                                                     ( ex.getMessage() == null
                                                         ? ""
                                                         : ": " +
                                                           ex.getMessage() ) ) ;

                throw new TestingException( errorMessage, ex ) ;
                }
            catch ( ArrayIndexOutOfBoundsException ex )
                {
                String errorMessage = String.format( "Failed to retrieve backing array from class %s, field %s, instance %s: inconsistent instance state: %s%s",
                                                     collectionToCopy.getClass()
                                                                     .getSimpleName(),
                                                     backingStoreFieldName,
                                                     collectionToCopy.toString(),
                                                     ex.getClass().getSimpleName(),
                                                     ( ex.getMessage() == null
                                                         ? ""
                                                         : ": " +
                                                           ex.getMessage() ) ) ;

                throw new TestingException( errorMessage, ex ) ;
                }
            }

        return collectionContents ;

        }   // end getContentsOfCircularArrayBackedCollection()
    

    /**
     * Utility to copy a chain-backed collection's contents into an array
     * 
     * @param collectionToCopy
     *     the collection to copy
     * @return array of the contents of {@code collectionToCopy} or {@code null} if
     *     {@code collectionToCopy} is {@code null}
     */
    public static Object[] getContentsOfChainBackedCollection(
                                       final Object collectionToCopy )
        {
        return getContentsOfChainBackedCollection( collectionToCopy,
                                                   "firstNode" ) ;

        }   // end getContentsOfChainBackedCollection()
    

    /**
     * Utility to copy a chain-backed collection's contents into an array
     * 
     * @param collectionToCopy
     *     the collection to copy
     * @param backingStoreFieldName
     *     field name of the collection's backing store
     * @return array of the contents of {@code collectionToCopy} or {@code null} if
     *     {@code collectionToCopy} is {@code null}
     */
    public static Object[] getContentsOfChainBackedCollection(
                                       final Object collectionToCopy,
                                       final String backingStoreFieldName )
        {
        return getContentsOfChainBackedCollection( collectionToCopy,
                                                   backingStoreFieldName,
                                                   null,
                                                   -1,
                                                   null,
                                                   null ) ;

        }   // end getContentsOfChainBackedCollection()
    

    /**
     * Utility to copy a chain-backed collection's contents into an array
     * 
     * @param collectionToCopy
     *     the collection to copy
     * @param backingStoreFieldName
     *     field name of the collection's backing store
     * @param entryCountFieldName
     *     field name of the collection's entry count (optional - defaults to 
     *     "numberOfEntries")
     * @param entryCount
     *     expected number of entries in the collection - only used if non-negative
     * @param nodeDataFieldName
     *     field name of the collection's node's data reference (optional - defaults 
     *     to "data")
     * @param nodeNextFieldName
     *     field name of the collection's node's next reference (optional - defaults
     *     to "next")
     * @return array of the contents of {@code collectionToCopy} or {@code null} if
     *     {@code collectionToCopy} is {@code null}
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static Object[] getContentsOfChainBackedCollection(
                                       final Object collectionToCopy,
                                       final String backingStoreFieldName,
                                       String entryCountFieldName,
                                       int entryCount,
                                       String nodeDataFieldName,
                                       String nodeNextFieldName )
                                throws TestingException
        {
        Object[] collectionContents = null ;

        if ( collectionToCopy != null )
            {
            // handle optional parameters
            if ( entryCountFieldName == null )
                {
                entryCountFieldName = "numberOfEntries" ;
                }
            
            if ( nodeDataFieldName == null )
                {
                nodeDataFieldName = "data" ;
                }
            
            if ( nodeNextFieldName == null )
                {
                nodeNextFieldName = "next" ;
                }
            
            // get current entry count from the collection if not provided
            if ( entryCount < 0 )
                {
                try
                    {
                    entryCount = getIntField( collectionToCopy,
                                              entryCountFieldName ) ;
                    }
                catch ( IllegalArgumentException | SecurityException e )
                    {
                    String errorMessage = String.format( "Failed to retrieve entry count from class %s, field %s, instance %s: %s%s",
                                                         collectionToCopy.getClass()
                                                                         .getSimpleName(),
                                                         entryCountFieldName,
                                                         collectionToCopy.toString(),
                                                         e.getClass()
                                                          .getSimpleName(),
                                                         ( e.getMessage() == null
                                                             ? ""
                                                                 : ": " +
                                                                   e.getMessage() ) ) ;

                    throw new TestingException( errorMessage, e ) ;
                    }
                }

            // instantiate an array to hold the collection's contents
            collectionContents = new Object[ entryCount ] ;

            // collect the contents of the bag
            try
                {
                Object currentNode = getReferenceField( collectionToCopy, 
                                                        backingStoreFieldName ) ;
                int i = 0 ;
            
                while ( currentNode != null )
                    {
                    collectionContents[ i++ ] = getReferenceField( currentNode, "data" ) ;
                    currentNode = getReferenceField( currentNode, "next" ) ;
                    }
                }
            catch ( IllegalArgumentException | SecurityException ex )
                {
                String errorMessage = String.format( "Failed to retrieve backing chain from class %s, field %s, instance %s: %s%s",
                                                     collectionToCopy.getClass()
                                                              .getSimpleName(),
                                                     backingStoreFieldName,
                                                     collectionToCopy.toString(),
                                                     ex.getClass().getSimpleName(),
                                                     ( ex.getMessage() == null
                                                         ? ""
                                                         : ": " + ex.getMessage() ) ) ;

                throw new TestingException( errorMessage, ex ) ;
                }
            }

        return collectionContents ;

        }   // end getContentsOfChainBackedCollection()

    
    // --------------------------------------------------
    //
    // This section contains utilities to use reflection to interrogate 
    // the class under test
    //
    // --------------------------------------------------
    

    /*
     * utility getter/setter methods for data fields
     */
    
    
    /**
     * Retrieve the value of a named reference field from an instance
     *
     * @param anInstance
     *     the instance to interrogate
     * @param fieldName
     *     the name of the field in {@code anInstance} to retrieve
     * @param fieldType
     *     the type of {@code fieldName}
     * @return the value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    private static Field getField( Object anInstance,
                                   String fieldName,
                                   String fieldType )
                                throws TestingException
        {
        Class <?> theClass = anInstance.getClass() ;
        
        try
            {
            Field theField = theClass.getDeclaredField( fieldName ) ;
            theField.setAccessible( true ) ;
            
            return theField ;
            
            }
        catch ( NoSuchFieldException |
                SecurityException ex )
            {
            String errorMessage = String.format( "Failed to retrieve %s value from class %s, field %s, instance %s: %s%s",
                                                 fieldType,
                                                 theClass.getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 ex.getClass().getSimpleName(),
                                                 ( ex.getMessage() == null
                                                     ? "" 
                                                     : ": " + ex.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, ex ) ;
            }
        
        }   // end getField()
    
    
    /**
     * Retrieve the value of a named boolean field from an instance
     *
     * @param anInstance
     *     the instance to interrogate
     * @param fieldName
     *     the name of the field in {@code anInstance} to retrieve
     * @return the value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static boolean getBooleanField( Object anInstance,
                                           String fieldName )
                            throws TestingException
        {
        try
            {
            return getField( anInstance, fieldName, "boolean" ).getBoolean( anInstance ) ;
            }
        catch ( IllegalArgumentException | IllegalAccessException e )
            {
            String errorMessage = String.format( "Failed to retrieve boolean value from class %s, field %s, instance %s: %s%s",
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 e.getClass().getSimpleName(),
                                                 ( e.getMessage() == null
                                                     ? "" 
                                                     : ": " + e.getMessage() ) ) ;

            throw new TestingException( errorMessage, e ) ;
            }
        
        }   // end getBooleanField()
    
    
    /**
     * Set the value of a named boolean field in a given instance
     *
     * @param anInstance
     *     the instance to update
     * @param fieldName
     *     the name of the field in {@code anInstance} to set
     * @param newValue
     *     the value to store
     * @return the previous value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static boolean setBooleanField( Object anInstance,
                                              String fieldName,
                                              boolean newValue )
                                throws TestingException
        {
        try
            {
            final Field theField = getField( anInstance, fieldName, "boolean" ) ;
            final boolean oldValue = theField.getBoolean( anInstance ) ;
            
            theField.setBoolean( anInstance, newValue ) ;
            
            return oldValue ;
            }
        catch ( IllegalAccessException | SecurityException ex )
            {
            String errorMessage = String.format( "Failed to retrieve or set boolean value from/to class %s, field %s, instance %s: %s%s", 
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 ex.getClass().getSimpleName(),
                                                 ( ex.getMessage() == null
                                                     ? "" 
                                                     : ": " + ex.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, ex ) ;
            }
        
        }   // end setBooleanField()
    
    
    /**
     * Retrieve the value of a named byte field from an instance
     *
     * @param anInstance
     *     the instance to interrogate
     * @param fieldName
     *     the name of the field in {@code anInstance} to retrieve
     * @return the value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static byte getByteField( Object anInstance,
                                        String fieldName )
                                throws TestingException
        {
        try
            {
            return getField( anInstance, fieldName, "byte" ).getByte( anInstance ) ;
            }
        catch ( IllegalArgumentException | IllegalAccessException e )
            {
            String errorMessage = String.format( "Failed to retrieve byte value from class %s, field %s, instance %s: %s%s",
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 e.getClass().getSimpleName(),
                                                 ( e.getMessage() == null
                                                     ? "" 
                                                     : ": " + e.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, e ) ;
            }
        
        }   // end getByteField()
    
    
    /**
     * Set the value of a named byte field in a given instance
     *
     * @param anInstance
     *     the instance to update
     * @param fieldName
     *     the name of the field in {@code anInstance} to set
     * @param newValue
     *     the value to store
     * @return the previous value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static byte setByteField( Object anInstance,
                                        String fieldName,
                                        byte newValue )
                                throws TestingException
        {
        try
            {
            final Field theField = getField( anInstance, fieldName, "byte" ) ;
            final byte oldValue = theField.getByte( anInstance ) ;

            theField.setByte( anInstance, newValue ) ;

            return oldValue ;
            }
        catch ( IllegalAccessException | SecurityException ex )
            {
            String errorMessage = String.format( "Failed to retrieve or set byte value from/to class %s, field %s, instance %s: %s%s", 
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 ex.getClass().getSimpleName(),
                                                 ( ex.getMessage() == null
                                                     ? "" 
                                                     : ": " + ex.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, ex ) ;
            }
        
        }   // end setByteField()
    
    
    /**
     * Retrieve the value of a named char field from an instance
     *
     * @param anInstance
     *     the instance to interrogate
     * @param fieldName
     *     the name of the field in {@code anInstance} to retrieve
     * @return the value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static char getCharField( Object anInstance,
                                        String fieldName )
                            throws TestingException
        {
        try
            {
            return getField( anInstance, fieldName, "char" ).getChar( anInstance ) ;
            }
        catch ( IllegalArgumentException | IllegalAccessException e )
            {
            String errorMessage = String.format( "Failed to retrieve char value from class %s, field %s, instance %s: %s%s",
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 e.getClass().getSimpleName(),
                                                 ( e.getMessage() == null
                                                     ? "" 
                                                     : ": " + e.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, e ) ;
            }
        
        }   // end getCharField()
    
    
    /**
     * Set the value of a named char field in a given instance
     *
     * @param anInstance
     *     the instance to update
     * @param fieldName
     *     the name of the field in {@code anInstance} to set
     * @param newValue
     *     the value to store
     * @return the previous value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static char setCharField( Object anInstance,
                                        String fieldName,
                                        char newValue )
                            throws TestingException
        {
        try
            {
            final Field theField = getField( anInstance, fieldName, "char" ) ;
            final char oldValue = theField.getChar( anInstance ) ;
            
            theField.setChar( anInstance, newValue ) ;
            
            return oldValue ;
            }
        catch ( IllegalAccessException | SecurityException ex )
            {
            String errorMessage = String.format( "Failed to retrieve or set char value from/to class %s, field %s, instance %s: %s%s", 
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 ex.getClass().getSimpleName(),
                                                 ( ex.getMessage() == null
                                                     ? "" 
                                                     : ": " + ex.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, ex ) ;
            }
        
        }   // end setCharField()
    
    
    /**
     * Retrieve the value of a named double field from an instance
     *
     * @param anInstance
     *     the instance to interrogate
     * @param fieldName
     *     the name of the field in {@code anInstance} to retrieve
     * @return the value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static double getDoubleField( Object anInstance,
                                            String fieldName )
                            throws TestingException
        {
        try
            {
            return getField( anInstance, fieldName, "double" ).getDouble( anInstance ) ;
            }
        catch ( IllegalArgumentException | IllegalAccessException e )
            {
            String errorMessage = String.format( "Failed to retrieve double value from class %s, field %s, instance %s: %s%s",
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 e.getClass().getSimpleName(),
                                                 ( e.getMessage() == null
                                                     ? "" 
                                                     : ": " + e.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, e ) ;
            }
        
        }   // end getDoubleField()
    
    
    /**
     * Set the value of a named double field in a given instance
     *
     * @param anInstance
     *     the instance to update
     * @param fieldName
     *     the name of the field in {@code anInstance} to set
     * @param newValue
     *     the value to store
     * @return the previous value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static double setDoubleField( Object anInstance,
                                            String fieldName,
                                            double newValue )
                                    throws TestingException
        {
        try
            {
            final Field theField = getField( anInstance, fieldName, "double" ) ;
            final double oldValue = theField.getDouble( anInstance ) ;

            theField.setDouble( anInstance, newValue ) ;

            return oldValue ;
            }
        catch ( IllegalAccessException | SecurityException ex )
            {
            String errorMessage = String.format( "Failed to retrieve or set double value from/to class %s, field %s, instance %s: %s%s", 
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 ex.getClass().getSimpleName(),
                                                 ( ex.getMessage() == null
                                                     ? "" 
                                                     : ": " + ex.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, ex ) ;
            }
        
        }   // end setDoubleField()
    
    
    /**
     * Retrieve the value of a named short field from an instance
     *
     * @param anInstance
     *     the instance to interrogate
     * @param fieldName
     *     the name of the field in {@code anInstance} to retrieve
     * @return the value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static float getFloatField( Object anInstance,
                                          String fieldName )
                        throws TestingException
        {
        try
            {
            return getField( anInstance, fieldName, "float" ).getFloat( anInstance ) ;
            }
        catch ( IllegalArgumentException | IllegalAccessException e )
            {
            String errorMessage = String.format( "Failed to retrieve float value from class %s, field %s, instance %s: %s%s",
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 e.getClass().getSimpleName(),
                                                 ( e.getMessage() == null
                                                     ? "" 
                                                     : ": " + e.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, e ) ;
            }
        
        }   // end getFloatField()
    
    
    /**
     * Set the value of a named float field in a given instance
     *
     * @param anInstance
     *     the instance to update
     * @param fieldName
     *     the name of the field in {@code anInstance} to set
     * @param newValue
     *     the value to store
     * @return the previous value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static float setFloatField( Object anInstance,
                                          String fieldName,
                                          float newValue )
                                    throws TestingException
        {
        try
            {
            final Field theField = getField( anInstance, fieldName, "float" ) ;
            final float oldValue = theField.getFloat( anInstance ) ;

            theField.setFloat( anInstance, newValue ) ;

            return oldValue ;
            }
        catch ( IllegalAccessException | SecurityException ex )
            {
            String errorMessage = String.format( "Failed to retrieve or set float value from/to class %s, field %s, instance %s: %s%s",
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,   anInstance.toString(),
                                                 ex.getClass().getSimpleName(),
                                                 ( ex.getMessage() == null
                                                     ? "" 
                                                     : ": " + ex.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, ex ) ;
            }
        
        }   // end setFloatField()
    
    
    /**
     * Retrieve the value of a named int field from an instance
     *
     * @param anInstance
     *     the instance to interrogate
     * @param fieldName
     *     the name of the field in {@code anInstance} to retrieve
     * @return the value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static int getIntField( Object anInstance,
                                      String fieldName )
                            throws TestingException
        {
        try
            {
            return getField( anInstance, fieldName, "int" ).getInt( anInstance ) ;
            }
        catch ( IllegalArgumentException | IllegalAccessException e )
            {
            String errorMessage = String.format( "Failed to retrieve int value from class %s, field %s, instance %s: %s%s",
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 e.getClass().getSimpleName(),
                                                 ( e.getMessage() == null
                                                     ? "" 
                                                     : ": " + e.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, e ) ;
            }
        
        }   // end getIntField()
    
    
    /**
     * Set the value of a named int field in a given instance
     *
     * @param anInstance
     *     the instance to update
     * @param fieldName
     *     the name of the field in {@code anInstance} to set
     * @param newValue
     *     the value to store
     * @return the previous value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static int setIntField( Object anInstance,
                                      String fieldName,
                                      int newValue )
                                throws TestingException
        {
        try
            {
            final Field theField = getField( anInstance, fieldName, "int" ) ;
            final int oldValue = theField.getInt( anInstance ) ;
            
            theField.setInt( anInstance, newValue ) ;
            
            return oldValue ;
            }
        catch ( IllegalAccessException | SecurityException ex )
            {
            String errorMessage = String.format( "Failed to retrieve or set int value from/to class %s, field %s, instance %s: %s%s", 
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 ex.getClass().getSimpleName(),
                                                 ( ex.getMessage() == null
                                                     ? "" 
                                                     : ": " + ex.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, ex ) ;
            }
        
        }   // end setIntField()
    
    
    /**
     * Retrieve the value of a named long field from an instance
     *
     * @param anInstance
     *     the instance to interrogate
     * @param fieldName
     *     the name of the field in {@code anInstance} to retrieve
     * @return the value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static long getLongField( Object anInstance,
                                        String fieldName )
                            throws TestingException
        {
        try
            {
            return getField( anInstance, fieldName, "long" ).getLong( anInstance ) ;
            }
        catch ( IllegalArgumentException | IllegalAccessException e )
            {
            String errorMessage = String.format( "Failed to retrieve long value from class %s, field %s, instance %s: %s%s",
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 e.getClass().getSimpleName(),
                                                 ( e.getMessage() == null
                                                     ? "" 
                                                     : ": " + e.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, e ) ;
            }
        
        }   // end getLongField()
    
    
    /**
     * Set the value of a named long field in a given instance
     *
     * @param anInstance
     *     the instance to update
     * @param fieldName
     *     the name of the field in {@code anInstance} to set
     * @param newValue
     *     the value to store
     * @return the previous value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static long setLongField( Object anInstance,
                                        String fieldName,
                                        long newValue )
                                throws TestingException
        {
        try
            {
            final Field theField = getField( anInstance, fieldName, "long" ) ;
            final long oldValue = theField.getLong( anInstance ) ;
            
            theField.setLong( anInstance, newValue ) ;
            
            return oldValue ;
            }
        catch ( IllegalAccessException | SecurityException ex )
            {
            String errorMessage = String.format( "Failed to retrieve or set long value from/to class %s, field %s, instance %s: %s%s", 
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 ex.getClass().getSimpleName(),
                                                 ( ex.getMessage() == null
                                                     ? "" 
                                                     : ": " + ex.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, ex ) ;
            }
        
        }   // end setLongField()
    
    
    /**
     * Retrieve the value of a named reference field from an instance
     *
     * @param anInstance
     *     the instance to interrogate
     * @param fieldName
     *     the name of the field in {@code anInstance} to retrieve
     * @return the value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static Object getReferenceField( Object anInstance,
                                               String fieldName )
                                throws TestingException
        {
        try
            {
            return getField( anInstance, fieldName, "reference" ).get( anInstance ) ;
            }
        catch ( IllegalArgumentException | IllegalAccessException e )
            {
            String errorMessage = String.format( "Failed to retrieve reference value from class %s, field %s, instance %s: %s%s",
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 e.getClass().getSimpleName(),
                                                 ( e.getMessage() == null
                                                     ? "" 
                                                     : ": " + e.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, e ) ;
            }
        
        }   // end getReferenceField()
    
    
    /**
     * Set the value of a named reference field in a given instance
     *
     * @param anInstance
     *     the instance to update
     * @param fieldName
     *     the name of the field in {@code anInstance} to set
     * @param newValue
     *     the value to store
     * @return the previous value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static Object setReferenceField( Object anInstance,
                                            String fieldName,
                                            Object newValue )
                                    throws TestingException
        {
        try
            {
            final Field theField = getField( anInstance, fieldName, "reference" ) ;
            final Object oldValue = theField.get( anInstance ) ;
            
            theField.set( anInstance, newValue ) ;
            
            return oldValue ;
            }
        catch ( IllegalAccessException | SecurityException ex )
            {
            String errorMessage = String.format( "Failed to retrieve or set reference value from/to class %s, field %s, instance %s: %s%s", 
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 ex.getClass().getSimpleName(),
                                                 ( ex.getMessage() == null
                                                     ? "" 
                                                     : ": " + ex.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, ex ) ;
            }
        
        }   // end setReferenceField()
    
    
    /**
     * Retrieve the value of a named short field from an instance
     *
     * @param anInstance
     *     the instance to interrogate
     * @param fieldName
     *     the name of the field in {@code anInstance} to retrieve
     * @return the value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static short getShortField( Object anInstance,
                                          String fieldName )
                            throws TestingException
        {
        try
            {
            return getField( anInstance, fieldName, "short" ).getShort( anInstance ) ;
            }
        catch ( IllegalArgumentException | IllegalAccessException e )
            {
            String errorMessage = String.format( "Failed to retrieve short value from class %s, field %s, instance %s: %s%s",
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 e.getClass().getSimpleName(),
                                                 ( e.getMessage() == null
                                                     ? "" 
                                                     : ": " + e.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, e ) ;
            }
        
        }   // end getShortField()
    
    
    /**
     * Set the value of a named short field in a given instance
     *
     * @param anInstance
     *     the instance to update
     * @param fieldName
     *     the name of the field in {@code anInstance} to set
     * @param newValue
     *     the value to store
     * @return the previous value stored in the named field
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     */
    public static short setShortField( Object anInstance,
                                       String fieldName,
                                       short newValue )
                                throws TestingException
        {
        try
            {
            final Field theField = getField( anInstance, fieldName, "short" ) ;
            final short oldValue = theField.getShort( anInstance ) ;
            
            theField.setShort( anInstance, newValue ) ;
            
            return oldValue ;
            }
        catch ( IllegalAccessException | SecurityException ex )
            {
            String errorMessage = String.format( "Failed to retrieve or set short value from/to class %s, field %s, instance %s: %s%s", 
                                                 anInstance.getClass().getSimpleName(),
                                                 fieldName,
                                                 anInstance.toString(),
                                                 ex.getClass().getSimpleName(),
                                                 ( ex.getMessage() == null
                                                     ? "" 
                                                     : ": " + ex.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, ex ) ;
            }
        
        }   // end setShortField()
    
    
    /*
     * utility methods for method invocation
     */
    

    /**
     * Utility method to invoke an instance or static method
     *
     * @param theClass
     *     the class for the static method invocation
     * @param anInstance
     *     the instance context for the method invocation - will be null for static 
     *     methods
     * @param methodName
     *     the name of the method to execute
     * @param parameterTypes
     *     the types of the method's parameters
     * @param arguments
     *     any parameters to pass to the method
     * @return any value or object returned by the method - if the named method
     *     returns a primitive type, the value will be wrapped - if the method is
     *     void, then null
     * @throws TestingException
     *      any wrapped exceptions which may be thrown by reflection
     * @throws Throwable
     *     anything thrown by the named method
     */
    public static Object invoke( Class<?> theClass,
                                    Object anInstance,
                                    String methodName,
                                    Class<?>[] parameterTypes,
                                    Object... arguments )
                            throws TestingException, Throwable
        {
        
        try
            {
            Method theMethod = null ;
            
            for ( Method aMethod : theClass.getDeclaredMethods() )
                {
                if ( aMethod.getName().equals( methodName ) )
                    {
                    // check the parameters
                    Type[] definedParameterTypes = aMethod.getParameterTypes() ;
                    Type[] definedGenericParameterTypes = aMethod.getGenericParameterTypes() ;
                    
                    // no parameters defined
                    if ( definedParameterTypes.length == 0 )
                        {
                        if ( ( parameterTypes == null ) ||
                             ( parameterTypes.length == 0 ) )
                            {
                            // no parameters supplied - found it
                            theMethod = aMethod ;

                            break ;
                            }

                        // not a match - keep looking
                        continue ;
                        }
                    
                    // at least one parameter defined
                    if ( definedParameterTypes.length == parameterTypes.length )
                        {
                        // correct number of parameters
                        // see if they're the right types
                        boolean mismatch = false ;
                        for ( int i = 0; i < definedParameterTypes.length; i++ )
                            {
                            if ( !definedParameterTypes[ i ].equals( parameterTypes[ i ] ) && 
                                 !definedGenericParameterTypes[ i ].equals( parameterTypes[ i ] ) )
                                {
                                // mismatch
                                mismatch = true ;
                                
                                break ;
                                }
                            }
                        
                        if ( mismatch )
                            {
                            continue ;
                            }
                        
                        // found a match
                        theMethod = aMethod ;
                        
                        break ;
                        }
                    
                    }
                
                }
            
            if ( theMethod == null )
                {
                // didn't find a matching method
                throw new NoSuchMethodException() ;
                
                }
            
            theMethod.setAccessible( true ) ;
            
            // for static methods, anInstance is typically null
            return theMethod.invoke( anInstance, arguments ) ;
            
            }
        
        catch ( InvocationTargetException e )
            {
            // simply propagate any exception from the called method to our caller
            throw e.getCause() ;
            }
        
        catch ( NoSuchMethodException |
                SecurityException |
                IllegalAccessException |
                IllegalArgumentException e )
            {
            String errorMessage = String.format( "Failed to invoke method %s in class %s with argument(s): %s:%n\t%s%s",
                                                 methodName,
                                                 theClass.getSimpleName(),
                                                 Arrays.toString( arguments ),
                                                 e.getClass().getSimpleName(),
                                                 ( e.getMessage() == null
                                                     ? "" 
                                                     : ": " + e.getMessage() ) ) ;
            
            throw new TestingException( errorMessage, e ) ;
            }
        
        }   // end invoke()
    
    
    /*
     * utility methods for code structure analysis
     */
    
    
    /**
     * Determine if a field (instance or class variable) is of an annotation
     * type
     * 
     * @param field
     *        the field to test
     * @return true if {@code field} is of an annotation type; false otherwise
     */
    public static boolean isAnnotation( Field field )
        {
        return typeOf( field ).isAnnotation() ;

        }   // end isAnnotation()

    
    /**
     * Determine if a class is an array
     * 
     * @param aClass
     *        the class to test
     * @return true if {@code aClass} is an array class; false otherwise
     */
    public static boolean isArray( Class<?> aClass )
        {
        return aClass.isArray() ;

        }   // end isArray()


    /**
     * Determine if a field (instance or class variable) is of an array type
     * 
     * @param field
     *        the field to test
     * @return true if {@code field} is of an array class type; false otherwise
     */
    public static boolean isArray( Field field )
        {
        return isArray( field.getType() ) ;

        }   // end isArray()


    /**
     * Determine if a field (instance or class variable) is of a class reference
     * type
     * 
     * @param field
     *        the field to test
     * @return true if {@code field} is of a class reference type; false
     *         otherwise
     */
    public static boolean isClassReference( Field field )
        {
        return typeOf( field ).toString().startsWith( "class" ) ;

        }   // end isClassReference()


    /**
     * Determine if a field (instance or class variable) is of a class reference
     * type which is a collection ({@code implements java.util.Collection} or
     * is a subclass of a class which does)
     * 
     * @param field
     *        the field to test
     * @return true if {@code field} is of a class reference type; false
     *         otherwise
     */
    public static boolean isCollection( Field field )
        {
        try
            {
            return Class.forName( "java.util.Collection" )
                            .isInstance( field.get( field.getDeclaringClass()
                                                        .newInstance() ) ) ;
            }
        catch ( ClassNotFoundException | 
                InstantiationException | 
                IllegalAccessException e )
            {
            e.printStackTrace();
            
            return false ;
            }

        }   // end isCollection()


    /**
     * Determine if a field (instance or class variable) is of an enumeration
     * type
     * 
     * @param field
     *        the field to test
     * @return true if {@code field} is of an enumeration type; false otherwise
     */
    public static boolean isEnumeration( Field field )
        {
        return typeOf( field ).isEnum() ;

        }   // end isEnumeration()


    /**
     * Determine if a field (instance or class variable) is of an interface
     * reference type
     * 
     * @param field
     *        the field to test
     * @return true if {@code field} is of an interface reference type; false
     *         otherwise
     */
    public static boolean isInterfaceReference( Field field )
        {
        return typeOf( field ).isInterface() ;

        }   // end isInterfaceReference()


    /**
     * Determine if a field (instance or class variable) is of a primitive type
     * 
     * @param field
     *        the field to test
     * @return true if {@code field} is of a primitive type; false otherwise
     */
    public static boolean isPrimitive( Field field )
        {
        return typeOf( field ).isPrimitive() ;

        }   // end isPrimitive()


    /**
     * Determine if a field is a class variable
     * 
     * @param field
     *        the field to test
     * @return true if {@code field} is a class variable; false otherwise
     */
    public static boolean isStatic( Field field )
        {
        String[] parts =            field.toGenericString().split( " " ) ;
        boolean isStatic =          false ;
        
        switch ( parts[ 0 ] )
            {
            case "private":
            case "protected":
            case "public":
                isStatic =          parts[ 1 ].equals( "static" ) ;
                break ;
            default:
                isStatic =          parts[ 0 ].equals( "static" ) ;
            }

        return isStatic ;

        }   // end isStatic()
    
    
    /**
     * Determine if a field is of a primitive type
     * 
     * @param field
     *            the field to test
     * @return true if {@code field} is of a primitive type; false otherwise
     */
    public static Class<?> primitiveTypeOf( Field field )
        {
        Class<?> type =         typeOf( field ) ;
        
        while ( type.isArray() )
            {
            type =              type.getComponentType() ;
            }
        
        if ( ! type.isPrimitive() )
            {
            type =              null ;
            }
        
        return type ;
        
        }   // end primitiveTypeOf()
    
    
    /**
     * Determine the class of a field
     * 
     * @param field
     *            the field to test
     * @return the field's class
     */
    public static Class<?> typeOf( Field field )
        {
        Class<?> type ;
        
        if ( isArray( field ) )
            {
            type =              field.getType().getComponentType() ;
            }
        else
            {
            type =              field.getType() ;
            }

        return type ;
        
        }   // end typeOf()


    /**
     * Determine the visibility of a field
     * 
     * @param field
     *            the field to test
     * @return the field's visibility
     */
    public static String visibilityOf( Field field )
        {
        String[] parts =            field.toGenericString().split( " " ) ;
        String visibility =         null ;
        
        switch ( parts[ 0 ] )
            {
            case "private":
            case "protected":
            case "public":
                visibility =        parts[ 0 ] ;
                break ;
            default:
                visibility =        "package" ;
            }

        return visibility ;

        }   // end visibilityOf()

    }   // end class Reflection