/*******************************************************************************
 * Copyright 2012 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.deeco.test.unit.knowledge;

import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgePathHelper;

@Test
public class KPBuilderTest {

  public void appendToRoot() {
	  String res = KPBuilder.appendToRoot("root", "tail");
	  assertEquals(res,"root.tail");
  }

  public void appendToRootANullTail() {
	  String res = KPBuilder.appendToRoot("root", null);
	  assertEquals(res,"root");
  }
  
  public void appendToEmptyRoot() {
	  String res = KPBuilder.appendToRoot("","tail");
	  assertEquals(res,"tail");
  }

  public void appendToBlankRoot() {
	  String res = KPBuilder.appendToRoot("   ","tail");
	  assertEquals(res,"tail");
  }
  
  public void appendToRootBlankTail() {
	  String res = KPBuilder.appendToRoot("root","  ");
	  assertEquals(res,"root");
  }
  
  public void prependToRoot() {
	  String res = KPBuilder.prependToRoot("another", "test");
	  assertEquals(res,"test.another");
  }

  public void prependToRootANullTail() {
	  String res = KPBuilder.appendToRoot("another", null);
	  assertEquals(res,"another");
  }
  
  public void prependToEmptyRoot() {
	  String res = KPBuilder.appendToRoot("","test");
	  assertEquals(res,"test");
  }

  public void prependToBlankRoot() {
	  String res = KPBuilder.appendToRoot("   ","test");
	  assertEquals(res,"test");
  }
  
  public void prependToRootBlankTail() {
	  String res = KPBuilder.appendToRoot("another","  ");
	  assertEquals(res,"another");
  }

  public void decomposePath() {
	  String[] res = KPBuilder.decomposePath("a.decomposition.test");
	  assertEquals(res.length,3);
	  assertEquals(res[0],"a");
	  assertEquals(res[1],"decomposition");
	  assertEquals(res[2],"test");
  }
  
  public void decomposePathWithMiddleElementEmpty() {
	  String[] res = KPBuilder.decomposePath("a..test");
	  assertEquals(res.length,3);
	  assertEquals(res[0],"a");
	  assertEquals(res[1],"");
	  assertEquals(res[2],"test");
  }
  
  public void decomposePathWithMiddleElementBlank() {
	  String[] res = KPBuilder.decomposePath("a.   .test");
	  assertEquals(res.length,3);
	  assertEquals(res[0],"a");
	  assertEquals(res[1],"   ");
	  assertEquals(res[2],"test");
  }
 
  public void decomposePathWithLastElementEmpty() {
	  String[] res = KPBuilder.decomposePath("a.decomposition.");
	  assertEquals(res.length,3);
	  assertEquals(res[0],"a");
	  assertEquals(res[1],"decomposition");
	  assertEquals(res[2],"");
  }

  public void decomposePathWithLastElementBlank() {
	  String[] res = KPBuilder.decomposePath("a.decomposition.  ");
	  assertEquals(res.length,3);
	  assertEquals(res[0],"a");
	  assertEquals(res[1],"decomposition");
	  assertEquals(res[2],"  ");
  }

  public void decomposePathWithFirstElementEmpty() {
	  String[] res = KPBuilder.decomposePath(".decomposition.test");
	  assertEquals(res.length,3);
	  assertEquals(res[0],"");
	  assertEquals(res[1],"decomposition");
	  assertEquals(res[2],"test");
  }

  public void decomposePathWithFirstElementBlank() {
	  String[] res = KPBuilder.decomposePath("   .decomposition.test");
	  assertEquals(res.length,3);
	  assertEquals(res[0],"   ");
	  assertEquals(res[1],"decomposition");
	  assertEquals(res[2],"test");
  }
  
  public void decomposeANullPath() {
	  String[] res = KPBuilder.decomposePath(null);
	  assertEquals(res.length,0);
  }
  
  public void decomposeAnEmptyPath() {
	  String[] res = KPBuilder.decomposePath("");
	  assertEquals(res.length,0);
  }

  public void decomposeABlankPath() {
	  String[] res = KPBuilder.decomposePath("   ");
	  assertEquals(res.length,0);
  }

}
