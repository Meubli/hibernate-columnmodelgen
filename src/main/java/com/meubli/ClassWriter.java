package com.meubli;

import com.meubli.model.MetaEntity;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public class ClassWriter {

    private static final String META_MODEL_CLASS_NAME_SUFFIX = "Columns_";

    public void writeClass (MetaEntity metaEntity, ProcessingEnvironment processingEnvironment) throws IOException {
        JavaFileObject builderFile = processingEnvironment.getFiler()
                .createSourceFile(metaEntity.getPackageName() + "." + metaEntity.getClassSimpleName()+ META_MODEL_CLASS_NAME_SUFFIX);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.print("package ");
            out.print(metaEntity.getPackageName());
            out.println(";");
            out.println();
            out.println("import javax.annotation.Generated;");
            out.println();

            out.println(String.format("@Generated(value = \"%s\")", JpaEntityProcessor.class.getCanonicalName()));
            out.print("public class ");
            out.print( metaEntity.getClassSimpleName() + META_MODEL_CLASS_NAME_SUFFIX );
            out.println( " {" );

            for (Map.Entry<String, String> entry: metaEntity.getMapFieldColumn().entrySet()) {
                printColumnField(out, entry.getKey(), entry.getValue());
            }
            out.println( "}" );
        }

    }

    public void printColumnField(PrintWriter out, String field, String value) {
        String finalField = camelToSnake(field).toUpperCase();
        out.println(String.format("\t public static final String %s = \"%s\";", finalField, value));
    }

    private String camelToSnake(String str)
    {

        // Empty String
        String result = "";

        // Append first character(in lower case)
        // to result string
        char c = str.charAt(0);
        result = result + Character.toLowerCase(c);

        // Traverse the string from
        // ist index to last index
        for (int i = 1; i < str.length(); i++) {

            char ch = str.charAt(i);

            // Check if the character is upper case
            // then append '_' and such character
            // (in lower case) to result string
            if (Character.isUpperCase(ch)) {
                result = result + '_';
                result
                        = result
                        + Character.toLowerCase(ch);
            }

            // If the character is lower case then
            // add such character into result string
            else {
                result = result + ch;
            }
        }

        // return the result
        return result;
    }

}
