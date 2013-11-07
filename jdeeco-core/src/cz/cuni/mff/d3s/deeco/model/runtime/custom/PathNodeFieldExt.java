package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeFieldImpl;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class PathNodeFieldExt extends PathNodeFieldImpl {

        public PathNodeFieldExt() {
                super();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object that) {
                if (that != null && that instanceof PathNodeField) {
                        return ((PathNodeField) that).getName().equals(getName());
                }
                return false;
        }
        
        @Override
        public int hashCode() {
                return getName().hashCode();
        }
        
        @Override
        public String toString() {
                return getName();
        }
}