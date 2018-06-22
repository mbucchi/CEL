package cel.compiler.visitors;

import cel.compiler.errors.DeclarationError;
import cel.compiler.errors.UnknownDataTypeError;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;
import cel.parser.utils.StringCleaner;
import cel.values.ValueType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AttributeDeclarationVisitor extends CELBaseVisitor<Map<String, ValueType>> {
    public Map<String, ValueType> visitAttribute_dec_list(CELParser.Attribute_dec_listContext ctx) {
        List<CELParser.Attribute_declarationContext> attributes = ctx.attribute_declaration();

        HashMap<String, ValueType> attributeMap = new HashMap<>();

        for (CELParser.Attribute_declarationContext attr : attributes) {
            String attributeName = StringCleaner.tryRemoveQuotes(attr.attribute_name().getText());
            String dataType = attr.datatype().getText();

            if (attributeMap.containsKey(attributeName)) {
                throw new DeclarationError("Attribute `" + attributeName + "` is declared more than once", ctx);
            }

            ValueType attrType = ValueType.getValueFor(dataType);

            if (attrType == null) {
                throw new UnknownDataTypeError("`" + dataType + "` is not a valid data type", attr.datatype());
            }

            attributeMap.put(attributeName, ValueType.getValueFor(dataType));
        }

        return attributeMap;
    }
}
