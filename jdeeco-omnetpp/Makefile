JDK_HOME=/usr/lib/jvm/java-7-openjdk-amd64

CONFIGFILE = $(shell opp_configfilepath)
include $(CONFIGFILE)

all: checkmakefiles
	cd src && $(MAKE)

clean: checkmakefiles
	cd src && $(MAKE) clean

cleanall: checkmakefiles
	cd src && $(MAKE) clean
	rm -f src/Makefile
	rm -rf out/

# Create makefiles that allows building mixim without a separate INET installation.
makefiles:
	cd src && opp_makemake -f --deep --make-so -o jdeeco-omnetpp -O out -I${JDK_HOME}/include \
		-I${OMNETPP_SRC_DIR}/common -I${OMNETPP_INCL_DIR}/platdep -I${OMNETPP_SRC_DIR}/envir

checkmakefiles:
	@if [ ! -f src/Makefile ]; then \
	echo; \
	echo '=========================================================='; \
	echo 'src/Makefile does not exist. Please use "make makefiles"'; \
	echo 'to generate it!'; \
	echo '=========================================================='; \
	echo; \
	exit 1; \
	fi
