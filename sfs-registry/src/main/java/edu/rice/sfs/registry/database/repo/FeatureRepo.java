package edu.rice.sfs.registry.database.repo;

import edu.rice.sfs.registry.database.po.FeaturePO;
import org.springframework.data.repository.CrudRepository;

public interface FeatureRepo extends CrudRepository<FeaturePO, String> {

}
