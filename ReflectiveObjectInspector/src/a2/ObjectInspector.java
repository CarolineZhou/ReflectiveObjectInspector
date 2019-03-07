package a2;
/*==========================================================================
File: ObjectInspector.java
Purpose:Demo Object inspector for the Asst2TestDriver

Location: University of Calgary, Alberta, Canada
Created By: Jordan Kidney
Created on:  Oct 23, 2005
Last Updated: Oct 23, 2005

***********************************************************************
If you are going to reproduce this code in any way for your asignment 
rember to include my name at the top of the file toindicate where you
got the original code from
***********************************************************************


========================================================================*/

import java.util.*;
import java.lang.reflect.*;



public class ObjectInspector
{
    public ObjectInspector() { }

    
    
    //-----------------------------------------------------------
    public void inspect(Object obj, boolean recursive)
    {
		Vector objectsToInspect = new Vector();
		Class ObjClass = obj.getClass(); 														// class object created(meta object)

		System.out.println("inside inspector: " + obj + " (recursive = "+recursive+")");
		
		//inspect the current class
		inspectFields(obj, ObjClass,objectsToInspect);
		
		if(recursive)
		    inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
	   
    }
    
    
    
    //-----------------------------------------------------------
	/* Inspect the fields in a class object
	 * @param: Object : base object
	 * @param: Class  : class object
	 * @param: Vector : objects to be inspected
	 * @param: boolean: recursive value indicates the need to find all 
	 * 					field object inside a class recursively
	 */
	 private void inspectFieldClasses(Object obj,Class ObjClass,
		     						Vector objectsToInspect,boolean recursive)
	 {
		 // If there is still object in a class to be inspected
		 if(objectsToInspect.size() > 0 )
			System.out.println("---- Inspecting Field Classes ----");
			
		 
		 Enumeration e = objectsToInspect.elements();
		 while(e.hasMoreElements())
		 {
			 Field f = (Field) e.nextElement();
			 System.out.println("Inspecting Field: " + f.getName() );
			
			 /*
			  * while all the objects to inspect item should be object
			  * inspect that object recursively.
			  */
			 try
			 {
				 System.out.println("******************");
				 inspect( f.get(obj) , recursive);
				 System.out.println("******************");
			 }
			catch(Exception exp) 
			{ 
				exp.printStackTrace(); //check what happened
			}
		 }
	 }
    
    
    
    //-----------------------------------------------------------
	 /* Inspecting the current class
	  * @param: Object : the base object
	  * @param: Class  : the class object of the object (meta object)
	  * @param: Vector : objects still need to be inspected	 
	  */
	 private void inspectFields(Object obj,Class ObjClass,Vector objectsToInspect)
	 {
		 /* if all the class object's fields length is more than one;
		  * getDeclaredFields() include all field in the current class
		  * no matter the accessibility
		  */
		 if(ObjClass.getDeclaredFields().length >= 1)
		 {
			 Field f = ObjClass.getDeclaredFields()[0];										// f is a pointer of first element in the fields
			
			 f.setAccessible(true);															// each field the f is pointing can be accessed
			
			 /* if field type is not primitive:
			  * 	add to the objectsToInspect vector
			  */
			 if(! f.getType().isPrimitive() ) 
				 objectsToInspect.addElement( f );
			
			 try
			 {
				 System.out.println("Field: " + f.getName() + " = " + f.get(obj));
			 }
			 catch(Exception e) {}    
		 }

		/*  keep inspect of the parent class and keep track of objects 
		 * that is still needed to be inspected
		 */
		if(ObjClass.getSuperclass() != null)
			inspectFields(obj, ObjClass.getSuperclass() , objectsToInspect);
	 }
}
