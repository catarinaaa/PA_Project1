package ist.meic.pa.FunctionalProfilerExtended;

import javassist.*;
import java.util.Arrays;

public class WithFunctionalProfiler {

  public static void main(String[] args){
    try {
      Translator t = new MyTranslator();
      ClassPool pool = ClassPool.getDefault();

      /* Add print to profiled program */
      CtClass ctClass = pool.get(args[0]);
      CtMethod ctMainMethod = ctClass.getDeclaredMethod("main");
      String template = "{ ist.meic.pa.FunctionalProfilerExtended.Log.getInstance().print(); }";
      ctMainMethod.insertAfter(template);

      Loader cl = new Loader();
      cl.addTranslator(pool, t);

      /* Class that keeps reads and writes is loaded by parent loader, so that field accesses in this class aren't counted */
      cl.delegateLoadingOf("ist.meic.pa.FunctionalProfilerExtended.Log");

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
