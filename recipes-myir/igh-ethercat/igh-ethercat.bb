SUMMARY = "Igh ethercat"
DESCRIPTION = "install some file of igh-ethercat in filesystem"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://licenses/GPL-2;md5=94d55d512a9ba36caa9b7df079bae19f"

S = "${WORKDIR}"

SRC_URI = "file://licenses/GPL-2 \
        file://sbin/ethercatctl \
        file://etc/init.d/ethercat \
        file://etc/sysconfig/ethercat \
        file://etc/ethercat.conf \
        file://ethercat \
        file://ec_nuvoton.ko \
        file://ec_master.ko \
        file://ec_ma35d1-mac.ko \
        file://ec_generic.ko \
        file://include/ecrt.h \
        file://include/ectty.h \
        file://share/bash-completion/completions/ethercat.bash_completion \
	file://ethercat.service \
          "
          
inherit  systemd

S = "${WORKDIR}"
do_install (){
	install -d ${D}${systemd_system_unitdir}
	install -d ${D}/sbin
	install -d ${D}/etc
	install -d ${D}/etc/init.d
	install -d ${D}/etc/sysconfig
	install -d ${D}/include
	install -d ${D}/share/bash-completion/completions
	install -d ${D}/lib/modules/5.10.140-rt73

        install -m 755 ${WORKDIR}/sbin/ethercatctl ${D}/sbin/ethercatctl
        install -m 755 ${WORKDIR}/etc/init.d/ethercat ${D}/etc/init.d/ethercat
        install -m 755 ${WORKDIR}/etc/sysconfig/ethercat ${D}/etc/sysconfig/ethercat
        install -m 755 ${WORKDIR}/etc/ethercat.conf ${D}/etc/ethercat.conf
        install -m 755 ${WORKDIR}/ethercat ${D}/sbin/ethercat
        install -m 755 ${WORKDIR}/ec_nuvoton.ko ${D}/lib/modules/5.10.140-rt73/ec_nuvoton.ko
        install -m 755 ${WORKDIR}/ec_master.ko ${D}/lib/modules/5.10.140-rt73/ec_master.ko
        install -m 755 ${WORKDIR}/ec_ma35d1-mac.ko ${D}/lib/modules/5.10.140-rt73/ec_ma35d1-mac.ko
        install -m 755 ${WORKDIR}/ec_generic.ko ${D}/lib/modules/5.10.140-rt73/ec_generic.ko
        install -m 755 ${WORKDIR}/include/ecrt.h ${D}/include/ecrt.h
        install -m 755 ${WORKDIR}/include/ectty.h ${D}/include/ectty.h
        install -m 755 ${WORKDIR}/share/bash-completion/completions/ethercat.bash_completion ${D}/share/bash-completion/completions/ethercat.bash_completion

        install -m 644 ${WORKDIR}/ethercat.service ${D}${systemd_system_unitdir}/ethercat.service
}

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "ethercat.service"
SYSTEMD_AUTO_ENABLE = "enable"

FILES_${PN} = "/"
INSANE_SKIP_${PN} = "file-rdeps"
