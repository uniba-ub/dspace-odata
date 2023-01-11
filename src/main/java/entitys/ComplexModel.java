package entitys;

import java.util.HashMap;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;

public interface ComplexModel {

	CsdlComplexType getComplexType();
	FullQualifiedName getFullQualifiedName();
	String getName();
	int getParentFK();
	HashMap<String, String> getMapping();
	String getSchema();

}
