package ist.meic.pa.FunctionalProfilerExtended;

import javassist.*;
import javassist.expr.*;

public class MyTranslator implements Translator {
  	public void start(ClassPool pool)
      	throws NotFoundException, CannotCompileException {}

  	public void onLoad(ClassPool pool, String classname)
      	throws NotFoundException, CannotCompileException
  	{
      	CtClass ctClass = pool.get(classname);
      	if(ctClass.isInterface())
        	return;

      	// create an instance of class Log
      	CtField f = CtField.make("private static ist.meic.pa.FunctionalProfilerExtended.Log log = ist.meic.pa.FunctionalProfilerExtended.Log.getInstance();", ctClass);
      	ctClass.addField(f);

      	CtBehavior[] ctBehaviors = ctClass.getDeclaredBehaviors();

      	//iterates all methods and constructors in class looking for field accesses
      	for(CtBehavior c : ctBehaviors) {

	      	if(c.hasAnnotation("ist.meic.pa.FunctionalProfilerExtended.Exclude"))
	      		continue;
	
	        c.instrument(new ExprEditor() {
	        	public void edit(FieldAccess f) throws CannotCompileException {
	        		// if a read operation is found, add read with field class name to class Log
	        		try {
			            if (f.isReader() && !(f.getClassName().equals("java.lang.System")) && 
			            	!f.getField().hasAnnotation("ist.meic.pa.FunctionalProfilerExtended.Exclude")){
			              	String template = "{ log.addRead($0.getClass().getName()); $_ = $proceed($$); }";
			              	f.replace(template);
		            	}
			            // if a write operation is found, add write with field class name to class Log
			            if(f.isWriter() && !f.getField().hasAnnotation("ist.meic.pa.FunctionalProfilerExtended.Exclude")) {
			            	String template = "{";
			            	// ignore writes to initialize fields in constructors
			            	if (c.getMethodInfo().isConstructor())
			                	template += "if(!this.equals($0)) ";
			            	template += " log.addWrite($0.getClass().getName()); $_ = $proceed($$); }";
			            	f.replace(template);
			            }
		            } catch (NotFoundException e) {e.printStackTrace();}
		        }
	        });
	    }
    }
}
