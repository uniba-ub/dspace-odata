package entitys;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;

public interface Entity {
	
	public CsdlEntityType getEntityType();
	public FullQualifiedName getFullQualifiedName();
	public String getEntitySetName();
	public CsdlEntitySet getEntitySet();
	

}
