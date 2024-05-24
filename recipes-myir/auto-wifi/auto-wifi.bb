SUMMARY = "Auto wifi ap and sta"
DESCRIPTION = "Script to connect wifi sta and ap"
LICENSE = "PD"
PR = "r3"

SRC_URI = " \
    file://COPYING \
    file://myir_hostapd.conf \
    file://myir_udhcpd.conf \
"

LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING;md5=1c3a7fb45253c11c74434676d84fe7dd"

do_install () {
		install -d ${D}/etc
		
    cp -r ${WORKDIR}/myir_* ${D}/etc
}



#inherit allarch systemd

FILES_${PN} = "\
	     /etc/myir_hostapd.conf \
	     /etc/myir_udhcpd.conf \
"
