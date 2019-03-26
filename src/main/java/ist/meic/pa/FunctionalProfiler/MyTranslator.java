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
      if(ctClass.isInterface() || classname.equals("ist.meic.pa.FunctionalProfiler.Log")) /* maybe change this*/
        return;

      /*CtMethod[] ctMethods = ctClass.getDeclaredMethods();
      CtConstructor[] ctConstructors = ctClass.getConstructors();
*/
      CtBehavior[] ctBehaviors = ctClass.getDeclaredBehaviors();

      for(CtBehavior c : ctBehaviors) {
        c.instrument(new ExprEditor() {
          public void edit(FieldAccess f) throws CannotCompileException {
            if (f.isReader() && !(f.getClassName().equals("java.lang.System"))){
              String template = "{ ist.meic.pa.FunctionalProfiler.Log.getInstance().addRead($0.getClass().getName()); $_ = $proceed($$); }";
              try{
                f.replace(template);
              } catch (CannotCompileException e){ e.printStackTrace();}
            } else if(f.isWriter()) {
             String template = "{if(!this.equals($0)) ist.meic.pa.FunctionalProfiler.Log.getInstance().addWrite( \"%s\"); $_ = $proceed($$); }";
             f.replace(String.format(template, f.getClassName()));
           }
        })
      }

/*
      for(CtConstructor c : ctConstructors){
        c.instrument(new ExprEditor(){
          public void edit(FieldAccess f) throws CannotCompileException {
            if (f.isReader() && !(f.getClassName().equals("java.lang.System"))){
              String template = "{ ist.meic.pa.FunctionalProfiler.Log.getInstance().addRead( \"%s\"); $_ = $proceed($$); }";
              try{
                f.replace(String.format(template, f.getClassName()));
              } catch (CannotCompileException e){ e.printStackTrace();}
            }

            else if(f.isWriter()) {
             String template = "{if(!this.equals($0)) ist.meic.pa.FunctionalProfiler.Log.getInstance().addWrite( \"%s\"); " +
             "System.out.println($0); $_ = $proceed($$); }";
             f.replace(String.format(template, f.getClassName()));
           }
          }
        });
      }

      for (CtMethod m : ctMethods){
        m.instrument(new ExprEditor(){
          public void edit(FieldAccess f) throws CannotCompileException {
            if (f.isReader() && !(f.getClassName().equals("java.lang.System"))){
              if (!f.getClassName().equals(classname)){
                String template = "{ ist.meic.pa.FunctionalProfiler.Log.getInstance().addRead( \"%s\"); $_ = $proceed($$); }";
                try{
                  f.replace(String.format(template, f.getClassName()));
                } catch (CannotCompileException e){ e.printStackTrace();}
              }
              else{     //evaluate in runtime in case it's a subclass
                String template = "{ ist.meic.pa.FunctionalProfiler.Log.getInstance().addRead($0.getClass().getName()); $_ = $proceed($$); }";
                try{
                  f.replace(template);
                } catch (CannotCompileException e){ e.printStackTrace();}
              }
            } else if(f.isWriter()) {
              if (!f.getClassName().equals(classname)){
                String template = "{ ist.meic.pa.FunctionalProfiler.Log.getInstance().addWrite( \"%s\"); $_ = $proceed($$); }";
                try{
                  f.replace(String.format(template, f.getClassName()));
                } catch (CannotCompileException e){ e.printStackTrace();}
              }
              else{
                String template = "{ ist.meic.pa.FunctionalProfiler.Log.getInstance().addWrite($0.getClass().getName()); $_ = $proceed($$); }";
                try{
                  f.replace(template);
                } catch (CannotCompileException e){ e.printStackTrace();}
              }
            }
          }
        });
        */
    }
  }

}
