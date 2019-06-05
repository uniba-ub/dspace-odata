package entitys;

import java.util.HashMap;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;

public interface ComplexModel {
	
	
	public CsdlComplexType getComplexType();
	public FullQualifiedName getFullQualifiedName();
	public String getName();
	public String getRecourceTypeFilter();
	public int getParentFK();
	public HashMap<String, String> getMapping();
	public String getSchema();
	
	
}
