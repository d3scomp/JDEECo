CONFIGFILE = $(shell opp_configfilepath)
include $(CONFIGFILE)

INET_DIR ?= ${OMNETPP_ROOT}/../inet
MIXIM_DIR ?= ${OMNETPP_ROOT}/../mixim-2.3
JDEECO_OMNETPP_DIR ?= ../jdeeco-omnetpp
JDEECO_OMNETPP_MANET_DIR ?= ../jdeeco-omnetpp-manet

all: getlibs

getlibs:
	cp -v ${OMNETPP_LIB_DIR}/liboppcmdenvd.so .
	cp -v ${OMNETPP_LIB_DIR}/liboppcommond.so .
	cp -v ${OMNETPP_LIB_DIR}/liboppenvird.so .
	cp -v ${OMNETPP_LIB_DIR}/libopplayoutd.so .
	cp -v ${OMNETPP_LIB_DIR}/liboppnedxmld.so .
	cp -v ${OMNETPP_LIB_DIR}/liboppsimd.so .
	cp -v ${JDEECO_OMNETPP_DIR}/out/$(CONFIGNAME)/src/libjdeeco-omnetpp.so .
	cp -v ${JDEECO_OMNETPP_MANET_DIR}/out/$(CONFIGNAME)/src/libjdeeco-omnetpp-manet.so .
	cp -v ${MIXIM_DIR}/out/$(CONFIGNAME)/src/libmixim.so .
	cp -v ${INET_DIR}/out/$(CONFIGNAME)/src/libinet.so .

demo-normal:
	java -Xmx600M -cp ../dist/cz.cuni.mff.d3s.jdeeco.core-2.0.0.jar:../dist/jdeeco.jar:../dist/simulation.jar:../dist/simulation-demo.jar:../dist/org.eclipse.emf.common-2.9.0-v20130528-0742.jar:../dist/org.eclipse.emf.ecore-2.9.0-v20130528-0742.jar:../dist/org.eclipse.emf.ecore.xmi-2.9.0-v20130528-0742.jar:../dist/cloning-1.9.0.jar:../dist/objenesis-1.2.jar -Djava.compiler=NONE cz.cuni.mff.d3s.jdeeco.simulation.demo.Main

demo-valgrind:
	valgrind --trace-children=yes --error-limit=no --gen-suppressions=all --log-file=test.log java -Xmx600M -cp ../dist/cz.cuni.mff.d3s.jdeeco.core-2.0.0.jar:../dist/jdeeco.jar:../dist/simulation.jar:../dist/simulation-demo.jar:../dist/org.eclipse.emf.common-2.9.0-v20130528-0742.jar:../dist/org.eclipse.emf.ecore-2.9.0-v20130528-0742.jar:../dist/org.eclipse.emf.ecore.xmi-2.9.0-v20130528-0742.jar:../dist/cloning-1.9.0.jar:../dist/objenesis-1.2.jar -Djava.compiler=NONE cz.cuni.mff.d3s.jdeeco.simulation.demo.Main
