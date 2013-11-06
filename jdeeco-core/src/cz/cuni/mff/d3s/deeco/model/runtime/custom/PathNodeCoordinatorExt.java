package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeCoordinatorImpl;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class PathNodeCoordinatorExt extends PathNodeCoordinatorImpl {

        public PathNodeCoordinatorExt() {
                super();
        }
        
        @Override
        public boolean equals(Object that) {
                return that != null && that instanceof PathNodeCoordinator;
        }
        
        @Override
        public int hashCode() {
                return 3872467;
        }
        
        @Override
        public String toString() {
                return "<COORDINATOR>";
        }        

}