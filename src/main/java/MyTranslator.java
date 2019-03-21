import javassist.*;
import javassist.expr.*;

public class MyTranslator implements Translator {
    public void start(ClassPool pool)
        throws NotFoundException, CannotCompileException {}
    public void onLoad(ClassPool pool, String classname)
        throws NotFoundException, CannotCompileException
    {
        CtClass ctClass = pool.get(classname);
        System.out.println(classname);
        CtMethod[] ctMethods = ctClass.getDeclaredMethods();
        CtField ctFieldReads = CtField.make("public int readsCounter = 0;", ctClass);
        CtField ctFieldWrites = CtField.make("public int writesCounter = 0;", ctClass);
        ctClass.addField(ctFieldReads);
        ctClass.addField(ctFieldWrites);
        for (CtMethod m : ctMethods){
          m.instrument(
          new ExprEditor(){
            public void edit(FieldAccess f) throws CannotCompileException{
              if (f.isReader()){
                String template = "{readsCounter++; $_ = $proceed($$);}";
                f.replace(String.format(template));
              }
            }
          }
          );
        }
    }
}