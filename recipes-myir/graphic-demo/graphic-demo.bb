SUMMARY = "graphic-demo "
DESCRIPTION = "graphic-demo application"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://licenses/GPL-2;md5=94d55d512a9ba36caa9b7df079bae19f"

S = "${WORKDIR}"

SRC_URI = "file://licenses/GPL-2 \
		   file://position_test \
		   file://graphic_demo \
		   file://animatedtiles \
          "
          
S = "${WORKDIR}"
do_install (){
	install -d ${D}/usr/bin
	install -d ${D}/opt/

        install -m 755 ${WORKDIR}/graphic_demo ${D}/usr/bin/graphic_demo

	install -m 755 ${WORKDIR}/position_test ${D}/opt/position_test
	install -m 755 ${WORKDIR}/animatedtiles ${D}/opt/animatedtiles
}

FILES_${PN} = "/"
INSANE_SKIP_${PN} = "file-rdeps"
