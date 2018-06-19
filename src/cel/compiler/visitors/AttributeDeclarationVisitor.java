package cel.compiler.visitors;

import cel.compiler.errors.DeclarationError;
import cel.compiler.errors.UnknownDataTypeError;
import cel.compiler.utils.DataType;
import cel.parser.CEPLBaseVisitor;
import cel.parser.CEPLParser;

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
                throw new DeclarationError("Attribute `" + attributeName + "` is declared more than once", ctx);
            }

            Class attrClass = DataType.getClassFor(dataType);

            if (attrClass == null) {
                throw new UnknownDataTypeError("`" + dataType + "` is not a valid data type", attr.datatype());
            }

            attributeMap.put(attributeName, DataType.getClassFor(dataType));
        }

        return attributeMap;
    }
}
