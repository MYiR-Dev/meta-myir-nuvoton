require u-boot-common-ma35d1_2020.07.inc 

SUMMARY = "U-Boot bootloader fw_printenv/setenv utilities"
DEPENDS = "mtd-utils bison-native"


ENV_PATCH        ?= ""

LMA35_PATCH="file://0001-FEAT-fw-env-support.patch"

SRC_URI += " \
	${LMA35_PATCH} \
"


EXTRA_OEMAKE_class-target = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${CC} ${CFLAGS} ${LDFLAGS}" HOSTCC="${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}" STRIP=true V=1'
EXTRA_OEMAKE_class-cross = 'ARCH=${TARGET_ARCH} CC="${CC} ${CFLAGS} ${LDFLAGS}" STRIP=true V=1'

#inherit uboot-config
inherit uboot-config uboot-extlinux-config uboot-sign deploy cml1 python3native
do_configure () {
    if [ -n "${UBOOT_CONFIG}" ]; then
        unset i j
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]; then
                    oe_runmake -C ${S} O=${B}/${config} ${config}
                    if [ -n "${@' '.join(find_cfgs(d))}" ]; then
			echo $PWD
			export PATH=$PATH:../git/scripts/kconfig/
			echo $PATH
                        merge_config.sh -m -O ${B}/${config} ${B}/${config}/.config ${@" ".join(find_cfgs(d))}
                        oe_runmake -C ${S} O=${B}/${config} oldconfig
                    fi
                fi
            done
            unset j
        done
        unset i
        DEVTOOL_DISABLE_MENUCONFIG=true
    else
        if [ -n "${UBOOT_MACHINE}" ]; then
            oe_runmake -C ${S} O=${B} ${UBOOT_MACHINE}
        else
            oe_runmake -C ${S} O=${B} oldconfig
        fi
        merge_config.sh -m .config ${@" ".join(find_cfgs(d))}
        cml1_do_configure
    fi
}



do_compile () {
 	for config in ${UBOOT_MACHINE}; do
     	oe_runmake -C ${S} O=${B}/${config} envtools  
  done
      oe_runmake -C ${S} O=${B}/${config} u-boot-initial-env
}

do_install () {
	for config in ${UBOOT_MACHINE}; do
    install -d ${D}${base_sbindir}
    install -d ${D}${sysconfdir}
    install -m 755 ${B}/${config}/tools/env/fw_printenv ${D}${base_sbindir}/fw_printenv
    install -m 755 ${B}/${config}/tools/env/fw_printenv ${D}${base_sbindir}/fw_setenv


    install -m 0644 ${S}/tools/env/fw_env.config ${D}${sysconfdir}/fw_env.config
   
   done
   install -m 0644 ${B}/${config}/u-boot-initial-env ${D}${sysconfdir}/u-boot-initial-env
}

do_install_class-cross () {
	for config in ${UBOOT_MACHINE}; do
		install -d ${D}${bindir_cross}
		install -m 755 ${B}/${config}/tools/env/fw_printenv ${D}${bindir_cross}/fw_printenv
		install -m 755 ${B}/${config}/tools/env/fw_printenv ${D}${bindir_cross}/fw_setenv
   done
}

SYSROOT_PREPROCESS_FUNCS_class-cross = "uboot_fw_utils_cross"
uboot_fw_utils_cross() {
    sysroot_stage_dir ${D}${bindir_cross} ${SYSROOT_DESTDIR}${bindir_cross}
}

PROVIDES += "u-boot-nuvoton-fw-utils"
RPROVIDES_${PN} += "u-boot-nuvoton-fw-utils"

PACKAGE_ARCH = "${MACHINE_ARCH}"
BBCLASSEXTEND = "cross"
