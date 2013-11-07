package cz.cuni.mff.d3s.deeco.model.runtime.custom;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.PathNodeMemberImpl;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class PathNodeMemberExt extends PathNodeMemberImpl {

        public PathNodeMemberExt() {
                super();
        }
        
        @Override
        public boolean equals(Object that) {
                return that != null && that instanceof PathNodeMember;
        }
        
        @Override
        public int hashCode() {
                return 1847356;
        }
        
        @Override
        public String toString() {
                return "<MEMBER>";
        }        

}