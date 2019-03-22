package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import java.lang.reflect.*;

public class WithFunctionalProfiler {
    public static void main(String[] args){
      try{
        Translator t = new MyTranslator();
        ClassPool pool = ClassPool.getDefault();
       
        Loader cl = new Loader();
        cl.addTranslator(pool, t);
        cl.run(args[0], null);
        /*CtClass ctClass = pool.get(args[0]);

        CtMethod ctMainMethod = ctClass.getDeclaredMethod("main", String[].class);

        ctMainMethod.instrument(
          new ExprEditor(){
            public void edit(ConstructorCall c) throws CannotCompileException{
              System.out.println(c.getClassName());
            }
          }
        );*/



        //System.out.println(ctClass);

        /*Method mainMethod = Class.forName(args[0]).getMethod("main", String[].class);
        String[] params = null;
        mainMethod.invoke(null, (Object) params);*/
      } catch (Throwable e){
          e.printStackTrace();
      }
    }
}









/*expected output com estes espaços::

Total reads: 0 Total writes: 1
*nameOfPackage*.Student -> reads: 0 writes: 1


class Professor{
  public Professor(Student t){
    t.mark = 15;                //this field access counts because it is the field of another instance
    this.course = "PA";         // this doesn't count because I'm initializing the field of this instance
    System.out.println(this.course); //read counts
}
}

*/
