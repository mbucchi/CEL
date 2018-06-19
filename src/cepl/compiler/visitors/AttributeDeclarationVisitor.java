package cepl.compiler.visitors;

import cepl.compiler.errors.DeclarationError;
import cepl.compiler.utils.DataType;
import cepl.parser.CEPLBaseVisitor;
import cepl.parser.CEPLParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AttributeDeclarationVisitor extends CEPLBaseVisitor<Map<String, Class>> {
    public Map<String, Class> visitAttribute_dec_list(CEPLParser.Attribute_dec_listContext ctx) {
        List<CEPLParser.Attribute_declarationContext> attributes = ctx.attribute_declaration();

        HashMap<String, Class> attributeMap = new HashMap<>();

        for (CEPLParser.Attribute_declarationContext attr : attributes) {
            String attributeName = attr.attribute_name().getText();
            String dataType = attr.datatype().getText();

            if (attributeMap.containsKey(attributeName)) {
                throw new DeclarationError("Attribute `" + attributeName + "` is declared more than once");
            }

            attributeMap.put(attributeName, DataType.getTypeFor(dataType));
        }

        return attributeMap;
    }
}
