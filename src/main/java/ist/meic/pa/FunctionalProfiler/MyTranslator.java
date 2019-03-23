package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import javassist.expr.*;

public class MyTranslator implements Translator {
    public void start(ClassPool pool)
        throws NotFoundException, CannotCompileException {}
    public void onLoad(ClassPool pool, String classname)
        throws NotFoundException, CannotCompileException
    {
        CtClass ctClass = pool.get(classname);
        if(ctClass.isInterface() || classname.equals("ist.meic.pa.FunctionalProfiler.CountReadsWrites"))
          return;
        //System.out.println(classname);
        CtMethod[] ctMethods = ctClass.getDeclaredMethods();
        for (CtMethod m : ctMethods){
          m.instrument(
          new ExprEditor(){
            public void edit(FieldAccess f) throws CannotCompileException{
              if (f.isReader() && !(f.getClassName().equals("java.lang.System"))){
                String template = "{ ist.meic.pa.FunctionalProfiler.CountReadsWrites.addRead( \"%s\"); $_ = $proceed($$); }";
                try{
                  System.out.println("read detected in " + f.getClassName());
                  f.replace(String.format(template, f.getClassName()));
                } catch (CannotCompileException e){ e.printStackTrace();}
              } /*else if(f.isWriter()) {
                String template = "{ writesCounter++; $_ = $proceed($$); }";
                f.replace(template);
              }*/
            }
          }
          );
        }
    }
}
