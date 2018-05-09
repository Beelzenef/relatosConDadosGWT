// FORMATING CODE GENERATORS EXAMPLES
// IN ORDER TO UNDERSTAND WTH IS GOING ON WITH THEM IN GWT
// PRETTY COOL STUFF THO

// SIMPLEST CODE GENERATOR EVER IN GWT
// FROM https://groups.google.com/forum/#!topic/google-web-toolkit/JjaEz7r_Hac


public class FunctionGenerator extends Generator {
    private static final String IMPL_TYPE_NAME = Function.class.getSimpleName() + "Impl";
    private static final String IMPL_PACKAGE_NAME = Function.class.getPackage().getName();

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String requestedClass) throws UnableToCompleteException {
        TypeOracle typeOracle = context.getTypeOracle();
        JClassType functionType = typeOracle.findType(requestedClass);
        assert Function.class.equals(functionType.getClass());

        ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(IMPL_PACKAGE_NAME, IMPL_TYPE_NAME);

        composerFactory.addImport(Function.class.getCanonicalName());
        composerFactory.addImplementedInterface(Function.class.getName());

        PrintWriter printWriter = context.tryCreate(logger, IMPL_PACKAGE_NAME, IMPL_TYPE_NAME);
        SourceWriter sourceWriter = composerFactory.createSourceWriter(context, printWriter);

        sourceWriter.print("public Object execute() {");
        sourceWriter.print("    return "Hello";");
        sourceWriter.print("}");

        sourceWriter.commit(logger);
        return IMPL_PACKAGE_NAME + "." + IMPL_TYPE_NAME;
    }
}

// "MAP" STUFF, CODE GENERATOR
// FROM https://groups.google.com/forum/#!topic/google-web-toolkit/JjaEz7r_Hac

package rm.gwt.core.rebind; 


import com.google.gwt.core.ext.Generator; 
import com.google.gwt.core.ext.GeneratorContext; 
import com.google.gwt.core.ext.TreeLogger; 
import com.google.gwt.core.ext.UnableToCompleteException; 

import com.google.gwt.core.ext.typeinfo.JClassType; 
import com.google.gwt.core.ext.typeinfo.TypeOracle; 

import com.google.gwt.user.rebind.ClassSourceFileComposerFactory; 
import com.google.gwt.user.rebind.SourceWriter; 

import java.io.PrintWriter; 

import java.util.Enumeration; 
import java.util.HashMap; 
import java.util.ResourceBundle; 


/** 
 * GWT generator that returns a <code>Map</code> (<code>HashMap</ code>) 
 * from a properties file. 
 * Usage :: Map myPropertyMap = (Map) GWT.create(MyPropertyMap.class); 
 * Where :: MyProperMap extends PropertyMap (marker interface) 
 *       :: MyPropertyMap.properties is in same package as MyPropertyMap.java 
 */ 
public class PropertyMapGenerator extends Generator { 


    /** Simple name of class to be generated */ 
    private String className = null; 

    /** Package name of class to be generated */ 
    private String packageName = null; 

    /** Fully qualified class name passed into GWT.create() */ 
    private String typeName = null; 


    // inherited generator method 
    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException { 

        this.typeName = typeName; 
        TypeOracle typeOracle = context.getTypeOracle(); 

        try { 

            // get classType and save instance variables 
            JClassType classType = typeOracle.getType(typeName); 
            packageName = classType.getPackage().getName(); 
            className = classType.getSimpleSourceName() + "Wrapper"; 
            // Generate class source code 
            generateClass(logger, context); 

        } catch (Exception e) { 

            // record to logger that Map generation threw an exception 
            logger.log(TreeLogger.ERROR, "PropertyMap ERROR!!!", e); 

        } 

        // return the fully qualifed name of the class generated 
        return packageName + "." + className; 

    } 

    /** 
     * Generate source code for new class. Class extends <code>HashMap</code>. 
     * 
     * @param logger Logger object 
     * @param context Generator context 
     */ 
    private void generateClass(TreeLogger logger, GeneratorContext context) { 

        // get print writer that receives the source code 
        PrintWriter printWriter = null; 
        printWriter = context.tryCreate(logger, packageName, className); 
        // print writer if null, source code has ALREADY been generated, return 
        if (printWriter == null) return; 

        // init composer, set class properties, create source writer 
        ClassSourceFileComposerFactory composer = null; 
        composer = new ClassSourceFileComposerFactory(packageName, className); 
        composer.setSuperclass("java.util.HashMap"); 
        SourceWriter sourceWriter = null; 
        sourceWriter = composer.createSourceWriter(context, printWriter); 

        // generator constructor source code 
        generateConstructor(sourceWriter); 
        // close generated class 
        sourceWriter.outdent(); 
        sourceWriter.println("}"); 

        // commit generated class 
        context.commit(logger, printWriter); 

    } 

    /** 
     * Generate source code for the default constructor. Create default 
     * constructor, call super(), and insert all key/value pairs from 
     * the resoruce bundle. 
     * 
     * @param sourceWriter Source writer to output source code 
     */ 
    private void generateConstructor(SourceWriter sourceWriter) { 

        // init resource bundle 
        ResourceBundle bundle = ResourceBundle.getBundle(typeName); 

        // start constructor source generation 
        sourceWriter.println("public " + className + "() { "); 
        sourceWriter.indent(); 
        sourceWriter.println("super();"); 

        // add statements to pub key/value pairs from the resrouce bundle 
        for (Enumeration<String> keys = bundle.getKeys(); keys.hasMoreElements();) { 
            String key = keys.nextElement(); 
            sourceWriter.println("put(\"" + Generator.escape(key) + "\", \"" + bundle.getString(key) + "\");"); 
        } 

        // end constructor source generation 
        sourceWriter.outdent(); 
        sourceWriter.println("}"); 

    } 

} 
