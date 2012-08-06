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
package cz.cuni.mff.d3s.deeco.test.playground;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.TypeUtils;

public class Test {

	public static class A {
		public List<String> a;
	}

	public static abstract class B {
		public Integer b;
	}

	public static interface I {
		public void get();
	}

	public static void main(String[] args) {
		try {
			Type t = A.class.getField("a").getGenericType();
			System.out.println(TypeUtils.isGenericType(t));
		} catch (Exception e) {
			System.out.println("desdasdfa");
		}
	}

}
