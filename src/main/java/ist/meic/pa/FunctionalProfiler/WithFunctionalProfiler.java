package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import java.lang.reflect.*;

public class WithFunctionalProfiler {
    public static void main(String[] args){
      try{
        Translator t = new MyTranslator();
        ClassPool pool = ClassPool.getDefault();

        CtClass ctClassCounter = pool.makeClass("ist.meic.pa.FunctionalProfiler.CountReadsWrites");
        CtField ctFieldReads = CtField.make("static java.util.Map readsMap = new java.util.HashMap();", ctClassCounter);
        CtField ctFieldWrites = CtField.make("static java.util.Map writesMap = new java.util.HashMap();", ctClassCounter);
        ctClassCounter.addField(ctFieldReads);
        ctClassCounter.addField(ctFieldWrites);

        CtMethod ctMethodAddRead = CtNewMethod.make("public static void addRead(java.lang.Object classname){" +
                                            " if (!readsMap.containsKey($1)) {" +
                                            "   Integer one = new Integer(1);" +
                                            "   readsMap.put($1, one);" +
                                            " }" +
                                            " else {" +
                                            "   Integer currentValue = readsMap.get($1);" +
                                            "   Integer nextValueInt = currentValue+1;" +
                                            "   readsMap.put($1, nextValueInt);" +
                                            " }" +
                                            " }", ctClassCounter);
        ctClassCounter.addMethod(ctMethodAddRead);

        CtMethod ctMethodPrint = CtNewMethod.make("public static void printCounters(){" +
                                                  " System.out.println(readsMap.entrySet());"+
                                                  "}", ctClassCounter);
        ctClassCounter.addMethod(ctMethodPrint);

        CtClass ctClass = pool.get(args[0]);
        CtMethod ctMainMethod = ctClass.getDeclaredMethod("main");
        String template = "{ ist.meic.pa.FunctionalProfiler.CountReadsWrites.printCounters(); }";
        ctMainMethod.insertAfter(template);


        Loader cl = new Loader();
        cl.addTranslator(pool, t);
        cl.run(args[0], null);

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
