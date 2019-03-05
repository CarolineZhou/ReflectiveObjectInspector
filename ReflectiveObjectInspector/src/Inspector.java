/*
 * code that are have top and bottom "**********" border is 
 * taken from the ObjectInspector class. 
 * 
 * There are a lot of documentation in this class due to my 
 * first experience of using reflection 
 */


import java.util.*;
import java.lang.reflect.*;


public class Inspector extends ObjectInspector{
	
	
	public Inspector() {}	
	
	public void inspect(Object obj, boolean recursive)
	{
		Vector objectsToInspect = new Vector();
		Class ObjClass = obj.getClass(); 														// class object created(meta object)
		Class[] interfaces = ObjClass.getInterfaces();
		Class SuperClass;
		
		
		System.out.println("inside inspector: " + obj + " (recursive = "+recursive+")");
		
		//name of the immediate superclass
		SuperClass = ObjClass.getSuperclass();
		System.out.println("immediate superclass: " + SuperClass.getName());
		
		for(int i = 0; i < interfaces.length ; i++)
		{
			System.out.println("Interface " +  i + ": " + interfaces[i].getName());
		}
		
		//inspect the current class
		inspectFields(obj, ObjClass,objectsToInspect);
		
		
		
		
		
		//inspect the method in current class		
		if(ObjClass.getDeclaredMethods().length >= 1)
		{
			Method m = ObjClass.getDeclaredMethods()[0];
			m.setAccessible(true);
			
			// name
			try
			 {
				 System.out.println("Method: " + m.getName());
			 }
			 catch(Exception e) {}  
			
			// exception thrown
			Class[] exceptions = m.getExceptionTypes();
			for(int i = 0; i < exceptions.length; i++) 
			{
				try
				 {
					 System.out.println("Exception " + i + ": " + exceptions[i].getName());
				 }
				 catch(Exception e) {}  
			}
					
			Class[] parameters = m.getParameterTypes();
			for(int i = 0; i < parameters.length; i++) 
			{
				try
				 {
					 System.out.println("Parameter: " + parameters[i].getName());
				 }
				 catch(Exception e) {}  
			}
			
			Class returnType = m.getReturnType();
			try
			 {
				 System.out.println("Return Type: " + returnType.getName());
			 }
			 catch(Exception e) {}
			
			int mod = m.getModifiers();
			String retval = Modifier.toString(mod);
			try
			 {
				 System.out.println("Modifier: " + retval);
			 }
			 catch(Exception e) {}			
			
		}
		
		//inspect the constructors
		
		
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
			 int mod = f.getModifiers();
			 String retval = Modifier.toString(mod);
			 
			 
			 f.setAccessible(true);															// each field the f is pointing can be accessed
			
			 /* if field type is not primitive:
			  * 	add to the objectsToInspect vector
			  */
			 if(! f.getType().isPrimitive() ) 
				 objectsToInspect.addElement( f );
			
			 if(f.getType().isPrimitive()) {
				 try
				 {
					 System.out.println("Field: " + f.getName() + " = " + f.get(obj));
				 }
				 catch(Exception e) {}   
			 }
			 
			 try
			 {
				 System.out.println("Field type: " + f.getType());
			 }
			 catch(Exception e) {}
			 
			 
			 try
			 {
				 System.out.println("Field modifier: " + retval);
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