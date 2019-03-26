package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import java.lang.reflect.*;
import java.util.Arrays;

public class WithFunctionalProfiler {

  public static void main(String[] args){
    try {
      Translator t = new MyTranslator();
      ClassPool pool = ClassPool.getDefault();

      /* Add print to profiled program */
      CtClass ctClass = pool.get(args[0]);
      CtMethod ctMainMethod = ctClass.getDeclaredMethod("main");
      String template = "{ ist.meic.pa.FunctionalProfiler.Log.getInstance().print(); }";
      ctMainMethod.insertAfter(template);

      Loader cl = new Loader();
      cl.addTranslator(pool, t);

      /* Class that keeps reads and writes is loaded by parent loader, so that field accesses in this class aren't counted */
      cl.delegateLoadingOf("ist.meic.pa.FunctionalProfiler.Log");

      if(args.length == 1) {
        cl.run(args[0], null);
      } else {
        cl.run(args[0], Arrays.copyOfRange(args, 1, args.length));
      }

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
