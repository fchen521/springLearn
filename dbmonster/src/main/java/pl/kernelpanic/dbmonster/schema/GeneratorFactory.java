package pl.kernelpanic.dbmonster.schema;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.xml.sax.Attributes;


final class GeneratorFactory
        extends AbstractObjectCreationFactory {
    public final Object createObject(Attributes attributes) throws Exception {
        String className = attributes.getValue("type");
        Class clazz = Class.forName(className);
        return clazz.newInstance();
    }
}


/* Location:              C:\Users\lenovo\Downloads\dbmonster-core-1.0.3\dbmonster-core-1.0.3.jar!\pl\kernelpanic\dbmonster\schema\GeneratorFactory.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.0.7
 */